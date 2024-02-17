package com.merge.doongG.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedProductDTO {
    private Long productID; // 상품 ID
    private String productName; // 상품명
    private String productImage; // 상품 이미지
    private String category; // 상품 카테고리
    private int stock; // 재고
    private int price; // 가격
    private int discountedPrice; // 할인가
    private int viewCount; // 조회수
}
