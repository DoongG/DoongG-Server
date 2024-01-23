package com.merge.doongG.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyRoomRivewDTO {
    private Long reviewId; // 리뷰 아이디
    private String address; // 주소
    private String content; // 리뷰 내용
    private String createdAt; // 생성일자
}
