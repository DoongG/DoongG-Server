package com.merge.doongG.service;

import com.merge.doongG.domain.Comment;
import com.merge.doongG.domain.Post;
import com.merge.doongG.domain.User;
import com.merge.doongG.dto.CommentRequestDTO;
import com.merge.doongG.repository.CommentRepository;
import com.merge.doongG.repository.PostRepository;
import com.merge.doongG.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,
                              UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 댓글 추가
    @Override
    @Transactional
    public void addComment(Long postId, CommentRequestDTO commentDTO, UUID uuid) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> userOptional = userRepository.findByUuid(uuid);

        if (postOptional.isPresent() && userOptional.isPresent()) {
            Post post = postOptional.get();
            User commenter = userOptional.get();

            Comment comment = Comment.builder()
                    .content(commentDTO.getContent())
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .post(post)
                    .commenter(commenter)
                    .build();

            commentRepository.save(comment);

            post.incrementCommentCount();
            postRepository.save(post);
        } else {
            throw new NotFoundException("Post or User not found");
        }
    }

    // 댓글 수정
    @Override
    @Transactional
    public void updateComment(Long commentId, CommentRequestDTO commentDTO, UUID uuid) {
        Optional<Comment> existingCommentOptional = commentRepository.findById(commentId);
        Optional<User> existingUserOptional = userRepository.findByUuid(uuid);

        if (existingCommentOptional.isPresent() && existingUserOptional.isPresent()) {
            Comment existingComment = existingCommentOptional.get();
            User existingUser = existingUserOptional.get();

            if (existingComment.getCommenter().equals(existingUser)) {
                Comment updatedComment = Comment.builder()
                        .commentId(existingComment.getCommentId())
                        .content(commentDTO.getContent())
                        .createdAt(existingComment.getCreatedAt())
                        .updatedAt(new Timestamp(System.currentTimeMillis()))
                        .post(existingComment.getPost())
                        .commenter(existingUser)
                        .parentComment(existingComment.getParentComment())
                        .build();

                commentRepository.save(updatedComment);
            } else {
                throw new AccessDeniedException("이 댓글을 수정할 권한이 없습니다");
            }
        } else {
            throw new NotFoundException("Comment or User not found");
        }
    }

    // 댓글 삭제
    @Override
    @Transactional
    public void deleteComment(Long commentId, UUID uuid) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (comment.getCommenter().getUuid().equals(uuid)) {
            Post post = comment.getPost();
            post.decrementCommentCount();
            commentRepository.deleteById(commentId);
        } else {
            throw new AccessDeniedException("이 댓글을 삭제할 권한이 없습니다");
        }
    }
    // 대댓글 작성
    @Override
    @Transactional
    public void addReply(Long parentCommentId, CommentRequestDTO replyDTO, UUID uuid) {
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));

        Optional<User> userOptional = userRepository.findByUuid(uuid);

        if (userOptional.isPresent()) {
            User commenter = userOptional.get();

            Comment reply = Comment.builder()
                    .content(replyDTO.getContent())
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .post(parentComment.getPost())
                    .commenter(commenter)
                    .parentComment(parentComment)
                    .build();

            parentComment.getPost().incrementCommentCount();
            commentRepository.save(reply);
        } else {
            throw new NotFoundException("User not found");
        }
    }
}
