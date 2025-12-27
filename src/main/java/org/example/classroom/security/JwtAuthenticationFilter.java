package org.example.classroom.security;

import cn.hutool.core.text.AntPathMatcher;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.classroom.dao.user.RoleMapper;
import org.example.classroom.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RoleMapper roleMapper;

    private static final AntPathMatcher MATCHER = new AntPathMatcher();

    /** 直接放行的路径 */
    private static final String[] SKIP = {
            "/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",

    };

    /* ========================== 主要过滤逻辑 ========================== */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String path = request.getServletPath();

        // 1. 白名单直接放过
        if (shouldSkip(path)) {
            chain.doFilter(request, response);
            return;
        }

        // 2. 解析令牌
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        String token = header.substring(7);

        try {
            // ★新增：① 黑名单校验
            String jti = jwtUtil.extractJti(token);
            if (jwtUtil.isBlacklisted(jti)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has been logged out");
                return;
            }

            // ★新增：② 过期时间校验（validateToken 已包含签名验证）
            if (!jwtUtil.validateToken(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired or invalid");
                return;
            }

            // 3. 构建认证对象
            String uid = jwtUtil.getUserIdFromToken(token);
            if (uid != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String roleName = jwtUtil.getRoleOrQuery(token, roleMapper);
                if (roleName != null) {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleName);
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            uid, null, List.of(authority));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

        } catch (JwtException | IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT");
            return;
        }

        chain.doFilter(request, response);
    }

    /* ========================== 工具 ========================== */
    /** 白名单匹配（Ant 风格） */
    private boolean shouldSkip(String path) {
        for (String pattern : SKIP) {
            if (MATCHER.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }
}