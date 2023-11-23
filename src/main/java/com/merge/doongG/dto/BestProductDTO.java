package com.merge.doongG.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BestProductDTO {
    // 상품아이디
    private Long productID;
    // 상품명
    private String productName;
    // 상품이미지
    private String productImage;
    // 재고
    private int stock;
    // 가격
    private int price;
    // 할인가
    private int discountedPrice;
    // 조회수
    private int viewCount;
}
