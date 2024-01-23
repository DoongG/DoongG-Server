package com.merge.doongG.service;

import com.merge.doongG.dto.CommentRequestDTO;

import java.util.UUID;

public interface CommentService {
    void addComment(Long postId, CommentRequestDTO commentDTO, UUID uuid);

    void updateComment(Long commentId, CommentRequestDTO commentDTO, UUID uuid);

    void deleteComment(Long commentId, UUID uuid);

    void addReply(Long parentCommentId, CommentRequestDTO replyDTO, UUID uuid);
}
