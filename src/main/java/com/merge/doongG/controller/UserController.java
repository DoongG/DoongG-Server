package com.merge.doongG.controller;

import com.merge.doongG.dto.*;
import com.merge.doongG.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor // final이 선언된 모든 필드를 인자값으로 하는 생성자를 생성
@RequestMapping("/user")
public class UserController {
    private final UserService userService; // 생성자를 통해 주입받음

    @PostMapping("/emailCheck") // 이메일 중복 체크 (/user/emailCheck)
    public ResponseEntity<String> emailCheck(@RequestBody EmailCheckDTO dto) {
        boolean flag = userService.emailCheck(dto.getEmail());

        return flag ? ResponseEntity.ok().body("true") : ResponseEntity.ok().body("false");
    }

    @PostMapping("/nicknameCheck") // 닉네임 중복 체크 (/user/nicknameCheck)
    public ResponseEntity<String> nicknameCheck(@RequestBody NicknameCheckDTO dto) {
        boolean flag = userService.nicknameCheck(dto.getNickname());

        return flag ? ResponseEntity.ok().body("true") : ResponseEntity.ok().body("false");
    }

    @PostMapping("/smsAuth") // SMS 인증 (/user/smsAuth)
    public ResponseEntity<String> smsAuth(@RequestBody SmsAuthDTO dto) {
        String result = userService.sendSMS(dto.getPhoneNumber());
        System.out.print(result);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/join") // 회원가입 (/user/join)
    public ResponseEntity<String> join(@RequestBody UserJoinDTO dto) {
        userService.join(dto.getEmail(), dto.getPassword(), dto.getNickname(), dto.getProfileImg(), dto.getPhoneNumber());
        return ResponseEntity.ok().body("true"); // ok는 200을 의미
    }

    @PostMapping("/login") // 로그인 (/user/login)
    public ResponseEntity<String> login(@RequestBody UserLoginDTO dto) {
        String str = userService.login(dto.getEmail(), dto.getPassword());

        return ResponseEntity.ok().body(str); // 로그인 정보 반환
    }

    @PostMapping("/findEmail") // 이메일 찾기 (/user/findEmail)
    public ResponseEntity<String> findEmail(@RequestBody FindEmailDTO dto) {
        String email = userService.findEmail(dto.getNickname(), dto.getPhoneNumber());
        return ResponseEntity.ok().body(email);
    }

    @PostMapping("/emailAuth") // 이메일 인증 (/user/emailAuth)
    public ResponseEntity<String> emailAuth(@RequestBody EmailAuthDTO dto) {
        String result = userService.sendEmail(dto.getEmail());
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/resetPw") // 비밀번호 재설정 (/user/resetPw)
    public ResponseEntity<String> resetPw(@RequestBody ResetPwDTO dto) {
        userService.resetPw(dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok().body("true");
    }
}