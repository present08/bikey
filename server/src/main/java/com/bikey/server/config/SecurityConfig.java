package com.bikey.server.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.bikey.server.jwt.JwtFilter;
import com.bikey.server.jwt.JwtUtil;
import com.bikey.server.jwt.LoginFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JwtUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    // password BCrypt 암호화
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // security cors설정
        http
                .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://43.201.209.78:4000"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                })));

        // 세션로그인 관련 보안 csrf를 jwt를 사용함으로써 보안 설정을 비활성화
        http.csrf(auth -> auth.disable());
        // spring security에서 지원하는 폼로그인 방식을 비활성화
        http.formLogin(auth -> auth.disable());
        http.httpBasic(auth -> auth.disable());

        // login, /, join의 권한은 모든권한
        // /admin을 통한 접근은 ADMIN의 Role을 가진자만 접근할 수 있도록 설정
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/login", "/", "/join").permitAll()
                // Spring security에서 hasRole은 "ROLE_"이 prefix된다. -> ADMIN으로 설정할경우 실제 값은
                // ROLE_ADMIN이 된다.
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated());

        // LoginFilter 이전에 filter를 생성
        http.addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);

        // UsernamePasswordAuthenticationFilter의 위치에 커스텀 된 LoginFilter를 대신 사용
        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
                UsernamePasswordAuthenticationFilter.class);

        // 세션을 STATELESS상태로 관리
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}