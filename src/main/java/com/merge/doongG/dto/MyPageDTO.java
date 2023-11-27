package com.merge.doongG.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageDTO {
    private String nickname; // 닉네임
    private String profileImg; // 프로필 이미지
    private String email; // 이메일
}
