package com.bikey.server.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bikey.server.dto.CustomUserDetails;
import com.bikey.server.model.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // request에서 Authorization 헤더를 검색
        String authorization = request.getHeader("Authorization");
        System.out.println("[JwtFilter] 요청 URI: " + request.getRequestURI());
        System.out.println("[JwtFilter] Authorization 헤더: " + request.getHeader("Authorization"));

        // System.out.println(authorization);

        // 토큰이 비정상적이면 메서드 종료
        // authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("[JwtFilter] Authorization 헤더 없음 또는 형식 오류");
            // 비정상일 경우 다음 필터로 데이터를 넘겨줌
            filterChain.doFilter(request, response);

            return;
        }

        // Bearer 제거
        String token = authorization.split(" ")[1];

        System.out.println("[JwtFilter] 추출된 토큰: " + token);

        // 토큰 시간 검증하기
        if (jwtUtil.isExpire(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"JWT token expired\"}");
            return;
            // filterChain.doFilter(request, response);
            // return;
        }

        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        User user = new User();
        user.setUsername(username);
        user.setPassword("tempPw"); // DB를 수시로 체크하기 때문에 임시 비밀번호 설정
        user.setRole(role);

        System.out.println(user);

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        System.out.println("Authorities: " + customUserDetails.getAuthorities());
        System.out.println("Authorities: " + customUserDetails);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
                customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
