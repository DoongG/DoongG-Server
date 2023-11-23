package com.merge.doongG.repository;

import com.merge.doongG.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByBoardIdOrderByCreatedAtDesc(Long boardId, Pageable pageable);

    Page<Post> findByBoardIdOrderByViewsDesc(Long boardId, Pageable pageable);

    Page<Post> findByBoardIdAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(Long boardId, String keyword, Pageable pageable);

    Page<Post> findByBoardIdAndUserNicknameContainingIgnoreCaseOrderByCreatedAtDesc(Long boardId, String keyword, Pageable pageable);

    Page<Post> findByBoardIdAndHashtagsContainingIgnoreCaseOrderByCreatedAtDesc(Long boardId, String keyword, Pageable pageable);

    Page<Post> findByBoardIdAndContentContainingIgnoreCaseOrderByCreatedAtDesc(Long boardId, String keyword, Pageable pageable);

    Page<Post> findByBoardIdAndTitleContainingIgnoreCaseOrderByViewsDesc(Long boardId, String keyword, Pageable pageable);

    Page<Post> findByBoardIdAndUserNicknameContainingIgnoreCaseOrderByViewsDesc(Long boardId, String keyword, Pageable pageable);

    Page<Post> findByBoardIdAndHashtagsContainingIgnoreCaseOrderByViewsDesc(Long boardId, String keyword, Pageable pageable);

    Page<Post> findByBoardIdAndContentContainingIgnoreCaseOrderByViewsDesc(Long boardId, String keyword, Pageable pageable);

    Page<Post> findByBoardId(Long boardId, Pageable pageable);
}
