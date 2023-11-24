package com.merge.doongG.controller;

import com.merge.doongG.dto.ChNicknameDTO;
import com.merge.doongG.dto.ChProImgDTO;
import com.merge.doongG.dto.ChPwDTO;
import com.merge.doongG.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userAuth")
public class UserAuthController {
    private final UserService userService;

    @PostMapping("/chNick") // 닉네임 변경 (/user/updateNickname)
    public ResponseEntity<String> chNick(@RequestBody ChNicknameDTO dto) {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        String nickname = dto.getNickname();
        userService.chNickname(uuid, nickname);

        return ResponseEntity.ok().body("true");
    }

    @PostMapping("/chProImg") // 프로필 이미지 변경 (/user/updateProfileImg)
    public ResponseEntity<String> chProImg(@RequestBody ChProImgDTO dto) {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        String profileImg = dto.getProfileImg();
        userService.chProImg(uuid, profileImg);

        return ResponseEntity.ok().body("true");
    }

    @PostMapping("/chPw") // 비밀번호 변경 (/userAuth/chPw)
    public ResponseEntity<String> chPw(@RequestBody ChPwDTO dto) {
        UUID uuid = UUID.fromString((String) SecurityContextHolder.getContext().getAuthentication().getDetails());
        String password = dto.getPassword();
        userService.chPw(uuid, password);

        return ResponseEntity.ok().body("true");
    }
}
