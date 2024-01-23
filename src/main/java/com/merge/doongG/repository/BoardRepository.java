package com.merge.doongG.repository;

import com.merge.doongG.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByBoardName(String boardName);

    @Query("SELECT b.boardId FROM Board b WHERE b.boardName = :boardName")
    Long findIdByName(@Param("boardName") String boardName);
}
