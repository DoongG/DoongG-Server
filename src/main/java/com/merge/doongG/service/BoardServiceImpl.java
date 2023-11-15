package com.merge.doongG.service;

import com.merge.doongG.dto.BoardDTO;
import com.merge.doongG.dto.PostDTO;
import com.merge.doongG.repository.BoardRepository;
import com.merge.doongG.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final ModelMapper mapper;
    private final PostRepository postRepository;

    public BoardServiceImpl(BoardRepository boardRepository, ModelMapper mapper,
                            PostRepository postRepository) {
        this.boardRepository = boardRepository;
        this.mapper = mapper;
        this.postRepository = postRepository;
    }

    @Override
    public List<PostDTO> getGalleryBoard(String order, int pageSize, int page) {
        return Collections.emptyList();
    }

    @Override
    public List<PostDTO> searchGalleryBoard(String keyword, String order, int page, int pageSize) {
        return Collections.emptyList();
    }

    @Override
    public List<PostDTO> getListBoard(int pageSize, int page) {
        return Collections.emptyList();
    }

    @Override
    public List<PostDTO> searchListBoard(String keyword, int pageSize, int page) {
        return Collections.emptyList();
    }

    @Override
    public PostDTO getPost(Long postId) {
        return null;
    }

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        return null;
    }

    @Override
    public PostDTO updatePost(Long postId, PostDTO postDTO) {
        return null;
    }

    @Override
    public void deletePost(Long postId) {

    }

    @Override
    public long getTotalPosts() {
        return postRepository.count();
    }
}
