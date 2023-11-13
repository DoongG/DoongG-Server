package com.merge.doongG.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Getter
@Builder
@Entity
@AllArgsConstructor
@Table(name = "members")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(length = 15, nullable = false)
    private String nickname;

    @Column(length = 255)
    private String profileImg;

    @Column(nullable = false)
    private int role; // 0: 일반회원, 1: 관리자

    public User() {}
}
