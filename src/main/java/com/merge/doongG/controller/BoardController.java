package com.merge.doongG.controller;

import com.merge.doongG.dto.*;
import com.merge.doongG.service.BoardService;
import com.merge.doongG.service.ReactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;
    private final ReactionService reactionService;

    @Autowired
    public BoardController(BoardService boardService, ReactionService reactionService) {
        this.boardService = boardService;
        this.reactionService = reactionService;
    }

    // 통합 게시판
    @GetMapping
    public ResponseEntity<List<UnifiedBoardDTO>> getUnifiedBoards() {
        List<UnifiedBoardDTO> unifiedBoards = boardService.getUnifiedBoards();
        return ResponseEntity.ok(unifiedBoards);
    }

    // 게시판 렌더링
    @GetMapping("/{boardName}")
    public ResponseEntity<BoardResponseDTO> getBoard(
            @PathVariable String boardName,
            @RequestParam(defaultValue = "latest") String order,
            @RequestParam(defaultValue = "12") int pageSize,
            @RequestParam(defaultValue = "1") int page) {
        Page<PostDTO> posts = boardService.getBoard(boardName, order, pageSize, page);

        String boardType = boardService.getBoardDefaultType(boardName);

        BoardResponseDTO responseDTO = BoardResponseDTO.builder()
                .boardName(boardName)
                .boardDefaultType(boardType)
                .posts(posts.getContent())
                .build();

        return ResponseEntity.ok(responseDTO);
    }

    // 게시판 (갤러리 유형)
//    @GetMapping("/gallery/{boardId}")
//    public ResponseEntity<Page<PostDTO>> getGalleryBoard(
//            @PathVariable Long boardId,
//            @RequestParam(defaultValue = "latest") String order,
//            @RequestParam(defaultValue = "12") int pageSize,
//            @RequestParam(defaultValue = "1") int page) {
//        Page<PostDTO> posts = boardService.getBoard(boardId, order, pageSize, page);
//        return ResponseEntity.ok(posts);
//    }

    // 게시판 검색 (갤러리 유형)
    @GetMapping("/gallery/{boardId}/search")
    public ResponseEntity<Page<PostDTO>> searchGalleryBoard(
            @PathVariable Long boardId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "latest") String order,
            @RequestParam(defaultValue = "title") String category,
            @RequestParam(defaultValue = "12") int pageSize,
            @RequestParam(defaultValue = "1") int page) {
        Page<PostDTO> posts = boardService.searchBoard(boardId, keyword, order, category, page, pageSize);
        return ResponseEntity.ok(posts);
    }

    // 게시판 (리스트 유형)
//    @GetMapping("/list/{boardId}")
//    public ResponseEntity<Page<PostDTO>> getListBoard(
//            @PathVariable Long boardId,
//            @RequestParam(defaultValue = "latest") String order,
//            @RequestParam(defaultValue = "16") int pageSize,
//            @RequestParam(defaultValue = "1") int page) {
//        Page<PostDTO> posts = boardService.getBoard(boardId, order, pageSize, page);
//        long totalPosts = boardService.getTotalPosts();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Posts-Total-Count", String.valueOf(totalPosts));
//
//        return new ResponseEntity<>(posts, headers, HttpStatus.OK);
//    }

    // 게시판 검색 (리스트 유형)
    @GetMapping("/list/{boardId}/search")
    public ResponseEntity<Page<PostDTO>> searchListBoard(
            @PathVariable Long boardId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "latest") String order,
            @RequestParam(defaultValue = "title") String category,
            @RequestParam(defaultValue = "16") int pageSize,
            @RequestParam(defaultValue = "1") int page) {
        Page<PostDTO> posts = boardService.searchBoard(boardId, keyword, order, category, page, pageSize);
        return ResponseEntity.ok(posts);
    }

    // 게시물 상세 페이지
    @GetMapping("posts/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long postId) {
        PostDTO post = boardService.getPost(postId);
        boardService.incrementPostViews(postId);
        return ResponseEntity.ok(post);
    }

    // 리액션 받아오기
    @GetMapping("/getReaction")
    public ReactionDTO getReactionsByPostId(@RequestParam Long postId, @RequestParam Long userId) {
        return reactionService.getReactionsByPostId(postId, userId);
    }
}