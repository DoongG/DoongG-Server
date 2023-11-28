package com.merge.doongG.service;

import com.merge.doongG.dto.BoardDTO;
import com.merge.doongG.dto.PostDTO;
import com.merge.doongG.dto.UnifiedBoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoardService {
    List<BoardDTO> getAllBoards();

    BoardDTO createBoard(String boardName);
    BoardDTO updateBoard(Long boardId, String newBoardName);
    void deleteBoard(Long boardId);

    Page<PostDTO> getBoard(String boardName, String order, int pageSize, int page);

    Page<PostDTO> searchPosts(String boardName, String keyword, String order, int pageSize, int page, String searchType);


    PostDTO getPost(Long postId);
    PostDTO createPost(PostDTO postDTO);
    PostDTO updatePost(Long postId, PostDTO postDTO);
    void deletePost(Long postId);

    List<UnifiedBoardDTO> getUnifiedBoards();

    String getBoardDefaultType(String boardName);

    // 전체 게시물 수 가져오기
    long getTotalPosts(String boardname);

    @Transactional
    void incrementPostViews(Long postId);

    // 지난 주 좋아요 Top 10
    List<PostDTO> getTopLikedPosts(String boardName);
}