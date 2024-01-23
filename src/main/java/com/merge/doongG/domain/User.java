package com.merge.doongG.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "BINARY(16)", nullable = false, unique = true)
    private UUID uuid;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 15, nullable = false, unique = true)
    private String nickname;

    @Column
    private String profileImg;

    @Column(length = 15, nullable = false, unique = true)
    private String phoneNumber;

    // 장바구니 연결
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;

    // 리액션 연결
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reaction> reactions = new ArrayList<>();
}

