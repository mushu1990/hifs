package com.hifs.app.web.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain backEndSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // 开启 CSRF 并使用 Cookie 存储 Token
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                // Session 管理，基于状态的认证
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 创建或使用现有的 Session
                )
                // 请求授权规则
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**", "/dashboard/**").authenticated() // 后台管理相关路径需要认证
                        .requestMatchers("/login", "/logout").permitAll()              // 登录登出允许匿名访问
                        .anyRequest().denyAll()                                        // 其他请求拒绝
                )
                // 配置登录表单
                .formLogin(login -> login
                        .loginPage("/login")           // 登录页面路径
                        .defaultSuccessUrl("/dashboard") // 登录成功后的跳转路径
                        .permitAll()
                )
                // 配置登出
                .logout(logout -> logout
                        .logoutUrl("/logout")           // 登出路径
                        .logoutSuccessUrl("/login")     // 登出成功后的跳转路径
                        .invalidateHttpSession(true)   // 登出时销毁 Session
                );

        return http.build();
    }
}
