package com.merge.doongG.controller;

import com.merge.doongG.dto.*;
import com.merge.doongG.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
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
                .postCount(posts.getTotalElements())
                .posts(posts.getContent())
                .build();

        return ResponseEntity.ok(responseDTO);
    }

    // 게시물 검색
    @GetMapping("/search/{boardName}")
    public ResponseEntity<BoardResponseDTO> searchPosts(
            @PathVariable String boardName,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "full") String searchType,
            @RequestParam(defaultValue = "latest") String order,
            @RequestParam(defaultValue = "12") int pageSize,
            @RequestParam(defaultValue = "1") int page) {
        Page<PostDTO> searchedPosts = boardService.searchPosts(boardName, keyword, order, pageSize, page, searchType);

        String boardType = boardService.getBoardDefaultType(boardName);

        BoardResponseDTO responseDTO = BoardResponseDTO.builder()
                .boardName(boardName)
                .boardDefaultType(boardType)
                .posts(searchedPosts.getContent())
                .build();

        return ResponseEntity.ok(responseDTO);
    }

    // 게시물 상세 페이지
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long postId) {
        PostDTO post = boardService.getPost(postId);
        return ResponseEntity.ok(post);
    }

    // 조회 수 증가
    @PostMapping("/posts/increaseViews/{postId}")
    public ResponseEntity<Void> increasePostViews(@PathVariable Long postId) {
        boardService.incrementPostViews(postId);
        return ResponseEntity.ok().build();
    }

    // carousel을 위한 좋아요 수 탑10 게시물 받아오기
    @GetMapping("/topLiked/{boardName}")
    public ResponseEntity<List<PostDTO>> getTopLikedPosts(@PathVariable String boardName) {
        List<PostDTO> topLikedPosts = boardService.getTopLikedPosts(boardName);
        return new ResponseEntity<>(topLikedPosts, HttpStatus.OK);
    }
}