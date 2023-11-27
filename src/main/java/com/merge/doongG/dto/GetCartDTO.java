package com.merge.doongG.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCartDTO {
    private Long productID; // 상품 ID
    private String productName; // 상품명
    private String productImage; // 상품 이미지
    private int price; // 상품 가격
    private int discountedPrice; // 할인 가격
    private int quantity; // 상품 수량
}
