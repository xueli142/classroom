package org.example.demo5.filter;


import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo5.mapper.User.RoleMapper;
import org.example.demo5.util.JwtUtil;
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
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException,
    IOException {

        // 如果请求路径是 /auth/** 或其他公共路径，则直接放行，不进行 Token 验证
        String path = request.getServletPath();
        if (path.startsWith("/auth/") || path.startsWith("/swagger-ui/") || path.startsWith("/v3/api-docs")) {
            chain.doFilter(request, response);
            return;
        }
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        String token = header.substring(7);
        try{
            String uid = jwtUtil.getUserIdFromToken(token);
            if(uid!=null&&SecurityContextHolder.getContext().getAuthentication() == null){
                String roleName = jwtUtil.getRoleOrQuery(token, roleMapper);
                if(roleName!=null){
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleName);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            uid,
                            null,
                            List.of(authority));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            };

        }catch (
                JwtException | IllegalArgumentException e
        ){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT");
            return;
        }


        chain.doFilter(request, response);
    }}