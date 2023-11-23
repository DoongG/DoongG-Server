package com.merge.doongG.controller;

import com.merge.doongG.dto.PostDTO;
import com.merge.doongG.dto.UnifiedBoardDTO;
import com.merge.doongG.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/boards")
public class BoardController {
    @Autowired
    private BoardService boardService;

    // 통합 게시판
    @GetMapping
    public ResponseEntity<List<UnifiedBoardDTO>> getUnifiedBoards() {
        List<UnifiedBoardDTO> unifiedBoards = boardService.getUnifiedBoards();
        return ResponseEntity.ok(unifiedBoards);
    }

    // 게시판 (갤러리 유형)
    @GetMapping("/gallery/{boardId}")
    public ResponseEntity<Page<PostDTO>> getGalleryBoard(
            @PathVariable Long boardId,
            @RequestParam(defaultValue = "latest") String order,
            @RequestParam(defaultValue = "12") int pageSize,
            @RequestParam(defaultValue = "1") int page) {
        Page<PostDTO> posts = boardService.getBoard(boardId, order, pageSize, page);
        return ResponseEntity.ok(posts);
    }

    // 게시판 검색 (갤러리 유형)
    @GetMapping("/gallery/{boardId}/search")
    public ResponseEntity<Page<PostDTO>> searchGalleryBoard(
            @PathVariable Long boardId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "latest") String order,
            @RequestParam(defaultValue = "title") String category,
            @RequestParam(defaultValue = "12") int pageSize,
            @RequestParam(defaultValue = "1") int page
    ) {
        Page<PostDTO> posts = boardService.searchBoard(boardId, keyword, order, category, page, pageSize);
        return ResponseEntity.ok(posts);
    }

    // 게시판 (리스트 유형)
    @GetMapping("/list/{boardId}")
    public ResponseEntity<Page<PostDTO>> getListBoard(
            @PathVariable Long boardId,
            @RequestParam(defaultValue = "latest") String order,
            @RequestParam(defaultValue = "16") int pageSize,
            @RequestParam(defaultValue = "1") int page
    ) {
        Page<PostDTO> posts = boardService.getBoard(boardId, order, pageSize, page);
        long totalPosts = boardService.getTotalPosts();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Posts-Total-Count", String.valueOf(totalPosts));

        return new ResponseEntity<>(posts, headers, HttpStatus.OK);
    }

    // 게시판 검색 (리스트 유형)
    @GetMapping("/list/{boardId}/search")
    public ResponseEntity<Page<PostDTO>> searchListBoard(
            @PathVariable Long boardId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "latest") String order,
            @RequestParam(defaultValue = "title") String category,
            @RequestParam(defaultValue = "16") int pageSize,
            @RequestParam(defaultValue = "1") int page
    ) {
        Page<PostDTO> posts = boardService.searchBoard(boardId, keyword, order, category, page, pageSize);
        return ResponseEntity.ok(posts);
    }

    // 게시물 하나 가져오기
    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long postId) {
        PostDTO post = boardService.getPost(postId);
        return ResponseEntity.ok(post);
    }

    // 게시물 작성
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        PostDTO createdPost = boardService.createPost(postDTO);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    // 게시물 수정
    @PostMapping("/update/{postId}")
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable Long postId,
            @RequestBody PostDTO postDTO
    ) {
        PostDTO updatedPost = boardService.updatePost(postId, postDTO);
        log.info("Received PostDTO: {}", postDTO);
        return ResponseEntity.ok(updatedPost);
    }

    // 게시물 삭제
    @PostMapping("/delete/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        boardService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}