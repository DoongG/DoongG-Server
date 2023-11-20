package com.merge.doongG.service;

import com.merge.doongG.domain.User;
import com.merge.doongG.repository.UserRepository;
import com.merge.doongG.utils.JwtUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecretKey;
    @Value("${coolsms.api.phone}")
    private String apiPhone;

    private DefaultMessageService messageService;

    @PostConstruct // 생성자 호출 후 자동으로 실행
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
    }

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${jwt.token.secret}")
    private String key;
    private Long expireTimeMs = 1000L * 60 * 60; // 1시간


    // 이메일 중복체크
    public boolean emailCheck(String email) {
        Optional<User> selectedUser = userRepository.findByEmail(email);

        return selectedUser.isEmpty() ? true : false;
    }

    // 닉네임 중복 제크
    public boolean nicknameCheck(String nickname) {
        Optional<User> selectedUser = userRepository.findByNickname(nickname);

        return selectedUser.isEmpty() ? true : false;
    }

    // SMS 인증
    public String sendSMS(String phoneNumber) {
        // 해당 전화번호로 이미 가입된 유저가 있는지 확인
        Optional<User> selectedUser = userRepository.findByPhoneNumber(phoneNumber);

        // 이미 가입된 유저가 있는 경우
        if (!selectedUser.isEmpty()) {
            return "false";
        }

//        Message message = new Message();

        // 6자리 랜덤 숫자 생성
        String authNumber = String.valueOf((int) (Math.random() * 900000) + 100000);

//        message.setFrom(apiPhone);
//        message.setTo(phoneNumber);
//        message.setText("[둥지] 자취생들의 안식처 둥지의 인증번호는 [" + authNumber + "] 입니다.");

//        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

        return authNumber;
    }

    // 회원가입
    public boolean join(String email, String password, String nickname, String profileImg, String phoneNumber) {
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

    // 로그인
    public String login(String email, String password) {

        Optional<User> selectedUser = userRepository.findByEmail(email);

        // 존재하지 않는 회원
        if (selectedUser.isEmpty()) {
            return "0";
        }

        // 비밀번호가 틀린 경우
        if (!bCryptPasswordEncoder.matches(password, selectedUser.get().getPassword())) {
            return "1";
        }

        // 로그인 성공 시 토근 생성 후 return
        String token = JwtUtil.createToken(selectedUser.get().getNickname(), selectedUser.get().getUuid(), key, expireTimeMs);

        return token;
    }

}