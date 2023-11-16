package com.merge.doongG.controller;

import com.merge.doongG.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class PostController {
    @PostMapping
    public ResponseEntity<String> writePost(Authentication authentication) {
        return ResponseEntity.ok().body(authentication.getName() + "님이 게시물을 작성했습니다.");
    }
}