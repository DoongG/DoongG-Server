package com.merge.doongG.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long productId; // 상품번호
    private String postcode; // 우편번호
    private String address; // 상세주소
    private int quantity; // 주문수량
}
