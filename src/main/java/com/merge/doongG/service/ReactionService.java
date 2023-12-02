package com.merge.doongG.service;

import com.merge.doongG.dto.ReactionDTO;

import java.util.UUID;

public interface ReactionService {
    ReactionDTO likePost(Long postId, UUID uuid);

    ReactionDTO dislikePost(Long postId, UUID uuid);

    ReactionDTO getReactionsByPostId(Long postId, UUID uuid);
}
