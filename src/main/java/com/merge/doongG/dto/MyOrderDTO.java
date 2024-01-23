package com.merge.doongG.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyOrderDTO {
    private Long orderId; // 주문번호
    private String orderDate; // 주문일자
    private String orderStatus; // 주문상태
    private String postcode; // 우편번호
    private String address; // 주소
    private int quantity; // 주문수량
    private String productName; // 상품 이름
    private String productImg; // 상품 이미지
    private int productPrice; // 상품 가격
    private int productDiscountPrice;  // 할인 가격
}
