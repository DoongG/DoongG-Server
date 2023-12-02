package com.merge.doongG.controller;

import com.merge.doongG.dto.CommentRequestDTO;
import com.merge.doongG.dto.PostDTO;
import com.merge.doongG.dto.ReactionDTO;
import com.merge.doongG.service.BoardService;
import com.merge.doongG.service.CommentService;
import com.merge.doongG.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/boardsAuth")
public class BoardAuthController {
    private final BoardService boardService;
    private final ReactionService reactionService;
    private final CommentService commentService;

    @Autowired
    public BoardAuthController(BoardService boardService, ReactionService reactionService, CommentService commentService) {
        this.boardService = boardService;
        this.reactionService = reactionService;
        this.commentService = commentService;
    }

    // 유저 UUID 확인
    private UUID getLoggedInUserId() {
        String userUuid = (String) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return UUID.fromString(userUuid);
    }

    // 게시물 작성
    @PostMapping("/createPost")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        UUID userId = getLoggedInUserId();
        PostDTO createdPost = boardService.createPost(postDTO, userId);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    // 게시물 수정
    @PostMapping("/updatePost/{postId}")
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable Long postId,
            @RequestBody PostDTO postDTO) {
        UUID userId = getLoggedInUserId();
        PostDTO updatedPost = boardService.updatePost(postId, postDTO, userId);
        return ResponseEntity.ok(updatedPost);
    }

    // 게시물 삭제
    @PostMapping("/deletePost/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        UUID userId = getLoggedInUserId();
        boardService.deletePost(postId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 좋아요
    @PostMapping("/like")
    public ResponseEntity<ReactionDTO> likePost(@RequestBody ReactionDTO request) {
        UUID userId = getLoggedInUserId();
        ReactionDTO reactionDTO = reactionService.likePost(request.getPostId(), userId);
        return new ResponseEntity<>(reactionDTO, HttpStatus.OK);
    }

    // 싫어요
    @PostMapping("/dislike")
    public ResponseEntity<ReactionDTO> dislikePost(@RequestBody ReactionDTO request) {
        UUID userId = getLoggedInUserId();
        ReactionDTO reactionDTO = reactionService.dislikePost(request.getPostId(), userId);
        return new ResponseEntity<>(reactionDTO, HttpStatus.OK);
    }

    // 리액션 받아오기
    @GetMapping("/getReaction")
    public ReactionDTO getReactionsByPostId(@RequestParam Long postId) {
        UUID userId = getLoggedInUserId();
        return reactionService.getReactionsByPostId(postId, userId);
    }

    // 댓글 작성
    @PostMapping("/createComment/{postId}")
    public ResponseEntity<String> addComment(@PathVariable Long postId, @RequestBody CommentRequestDTO commentDTO) {
        UUID userId = getLoggedInUserId();
        commentService.addComment(postId, commentDTO, userId);
        return ResponseEntity.ok("Comment added successfully");
    }

    // 댓글 수정
    @PostMapping("/updateComment/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDTO commentDTO) {
        UUID userId = getLoggedInUserId();
        commentService.updateComment(commentId, commentDTO, userId);
        return ResponseEntity.ok("Comment updated successfully");
    }

    // 댓글 삭제
    @PostMapping("/deleteComment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        UUID userId = getLoggedInUserId();
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    // 대댓글
    @PostMapping("/replies/{parentCommentId}")
    public ResponseEntity<String> addReply(@PathVariable Long parentCommentId, @RequestBody CommentRequestDTO replyDTO) {
        UUID userId = getLoggedInUserId();
        commentService.addReply(parentCommentId, replyDTO, userId);
        return ResponseEntity.ok("Reply added successfully");
    }
}
