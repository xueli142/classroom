package org.example.demo5.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // 配置所有请求无需认证（测试环境用）
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // 允许所有请求
                .csrf(csrf -> csrf.disable()); // 关闭CSRF（测试环境简化）
        return http.build();
    }
}
