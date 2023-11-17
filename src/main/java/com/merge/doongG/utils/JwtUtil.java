package com.merge.doongG.utils;

import io.jsonwebtoken.Claims; // claims: JWT payload에 저장되는 정보단위
import io.jsonwebtoken.Jwts; // JWT를 생성하고 검증하는 기능을 제공하는 클래스
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.UUID;

public class JwtUtil {
    // JWT에서 닉네임 추출
    public static String getUserName(String token, String key) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().get("userName", String.class);
    }

    // JWT 만료 여부 확인
    public static boolean isExpired(String token, String key) {
        // 토큰의 만료시간이 현재시간보다 이전인지 확인
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    // JWT 생성
    public static String createToken(String nickname, UUID uuid, String key, Long expireTimeMs) {
        Claims claims = Jwts.claims(); // JWT payload에 저장되는 정보단위
        claims.put("userName", nickname); // 정보는 key/value 쌍으로 저장되므로 userNmae : userName 형태로 저장
        claims.put("uuid", uuid);

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(new Date(System.currentTimeMillis())) // 토큰 발행 시간 정보
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs)) // 토큰 유효시간 설정
                .signWith(SignatureAlgorithm.HS256, key) // 사용할 암호화 알고리즘과 signature에 들어갈 secret값 세팅
                .compact();
    }
}
