package com.merge.doongG.controller;

import com.merge.doongG.dto.UserJoinDTO;
import com.merge.doongG.dto.UserLoginDTO;
import com.merge.doongG.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor // final이 선언된 모든 필드를 인자값으로 하는 생성자를 생성
@RequestMapping("/user")
public class UserController {
    private final UserService userService; // 생성자를 통해 주입받음

    @PostMapping("/join") // 회원가입 (/user/join)
    public ResponseEntity<String> join(@RequestBody UserJoinDTO dto) {
        userService.join(dto.getEmail(), dto.getPassword(), dto.getNickname(), dto.getProfileImg(), dto.getPhoneNumber());
        return ResponseEntity.ok().body("true"); // ok는 200을 의미
        // ReponseEntity.ok("회원가입이 완료되었습니다."); // 이렇게도 가능
    }

    @PostMapping("/login") // 로그인 (/user/login)
    public ResponseEntity<String> login(@RequestBody UserLoginDTO dto) {
        String token = userService.login(dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok().body(token); // 토큰 반환
    }
}
