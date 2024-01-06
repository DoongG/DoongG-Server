package com.merge.doongG.controller;

import com.merge.doongG.dto.*;
import com.merge.doongG.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "인증 불필요 게시판 API", description = "게시판과 게시물 관리를 위한 API")
@RestController
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @Operation(
            summary = "통합 게시판 조회",
            description = "모든 통합 게시판을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "통합 게시판 조회 성공", content = @Content(schema = @Schema(implementation = List.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<UnifiedBoardDTO>> getUnifiedBoards() {
        List<UnifiedBoardDTO> unifiedBoards = boardService.getUnifiedBoards();
        return ResponseEntity.ok(unifiedBoards);
    }

    @Operation(
            summary = "게시판 렌더링",
            description = "지정된 게시판의 게시물을 조회합니다.",
            parameters = {
                    @Parameter(name = "boardName", description = "게시판 이름", example = "자유게시판", required = true),
                    @Parameter(name = "order", description = "게시물 정렬 순서", example = "latest"),
                    @Parameter(name = "pageSize", description = "페이지 크기", example = "12"),
                    @Parameter(name = "page", description = "페이지 번호", example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시판 렌더링 성공", content = @Content(schema = @Schema(implementation = BoardResponseDTO.class)))
            }
    )
    @GetMapping("/{boardName}")
    public ResponseEntity<BoardResponseDTO> getBoard(
            @PathVariable String boardName,
            @RequestParam(defaultValue = "latest") String order,
            @RequestParam(defaultValue = "12") int pageSize,
            @RequestParam(defaultValue = "1") int page) {
        Page<PostDTO> posts = boardService.getBoard(boardName, order, pageSize, page);

        String boardType = boardService.getBoardDefaultType(boardName);
        Long boardId = boardService.getBoardIdByName(boardName);

        BoardResponseDTO responseDTO = BoardResponseDTO.builder()
                .boardId(boardId)
                .boardName(boardName)
                .boardDefaultType(boardType)
                .postCount(posts.getTotalElements())
                .posts(posts.getContent())
                .build();

        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
            summary = "게시물 검색",
            description = "게시판에서 키워드로 게시물을 검색합니다.",
            parameters = {
                    @Parameter(name = "boardName", description = "게시판 이름", example = "레시피게시판", required = true),
                    @Parameter(name = "keyword", description = "검색 키워드", example = "짜장", required = true),
                    @Parameter(name = "searchType", description = "검색 유형", example = "full"),
                    @Parameter(name = "order", description = "게시물 정렬 순서", example = "latest"),
                    @Parameter(name = "pageSize", description = "페이지 크기", example = "12"),
                    @Parameter(name = "page", description = "페이지 번호", example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 검색 성공", content = @Content(schema = @Schema(implementation = Page.class)))
            }
    )
    @GetMapping("/search/{boardName}")
    public ResponseEntity<Page<PostDTO>> searchPosts(
            @PathVariable String boardName,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "full") String searchType,
            @RequestParam(defaultValue = "latest") String order,
            @RequestParam(defaultValue = "12") int pageSize,
            @RequestParam(defaultValue = "1") int page) {
        Page<PostDTO> searchedPosts = boardService.searchPosts(boardName, keyword, order, pageSize, page, searchType);

        return ResponseEntity.ok(searchedPosts);
    }

    @Operation(
            summary = "해시태그 검색",
            description = "게시판에서 해시태그로 게시물을 검색합니다.",
            parameters = {
                    @Parameter(name = "boardName", description = "게시판 이름", example = "맛집게시판", required = true),
                    @Parameter(name = "hashtags", description = "해시태그 리스트", example = "[마라, 중식]", required = true),
                    @Parameter(name = "order", description = "게시물 정렬 순서", example = "latest"),
                    @Parameter(name = "pageSize", description = "페이지 크기", example = "10"),
                    @Parameter(name = "page", description = "페이지 번호", example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "해시태그 검색 성공", content = @Content(schema = @Schema(implementation = Page.class)))
            }
    )
    @GetMapping("/hashtagSearch/{boardName}")
    public ResponseEntity<Page<PostDTO>> hashtagSearch(
            @PathVariable String boardName,
            @RequestParam List<String> hashtags,
            @RequestParam(defaultValue = "latest") String order,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "1") int page) {
        Page<PostDTO> posts = boardService.searchPostsByHashtags(boardName, hashtags, order, pageSize, page);
        return ResponseEntity.ok(posts);
    }

    @Operation(
            summary = "게시물 상세 페이지",
            description = "게시물의 상세 정보를 조회합니다.",
            parameters = {
                    @Parameter(name = "postId", description = "게시물 ID", example = "1", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 조회 성공", content = @Content(schema = @Schema(implementation = PostDTO.class)))
            }
    )
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long postId) {
        PostDTO post = boardService.getPost(postId);
        return ResponseEntity.ok(post);
    }

    @Operation(
            summary = "조회 수 증가",
            description = "게시물의 조회 수를 증가시킵니다.",
            parameters = {
                    @Parameter(name = "postId", description = "게시물 ID", example = "1", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 수 증가 성공")
            }
    )
    @PostMapping("/posts/increaseViews/{postId}")
    public ResponseEntity<Void> increasePostViews(@PathVariable Long postId) {
        boardService.incrementPostViews(postId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Carousel을 위한 좋아요 수 탑10 게시물 조회",
            description = "지정된 게시판에서 Carousel을 위한 좋아요 수 탑10 게시물을 조회합니다.",
            parameters = {
                    @Parameter(name = "boardName", description = "게시판 이름", example = "게임게시판", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Carousel을 위한 탑10 좋아요 수 게시물 조회 성공", content = @Content(schema = @Schema(implementation = List.class)))
            }
    )
    @GetMapping("/topLiked/{boardName}")
    public ResponseEntity<List<PostDTO>> getTopLikedPosts(@PathVariable String boardName) {
        List<PostDTO> topLikedPosts = boardService.getTopLikedPostsFromLastWeek(boardName);
        return new ResponseEntity<>(topLikedPosts, HttpStatus.OK);
    }
}