package com.merge.doongG.service;

import com.merge.doongG.domain.User;
import com.merge.doongG.repository.UserRepository;
import com.merge.doongG.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${jwt.token.secret}")
    private String key;
    private Long expireTimeMs = 1000L * 60 * 60; // 1시간

    // 회원가입
    public boolean join(String email, String password, String nickname, String profileImg, String phoneNumber) {
        // email 중복 체크
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new RuntimeException(email + "는 이미 있습니다.");
                }); // 에러 던지면 ExceptionManager에서 해당 에러를 받아서 처리

        // nickname 중복 체크
        userRepository.findByNickname(nickname)
                .ifPresent(user -> {
                    throw new RuntimeException(nickname + "는 이미 있습니다.");
                }); // 에러 던지면 ExceptionManager에서 해당 에러를 받아서 처리

        UUID uuid = UUID.randomUUID(); // 랜덤 uuid 생성

        // 저장
        User user = User.builder()
                .email(email)
                .uuid(uuid)
                .password(bCryptPasswordEncoder.encode(password)) // 암호화해서 저장
                .nickname(nickname)
                .profileImg(profileImg)
                .phoneNumber(phoneNumber)
                .build();

        userRepository.save(user);

        return true;
    }

    public String login(String email, String password) {
        Optional<User> selectedUser = userRepository.findByEmail(email);

        // 존재하지 않는 회원
        if (selectedUser.isEmpty()) {
            throw new RuntimeException("존재하지 않는 회원입니다.");
        }

        // 비밀번호가 틀린 경우
        if (!bCryptPasswordEncoder.matches(password, selectedUser.get().getPassword())) {
            throw new RuntimeException("비밀번호가 틀립니다.");
        }

        // 로그인 성공 시 토근 생성 후 return
        String token = JwtUtil.createToken(selectedUser.get().getNickname(), selectedUser.get().getUuid(), key, expireTimeMs);

        return token;
    }
}