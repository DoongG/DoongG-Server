package com.merge.doongG.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(nullable = false, unique = true)
    private String boardName;

//    @Column(nullable = false)
//    private String boardDefaultType;

    public void changeBoardName(String newBoardName) {
        this.boardName = newBoardName;
    }
}
