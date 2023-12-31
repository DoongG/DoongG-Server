package com.merge.doongG.dto;

import com.merge.doongG.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long postId;
    private String title, content;
    private Integer views, commentCount, likeCount, dislikeCount, postCount;

    private Board board;

    @Builder.Default
    private List<CommentResponseDTO> comments = new ArrayList<>();

    @Builder.Default
    private List<PostImageDTO> postImages = new ArrayList<>();

    @Builder.Default
    private List<HashtagDTO> hashtags = new ArrayList<>();

    private UserSummaryDTO user;

    private String commentAllowed;
    private Timestamp createdAt, updatedAt;
}
