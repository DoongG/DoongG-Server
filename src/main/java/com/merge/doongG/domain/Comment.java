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
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "commenter_pk", referencedColumnName = "pk")
    private User commenter;

    @Column(length = 500, nullable = false)
    private String content;

    @Column(name = "created_at", columnDefinition = "timestamp default current_timestamp", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp default current_timestamp", nullable = false)
    private Timestamp updatedAt;
}