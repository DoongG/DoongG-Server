package com.merge.doongG.repository;

import com.merge.doongG.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 유저 찾기
    Optional<User> findByEmail(String email);

    // 닉네임으로 유저 찾기
    Optional<User> findByNickname(String nickname);

    // 전화번호로 유저 찾기
    Optional<User> findByPhoneNumber(String phoneNumber);

    // 닉네임과 전화번호로 유저 찾기
    Optional<User> findByNicknameAndPhoneNumber(String nickname, String phoneNumber);

    // uuid로 유저 찾기
    Optional<User> findByUuid(UUID uuid);
}