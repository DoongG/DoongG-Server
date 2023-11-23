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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID; // 상품아이디

    @Column(nullable = false)
    private String productName; // 상품명

    @Column(nullable = false)
    private String productImage; // 상품이미지

    @Column(nullable = false)
    private String productDescription; // 상품설명

    @Column(nullable = false)
    private String category; // 상품분류

    @Column(nullable = false)
    private int stock; // 재고

    @Column(nullable = false)
    private int price; // 가격

    @Column(nullable = false)
    private int discountedPrice; // 할인가

    @Column(nullable = false, columnDefinition = "integer default 0")
    private int viewCount; // 조회수

    // 생성일자 자동생성
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;
}
