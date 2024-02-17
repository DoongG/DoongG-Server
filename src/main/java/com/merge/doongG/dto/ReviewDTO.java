package com.merge.doongG.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long reviewId; // 리뷰 식별자
    private String nickname; // 댓글 작성자
    private String content; // 댓글 내용
    private String createdAt; // 댓글 작성일자
}
