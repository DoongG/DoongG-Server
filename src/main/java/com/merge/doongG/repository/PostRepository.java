package com.merge.doongG.repository;

import com.merge.doongG.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    long countByBoardBoardName(String boardName);

    Page<Post> findByBoardBoardNameOrderByCreatedAtDesc(String boardName, Pageable pageable);

    Page<Post> findByBoardBoardNameOrderByViewsDesc(String boardName, Pageable pageable);
    
    Page<Post> findByBoardId(Long boardId, Pageable pageable);

    Page<Post> findByBoardBoardNameAndFullTextSearchContainingIgnoreCaseOrderByCreatedAtDesc(String boardName, String keyword, Pageable pageable);

    Page<Post> findByBoardBoardNameAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(String boardName, String keyword, Pageable pageable);

    Page<Post> findByBoardBoardNameAndUserNicknameContainingIgnoreCaseOrderByCreatedAtDesc(String boardName, String keyword, Pageable pageable);

    Page<Post> findByBoardBoardNameAndContentContainingIgnoreCaseOrderByCreatedAtDesc(String boardName, String keyword, Pageable pageable);

    Page<Post> findByBoardBoardNameAndFullTextSearchContainingIgnoreCaseOrderByViewsDesc(String boardName, String keyword, Pageable pageable);

    Page<Post> findByBoardBoardNameAndTitleContainingIgnoreCaseOrderByViewsDesc(String boardName, String keyword, Pageable pageable);

    Page<Post> findByBoardBoardNameAndUserNicknameContainingIgnoreCaseOrderByViewsDesc(String boardName, String keyword, Pageable pageable);

    Page<Post> findByBoardBoardNameAndContentContainingIgnoreCaseOrderByViewsDesc(String boardName, String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "WHERE p.board.boardName = :boardName " +
            "AND p.createdAt >= :oneWeekAgo " +
            "ORDER BY SIZE(p.reactions) DESC")
    List<Post> findTopLikedPosts(@Param("boardName") String boardName, @Param("oneWeekAgo") LocalDateTime oneWeekAgo, PageRequest pageable);

    List<Post> findByUser_Uuid(UUID uuid);

    boolean existsByPostIdAndUser_Uuid(Long postId, UUID uuid);
}
