package com.merge.doongG.service;

import com.merge.doongG.dto.BoardDTO;
import com.merge.doongG.dto.PostDTO;
import com.merge.doongG.dto.UnifiedBoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface BoardService {
    // 게시판 CRUD (실 사용 x)
    List<BoardDTO> getAllBoards();
    BoardDTO createBoard(String boardName);
    BoardDTO updateBoard(Long boardId, String newBoardName);
    void deleteBoard(Long boardId);

    // 게시판 조회
    Page<PostDTO> getBoard(String boardName, String order, int pageSize, int page);

    // 게시물 검색
    Page<PostDTO> searchPosts(String boardName, String keyword, String order, int pageSize, int page, String searchType);

    // 게시물 CRUD
    PostDTO getPost(Long postId);
    PostDTO createPost(PostDTO postDTO, UUID uuid);
    PostDTO updatePost(Long postId, PostDTO postDTO, UUID uuid);
    void deletePost(Long postId, UUID uuid);

    // 통합 게시판 조회
    List<UnifiedBoardDTO> getUnifiedBoards();

    // 게시판 디폴트 유형 조회
    String getBoardDefaultType(String boardName);

    // 전체 게시물 수 조회
    long getTotalPosts(String boardname);

    // 게시물 조회 수 증가
    @Transactional
    void incrementPostViews(Long postId);

    // 지난 주 좋아요 Top 10 조회
    List<PostDTO> getTopLikedPosts(String boardName);

    // 내가 작성한 글 조회
    List<PostDTO> getMyPosts(UUID uuid);

    // 내가 좋아요 표시한 글 조회
    List<PostDTO> getMyLikedPosts(UUID uuid);
}