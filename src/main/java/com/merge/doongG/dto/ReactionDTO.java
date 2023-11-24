package com.merge.doongG.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReactionDTO {
    private Long postId;
    private Long userId;
    private boolean liked;
    private boolean disliked;
    private Integer likes;
    private Integer dislikes;
}
