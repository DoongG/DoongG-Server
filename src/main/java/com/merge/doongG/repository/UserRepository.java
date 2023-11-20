package com.merge.doongG.repository;

import com.merge.doongG.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // 이메일로 유저 찾기
    Optional<User> findByNickname(String nickname); // 닉네임으로 유저 찾기
    Optional<User> findByPhoneNumber(String phoneNumber); // 전화번호로 유저 찾기
}