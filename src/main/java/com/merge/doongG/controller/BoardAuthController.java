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
import org.springframework.web.bind.annotation.*;

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

    // 게시물 작성
    @PostMapping("/createPost")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        PostDTO createdPost = boardService.createPost(postDTO);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    // 게시물 수정
    @PostMapping("/updatePost/{postId}")
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable Long postId,
            @RequestBody PostDTO postDTO) {
        PostDTO updatedPost = boardService.updatePost(postId, postDTO);
        return ResponseEntity.ok(updatedPost);
    }

    // 게시물 삭제
    @PostMapping("/deletePost/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        boardService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 좋아요
    @PostMapping("/like")
    public ResponseEntity<ReactionDTO> likePost(@RequestBody ReactionDTO request) {
        ReactionDTO reactionDTO = reactionService.likePost(request.getPostId(), request.getUserId());
        return new ResponseEntity<>(reactionDTO, HttpStatus.OK);
    }

    // 싫어요
    @PostMapping("/dislike")
    public ResponseEntity<ReactionDTO> dislikePost(@RequestBody ReactionDTO request) {
        ReactionDTO reactionDTO = reactionService.dislikePost(request.getPostId(), request.getUserId());
        return new ResponseEntity<>(reactionDTO, HttpStatus.OK);
    }

    // 댓글 작성
    @PostMapping("/createComment/{postId}")
    public ResponseEntity<String> addComment(@PathVariable Long postId, @RequestBody CommentRequestDTO commentDTO) {
        commentService.addComment(postId, commentDTO);
        return ResponseEntity.ok("Comment added successfully");
    }

    // 댓글 수정
    @PostMapping("/updateComment/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDTO commentDTO) {
        commentService.updateComment(commentId, commentDTO);
        return ResponseEntity.ok("Comment updated successfully");
    }

    // 댓글 삭제
    @PostMapping("/deleteComment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    // 대댓글
    @PostMapping("/replies/{parentCommentId}")
    public ResponseEntity<String> addReply(@PathVariable Long parentCommentId, @RequestBody CommentRequestDTO replyDTO) {
        commentService.addReply(parentCommentId, replyDTO);
        return ResponseEntity.ok("Reply added successfully");
    }
}
