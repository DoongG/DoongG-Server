package com.merge.doongG.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name = "commenter_id", referencedColumnName = "id")
    private User commenter;

    // 부모 댓글 (대댓글의 경우)
    @Builder.Default
    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment = null;

    @Builder.Default
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> childComments = new ArrayList<>();

    @Column(length = 500, nullable = false)
    private String content;

    @Column(name = "created_at", columnDefinition = "timestamp default current_timestamp", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp default current_timestamp", nullable = false)
    private Timestamp updatedAt;
}