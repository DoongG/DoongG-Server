package com.merge.doongG.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
@Entity
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post postId;

    @ManyToOne
    @JoinColumn(name = "poster_pk")
    private Post posterPk;

    @Column(length = 500, nullable = false)
    private String content;

    @Column(name = "created_at", columnDefinition = "timestamp default current_timestamp", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp default current_timestamp", nullable = false)
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "commenter_pk")
    private User user;

    public Comment() {}
}