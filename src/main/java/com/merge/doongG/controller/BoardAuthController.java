package com.merge.doongG.controller;

import com.merge.doongG.dto.CommentRequestDTO;
import com.merge.doongG.dto.PostDTO;
import com.merge.doongG.dto.ReactionDTO;
import com.merge.doongG.service.BoardService;
import com.merge.doongG.service.CommentService;
import com.merge.doongG.service.ReactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "인증 필요 게시판 API", description = "로그인 및 토큰이 필요한 게시판 API")
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

    @Operation(
            summary = "게시물 작성",
            description = "폼에서 받은 게시물 데이터를 DB에 삽입합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "게시물 작성 성공", content = @Content(schema = @Schema(implementation = PostDTO.class)))
            }
    )
    @PostMapping("/createPost")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        UUID userId = getLoggedInUserId();
        PostDTO createdPost = boardService.createPost(postDTO, userId);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @Operation(
            summary = "게시물 수정",
            description = "제공된 정보로 게시물 데이터를 업데이트합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 수정 성공", content = @Content(schema = @Schema(implementation = PostDTO.class)))
            }
    )
    @PostMapping("/updatePost/{postId}")
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable Long postId,
            @RequestBody PostDTO postDTO) {
        UUID userId = getLoggedInUserId();
        PostDTO updatedPost = boardService.updatePost(postId, postDTO, userId);
        return ResponseEntity.ok(updatedPost);
    }

    @Operation(
            summary = "게시물 삭제",
            description = "지정된 ID의 게시물을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "게시물 삭제 성공")
            }
    )
    @PostMapping("/deletePost/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        UUID userId = getLoggedInUserId();
        boardService.deletePost(postId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "좋아요",
            description = "요청된 게시물에 좋아요를 표시합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "좋아요 성공", content = @Content(schema = @Schema(implementation = ReactionDTO.class)))
            }
    )
    @PostMapping("/like")
    public ResponseEntity<ReactionDTO> likePost(@RequestBody ReactionDTO request) {
        UUID userId = getLoggedInUserId();
        ReactionDTO reactionDTO = reactionService.likePost(request.getPostId(), userId);
        return new ResponseEntity<>(reactionDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "싫어요",
            description = "요청된 게시물에 싫어요를 표시합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "싫어요 성공", content = @Content(schema = @Schema(implementation = ReactionDTO.class)))
            }
    )
    @PostMapping("/dislike")
    public ResponseEntity<ReactionDTO> dislikePost(@RequestBody ReactionDTO request) {
        UUID userId = getLoggedInUserId();
        ReactionDTO reactionDTO = reactionService.dislikePost(request.getPostId(), userId);
        return new ResponseEntity<>(reactionDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "리액션 조회",
            description = "게시물 ID를 사용하여 게시물의 리액션(좋아요, 싫어요)을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "반응 조회 성공", content = @Content(schema = @Schema(implementation = ReactionDTO.class)))
            }
    )
    @GetMapping("/getReaction")
    public ReactionDTO getReactionsByPostId(
            @Parameter(description = "리액션을 조회할 게시물의 ID", required = true)
            @RequestParam Long postId) {
        UUID userId = getLoggedInUserId();
        return reactionService.getReactionsByPostId(postId, userId);
    }

    @Operation(
            summary = "댓글 작성",
            description = "지정된 게시물에 댓글을 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 작성 성공", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/createComment/{postId}")
    public ResponseEntity<String> addComment(@PathVariable Long postId, @RequestBody CommentRequestDTO commentDTO) {
        UUID userId = getLoggedInUserId();
        commentService.addComment(postId, commentDTO, userId);
        return ResponseEntity.ok("댓글이 성공적으로 추가되었습니다.");
    }

    @Operation(
            summary = "댓글 수정",
            description = "지정된 댓글의 내용을 업데이트합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/updateComment/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDTO commentDTO) {
        UUID userId = getLoggedInUserId();
        commentService.updateComment(commentId, commentDTO, userId);
        return ResponseEntity.ok("댓글이 성공적으로 업데이트되었습니다.");
    }

    @Operation(
            summary = "댓글 삭제",
            description = "지정된 댓글을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 삭제 성공", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/deleteComment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        UUID userId = getLoggedInUserId();
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
    }

    @Operation(
            summary = "대댓글 추가",
            description = "지정된 부모 댓글에 답글을 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "대댓글 추가 성공", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/replies/{parentCommentId}")
    public ResponseEntity<String> addReply(@PathVariable Long parentCommentId, @RequestBody CommentRequestDTO replyDTO) {
        UUID userId = getLoggedInUserId();
        commentService.addReply(parentCommentId, replyDTO, userId);
        return ResponseEntity.ok("대댓글이 성공적으로 추가되었습니다.");
    }
}
