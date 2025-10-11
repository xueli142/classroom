package org.example.demo5.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.demo5.dto.ClaimsPayloadDto;
import org.example.demo5.mapper.User.RoleMapper;
import org.example.demo5.model.User.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct; // 注意：Java 9+ 可能会需要添加 javax.annotation-api 依赖
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // 从 application.properties 或 application.yml 中读取 JWT 密钥
    @Value("${jwt.secret}")
    private String secret;

    // 从 application.properties 或 application.yml 中读取 JWT 过期时间（毫秒）
    @Value("${jwt.expiration}")
    private long expiration;

    private Key key; // 用于签名和验证的密钥

    // 在构造函数之后初始化密钥
    @PostConstruct
    public void init() {
        // 确保密钥足够长（至少 256 位，即 32 字节）
        // 这里使用 Base64 解码你的 secret 字符串作为密钥
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }
    // 生成令牌，这里和创造令牌的函数不用，这里是为了安全暴露少部分接口出去
    //仅仅暴露需要的接口，username出去


    // 创建令牌的实际逻辑
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                //1.这里是JWT的header部分
                .setClaims(claims) // 设置声明
                .setSubject(subject) // 设置主题（通常是用户名）
                //2.这里是payload部分
                .setIssuedAt(new Date(System.currentTimeMillis())) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 设置过期时间
                //3.这里是签名部分，包含指定算法和密钥，防止被修改
                .signWith(key, SignatureAlgorithm.HS256) // 使用密钥和算法签名
                .compact(); // 压缩成 JWT 字符串
    }


    /**
     注意这里的claims作为主题部分，包含主要信息
     这里添加username,name,role等信息
     */
    public String generateToken(String uid,String name,String uuid,String role) {

        Map<String, Object> claims = new HashMap<>();
        //这里初始化了一个空的 claims Map。
        // 你可以在这里添加额外的声明，例如用户角色
        // claims.put("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        claims.put("uuid", uuid); // 添加用户ID声明
        claims.put("name", name); // 添加姓名声明
        claims.put("role", role); // 添加角色声明
        // 这里可以添加其他自定义声明，例如用户 ID、权限等



        return createToken(claims, uid); // 调用 createToken 方法生成 JWT
    }

    public Boolean validateToken(String token) {
        try {
            // 提取token中的所有声明
            extractAllClaims(token);
            // 判断token是否过期
            return !isTokenExpired(token);
        } catch (Exception e) {
            // 如果出现异常，返回false
            return false;
        }
    }



//它们是用于解析已存在的 Token，而不是创建新的 Token。


    // 从令牌中获取用户名
    public String getUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 从令牌中获取过期日期
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

/**
 * 从JWT令牌中提取特定的声明信息
 * 这是一个泛型方法，可以提取不同类型的声明
 * @param token JWT令牌字符串
 * @param claimsResolver 用于处理Claims对象的函数式接口
 * @param <T> 返回值的类型
 * @return 根据claimsResolver处理后的声明值
 */
    // 从令牌中获取特定声明
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    // 首先提取令牌中的所有声明
        final Claims claims = extractAllClaims(token);
    // 使用提供的函数式接口处理并返回特定的声明值
        return claimsResolver.apply(claims);
    }

    // 获取其中的uid字段
    public String getUserIdFromToken(String token) {
        // 使用 extractClaim 方法，传入一个 Lambda 表达式
        // 这个表达式会从 Claims 对象中获取 "uid" 字段，并将其作为 String 类型返回
        return extractClaim(token, claims -> claims.get("uid", String.class));
    }

/**
 * 从令牌中提取UUID信息
 * @param token JWT令牌字符串
 * @return 提取出的UUID字符串
 */
    public String getUuidFromToken(String token) {
    // 使用extractClaim方法从令牌中提取特定声明
    // 这里提取的是名为"uuid"的声明，并将其转换为String类型返回
        return extractClaim(token, claims -> claims.get("uuid", String.class));
    }
    /**
     * 从令牌中获取name字段的方法
     * @param token JWT令牌字符串
     * @return 返回令牌中包含的name字段的值
     */

    public String getNameFromToken(String token) {
        return extractClaim(token, claims -> claims.get("name", String.class));
    }
/**
 * 从给定的令牌中提取角色信息
 * @param token JWT令牌字符串
 * @return 返回令牌中存储的角色信息字符串
 */
    public String getRoleFromToken(String token) {
    // 使用extractClaim方法从令牌中提取特定声明，这里提取角色信息
    // 参数claims -> claims.get("role", String.class)表示从声明中获取键为"role"的值，并指定其类型为String
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // 从令牌中获取所有声明
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // 检查令牌是否过期
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    // 验证令牌
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = getUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * 从令牌里同时取出 uid、uuid、role
     * 如果 token 生成时没放 role，返回的 role 为 null
     */
    public ClaimsPayloadDto parseToken(String token) {
        Claims claims = extractAllClaims(token);
        return ClaimsPayloadDto.builder()
                .uid(claims.getSubject())          // 我们生成时 subject=uid
                .uuid(claims.get("uuid", String.class))
                .role(claims.get("role", String.class))
                .build();
    }

    /**
     * 先尝试从 token 拿 role；拿不到就用 uid+uuid 查库（降级）
     */
    public String getRoleOrQuery(String token, RoleMapper roleMapper) {
        ClaimsPayloadDto payload = parseToken(token);
        if (payload.getRole() != null) {
            return payload.getRole();
        }
        // 降级查库
        Role role = roleMapper.selectByBiz(payload.getUid(), payload.getUuid());
        return role == null ? null : role.getRole();
    }



}