package com.merge.doongG.service;

import com.merge.doongG.domain.Comment;
import com.merge.doongG.domain.Post;
import com.merge.doongG.domain.User;
import com.merge.doongG.dto.CommentRequestDTO;
import com.merge.doongG.dto.UserSummaryDTO;
import com.merge.doongG.repository.CommentRepository;
import com.merge.doongG.repository.PostRepository;
import com.merge.doongG.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.sql.Timestamp;
import java.util.Optional;

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
    public void addComment(Long postId, CommentRequestDTO commentDTO) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            UserSummaryDTO currentUser = getCurrentUserById(commentDTO.getCommenterId());
            User commenter = convertUserSummaryToUser(currentUser);

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
            throw new NotFoundException("Post not found");
        }
    }

    // 댓글 수정
    @Override
    @Transactional
    public void updateComment(Long commentId, CommentRequestDTO commentDTO) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));User existingUser = existingComment.getCommenter();

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
    }

    // 댓글 삭제
    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        Post post = comment.getPost();
        post.decrementCommentCount();
        commentRepository.deleteById(commentId);
    }

    // 대댓글 작성
    @Override
    @Transactional
    public void addReply(Long parentCommentId, CommentRequestDTO replyDTO) {
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));

        UserSummaryDTO currentUser = getCurrentUserById(replyDTO.getCommenterId());

        User commenter = convertUserSummaryToUser(currentUser);

        Comment reply = Comment.builder()
                .content(replyDTO.getContent())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .post(parentComment.getPost())  // Set the post property
                .commenter(commenter)
                .parentComment(parentComment)
                .build();

        parentComment.getPost().incrementCommentCount();
        commentRepository.save(reply);
    }

    private User convertUserSummaryToUser(UserSummaryDTO currentUser) {
        return User.builder()
                .id(currentUser.getId())
                .nickname(currentUser.getNickname())
                .profileImg(currentUser.getProfileImg())
                .build();
    }

    private UserSummaryDTO getCurrentUserById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            return new UserSummaryDTO(user.getId(), user.getNickname(), user.getProfileImg());
        } else {
            return null;
        }
    }
}
