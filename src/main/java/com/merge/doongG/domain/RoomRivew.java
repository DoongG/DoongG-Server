package com.merge.doongG.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class RoomRivew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long reviewId;

    // user 테이블과 연결
    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private User user;

    // 주소
    @Column(nullable = false)
    private String address;

    // 위도
    @Column(nullable = false)
    private String latitude;

    // 경도
    @Column(nullable = false)
    private String longitude;

    // 리뷰 내용
    @Column(nullable = false)
    private String content;

    // 생성일자 자동생성
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;
}
