package com.merge.doongG.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserDTO {
    private Long id;
    private UUID uuid;
    private String email;
    private String password;
    private String nickname;
    private String profileImg;
    private String phoneNumber;
}
