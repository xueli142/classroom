/**
 * JWT 工具类
 * 职责：
 *  1. 生成 / 解析 / 验证 Token
 *  2. 黑名单（登出）支持
 *  3. 统一封装 Claims 对象，避免魔法值散落各处
 *
 * 作者：your-name
 * 日期：2025-11-05
 */
package org.example.classroom.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.example.classroom.dao.user.RoleMapper;
import org.example.classroom.transfer.dto.besides.ClaimsPayloadDto;
import org.example.classroom.model.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtUtil {

    /* ========================== 常量 ========================== */
    /** Redis 黑名单前缀 */
    private static final String BLACK_PREFIX = "blacklist:";

    /* ========================== 注入 ========================== */
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.secret}")
    private String secret;          // 至少 32 字节（256 bit）

    @Value("${jwt.expiration}")
    private long expiration;        // 单位：毫秒

    /** 签名密钥（由 secret 生成） */
    private Key key;

    /* ========================== 初始化 ========================== */
    @PostConstruct
    public void init() {
        // 将配置里的字符串转为 JWT 要求的 Key
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 生成 JWT
     * @param userId 用户业务主键
     * @param role   角色编码
     * @return 完整 JWT 字符串
     */
    public String generateToken(String userId, String role) {
        long now = System.currentTimeMillis();
        String jti = UUID.randomUUID().toString();   // 黑名单唯一编号

        // 只放“业务需要随 token 带走”的私有声明
        Map<String, Object> claims = Map.of("role", role,
                "userId",userId
        );

        return Jwts.builder()
                .setId(jti)                                // jti
                .setSubject(userId)                        // sub → 用户主键
                .setIssuedAt(new Date(now))                // iat
                .setExpiration(new Date(now + expiration)) // exp
                .addClaims(claims)                         // 私有声明
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /* ========================== 2. 解析 Token ========================== */
    /** 提取全部 Claims（会验签） */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /** 泛型：从 Token 中抽取指定声明 */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    /** 取 subject（即 userId） */
    public String getUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /** 取过期时间 */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /** 取 JWT ID（用于黑名单） */
    public String extractJti(String token) {
        return extractAllClaims(token).getId();
    }

    /** 取剩余有效期（秒） */
    public long getRemainingSeconds(String token) {
        long remaining = extractExpiration(token).getTime() - System.currentTimeMillis();
        return Math.max(0, remaining / 1000);
    }

    /* ---------- 业务快捷方法 ---------- */
    public String getUserIdFromToken(String token) {
        return extractClaim(token, claims -> claims.get("userId", String.class));
    }

    public String getRoleFromToken(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    /** 统一封装 DTO，避免魔法值 */
    public ClaimsPayloadDto parseToken(String token) {
        Claims claims = extractAllClaims(token);
        return ClaimsPayloadDto.builder()
                .userId(claims.get("userId", String.class))
                .role(claims.get("role", String.class))
                .build();
    }

    /* ========================== 3. 验证 Token ========================== */
    /** 仅验证：是否过期 & 签名正确 */
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /** 验证：是否过期 & 用户名一致 */
    public Boolean validateToken(String token, String username) {
        return username.equals(getUsername(token)) && !isTokenExpired(token);
    }

    /** 判断 Token 是否已过期 */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /* ========================== 4. 黑名单 ========================== */
    /** 将 Token 加入黑名单（退出登录） */
    public void addToBlacklist(String jti, long expireSeconds) {
        redisTemplate.opsForValue()
                .set(BLACK_PREFIX + jti, "1",
                        Duration.ofSeconds(expireSeconds));
    }

    /** 判断 Token 是否在黑名单 */
    public boolean isBlacklisted(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACK_PREFIX + jti));
    }

    /* ========================== 5. 降级查库 ========================== */
    /**
     * 先取 Token 中的 role，若为空则拿 userId 查库兜底
     * @param token      JWT
     * @param roleMapper DAO
     * @return 最终角色编码
     */
    public String getRoleOrQuery(String token, RoleMapper roleMapper) {
        ClaimsPayloadDto payload = parseToken(token);
        if (payload.getRole() != null) {
            return payload.getRole();
        }
        Role role = roleMapper.selectByBiz(payload.getUserId());
        return role == null ? null : role.getRole();
    }
}