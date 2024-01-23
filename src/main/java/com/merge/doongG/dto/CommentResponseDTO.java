package com.merge.doongG.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {
    private Long commentId;
    private PostDTO post;
    private UserSummaryDTO commenter;
    private Long parentCommentId;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
