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
    private Integer views, commentCount, likeCount, dislikeCount;
    private UserSummaryDTO user;
    private Board board;
    private List<CommentResponseDTO> comments = new ArrayList<>();
    private List<PostImageDTO> postImages = new ArrayList<>();
    private List<HashtagDTO> hashtags = new ArrayList<>();
    private String commentAllowed;
    private Timestamp createdAt, updatedAt;

    public void setUser(UserSummaryDTO user) {
        this.user = user;
    }
}
