package com.merge.doongG.service;

import com.merge.doongG.dto.CommentRequestDTO;

public interface CommentService {
    void addComment(Long postId, CommentRequestDTO commentDTO);

    void updateComment(Long commentId, CommentRequestDTO commentDTO);

    void deleteComment(Long commentId);

    void addReply(Long parentCommentId, CommentRequestDTO replyDTO);
}
