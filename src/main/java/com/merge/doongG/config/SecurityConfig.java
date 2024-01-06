package com.merge.doongG.config;

import com.merge.doongG.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity // Spring Security 활성화
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserService userService;

    @Value("${jwt.token.secret}")
    private String key;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // 인증이 필요없는 경로 설정
        return (web) -> web.ignoring().requestMatchers("/boards/**","/user/**","/user/join" , "/user/login", "/shop/**", "/roomRivew/**", "/food/**", "/swagger-ui/**", "/v3/**"); // user, shop 경로는 인증이 필요없음
    }

    // spring security 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
                // cors 설정 허용
                .cors(config -> {
                    config.configurationSource(request -> {
                        var cors = new org.springframework.web.cors.CorsConfiguration();
                        cors.setAllowedOrigins(List.of("*"));
                        cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                        cors.setAllowedHeaders(List.of("*"));
                        return cors;
                    });
                })
                .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 페이지 없애기
                .logout(AbstractHttpConfigurer::disable) // 기본 로그아웃 페이지 없애기
                .authorizeRequests()
                .requestMatchers(HttpMethod.POST,"/boards/**").authenticated() // POST /boards 요청에 대해 인증 필요로 함
                .anyRequest().authenticated() // 나머지 요청에 대해 인증 필요로 함
                .and()
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 사용하지 않겠다는 의미
                // JwtFilter를 추가해서 접근에 대해 JWT를 검증하도록 설정
                .addFilterAfter(new JwtFilter(userService, key), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
