package com.merge.doongG.service;

import com.merge.doongG.dto.BoardDTO;
import com.merge.doongG.dto.PostDTO;
import com.merge.doongG.dto.UnifiedBoardDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BoardService {
    List<BoardDTO> getAllBoards();

    BoardDTO createBoard(String boardName);
    BoardDTO updateBoard(Long boardId, String newBoardName);
    void deleteBoard(Long boardId);

    Page<PostDTO> getBoard(Long boardId, String order, int pageSize, int page);
    Page<PostDTO> searchBoard(Long boardId, String keyword, String order, String category, int page, int pageSize);

    PostDTO getPost(Long postId);
    PostDTO createPost(PostDTO postDTO);
    PostDTO updatePost(Long postId, PostDTO postDTO);
    void deletePost(Long postId);

    long getTotalPosts();

    List<UnifiedBoardDTO> getUnifiedBoards();
}