package com.merge.doongG.dto;

import com.merge.doongG.domain.Board;
import com.merge.doongG.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long postId;
    private String title, content;
    private Integer views, commentCount;
    private User user;
    private Board board;
    private List<CommentDTO> comments;
    private List<PostImageDTO> postImages;
    private List<HashtagDTO> hashtags;
    private String commentAllowed;
    private Timestamp createdAt, updatedAt;

    public void setUser(User user) {
        this.user = user;
    }
}
