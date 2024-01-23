package com.merge.doongG.dto;

// 상품 하나 조회

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetOneDTO {
    private Long productID; // 상품아이디
    private String productName; // 상품명
    private String productImage; // 상품 이미지
    private String productDescription; // 상품 설명
    private String category; // 카테고리
    private int stock; // 재고
    private int price; // 정상가
    private int discountedPrice; // 할인가
    private int viewCount; // 조회수
    private String createdAt; // 생성일자
    private List<ReviewDTO> reviews; // 댓글 배열 (작성자, 내용, 작성일자)
}