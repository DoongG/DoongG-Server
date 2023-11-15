package com.merge.doongG.service;

import com.merge.doongG.dto.PostDTO;

import java.util.List;

public interface BoardService {

    List<PostDTO> getGalleryBoard(String order, int pageSize, int page);
    List<PostDTO> searchGalleryBoard(String keyword, String order, int page, int pageSize);

    List<PostDTO> getListBoard(int pageSize, int page);
    List<PostDTO> searchListBoard(String keyword, int pageSize, int page);

    PostDTO getPost(Long postId);
    PostDTO createPost(PostDTO postDTO);
    PostDTO updatePost(Long postId, PostDTO postDTO);
    void deletePost(Long postId);

    long getTotalPosts();
}