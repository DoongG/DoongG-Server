package com.merge.doongG.controller;

import com.merge.doongG.dto.*;
import com.merge.doongG.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증 불필요 사용자 API", description = "사용자 관리를 위한 API")
@RestController
@RequiredArgsConstructor // final이 선언된 모든 필드를 인자값으로 하는 생성자를 생성
@RequestMapping("/user")
public class UserController {
    private final UserService userService; // 생성자를 통해 주입받음

    @Operation(
            summary = "이메일 중복 확인",
            description = "이메일 중복을 확인합니다. 이메일이 중복되면 'true'를 반환하고, 그렇지 않으면 'false'를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이메일 중복 확인 성공", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/emailCheck")
    public ResponseEntity<String> emailCheck(@RequestBody EmailCheckDTO dto) {
        boolean flag = userService.emailCheck(dto.getEmail());

        return flag ? ResponseEntity.ok().body("true") : ResponseEntity.ok().body("false");
    }

    @Operation(
            summary = "닉네임 중복 확인",
            description = "닉네임 중복을 확인합니다. 닉네임이 중복되면 'true'를 반환하고, 그렇지 않으면 'false'를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "닉네임 중복 확인 성공", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/nicknameCheck")
    public ResponseEntity<String> nicknameCheck(@RequestBody NicknameCheckDTO dto) {
        boolean flag = userService.nicknameCheck(dto.getNickname());

        return flag ? ResponseEntity.ok().body("true") : ResponseEntity.ok().body("false");
    }

    @Operation(
            summary = "SMS 인증",
            description = "SMS 인증을 수행합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "SMS 인증 성공", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/smsAuth")
    public ResponseEntity<String> smsAuth(@RequestBody SmsAuthDTO dto) {
        String result = userService.sendSMS(dto.getPhoneNumber());
        System.out.print(result);
        return ResponseEntity.ok().body(result);
    }

    @Operation(
            summary = "회원 가입",
            description = "회원 가입을 수행합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 가입 성공", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserJoinDTO dto) {
        userService.join(dto.getEmail(), dto.getPassword(), dto.getNickname(), dto.getProfileImg(), dto.getPhoneNumber());
        return ResponseEntity.ok().body("true"); // ok는 200을 의미
    }

    @Operation(
            summary = "로그인",
            description = "로그인을 수행합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO dto) {
        String str = userService.login(dto.getEmail(), dto.getPassword());

        return ResponseEntity.ok().body(str); // 로그인 정보 반환
    }

    @Operation(
            summary = "이메일 조회",
            description = "닉네임과 전화번호로 사용자 이메일을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이메일 조회 성공", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/findEmail")
    public ResponseEntity<String> findEmail(@RequestBody FindEmailDTO dto) {
        String email = userService.findEmail(dto.getNickname(), dto.getPhoneNumber());
        return ResponseEntity.ok().body(email);
    }

    @Operation(
            summary = "이메일 인증",
            description = "이메일을 통한 인증 메일을 발송합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이메일 인증 성공", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/emailAuth")
    public ResponseEntity<String> emailAuth(@RequestBody EmailAuthDTO dto) {
        String result = userService.sendEmail(dto.getEmail());
        return ResponseEntity.ok().body(result);
    }

    @Operation(
            summary = "비밀번호 재설정",
            description = "사용자 비밀번호를 재설정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "비밀번호 재설정 성공", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/resetPw")
    public ResponseEntity<String> resetPw(@RequestBody ResetPwDTO dto) {
        userService.resetPw(dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok().body("true");
    }
}