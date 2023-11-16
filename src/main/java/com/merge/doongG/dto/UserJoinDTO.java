package com.merge.doongG.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserJoinDTO {
    private String email;
    private String password;
    private String nickname;
    private String profileImg;
    private String phoneNumber;
}
