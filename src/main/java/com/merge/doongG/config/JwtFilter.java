package com.merge.doongG.config;

import com.merge.doongG.service.UserService;
import com.merge.doongG.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final String key;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader("Authorization"); // 헤더에서 토큰 받아오기

        // JWT 없거나, Bearer로 시작하지 않는 경우 쳐내기
        // JWT 없거나 직접 URL 입력으로 접근하는 경우를 쳐낼 수 있음
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 꺼내기
        String token = authorization.split(" ")[1];

        // 토큰이 expired 되었는지 확인 (만료 시간이 지났는지)
        if (JwtUtil.isExpired(token, key)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에서 userName 꺼내기
        String userName = JwtUtil.getUserName(token, key);
        // 토큰에서 uuid 꺼내기
        String uuid = JwtUtil.getUuid(token, key);

        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userName, null, List.of(new SimpleGrantedAuthority("USER")));
        // Detail 설정
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        authenticationToken.setDetails(uuid); // 이 값에 접근하기 위해서는 SecurityContextHolder.getContext().getAuthentication().getDetails()로 접근해야 함
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
