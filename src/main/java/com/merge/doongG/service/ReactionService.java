package com.merge.doongG.service;

import com.merge.doongG.dto.ReactionDTO;

public interface ReactionService {
    ReactionDTO likePost(Long postId, Long userId);

    ReactionDTO dislikePost(Long postId, Long userId);

    ReactionDTO getReactionsByPostId(Long postId, Long userId);
}
