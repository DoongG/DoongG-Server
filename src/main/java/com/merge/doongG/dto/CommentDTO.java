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
public class CommentDTO {
    private Long commentId;
    private PostDTO post;
    private UserDTO commenter;
    private CommentDTO parentComment;
    private String content;
    private Timestamp createAt;
    private Timestamp updateAt;
}
