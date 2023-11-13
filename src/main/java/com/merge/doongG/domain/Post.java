package com.merge.doongG.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "poster_pk")
    private User user;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer views = 0;

    @Column(nullable = false)
    private Integer comments = 0;

    @Column(columnDefinition = "timestamp default current_timestamp", nullable = false)
    private Timestamp createdAt;

    @Column(columnDefinition = "timestamp default current_timestamp", nullable = false)
    private Timestamp updatedAt;

    @Column(nullable = false)
    private Boolean commentAllowed = true;
}