package com.merge.doongG.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "commenter_id", nullable = false)
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
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp default current_timestamp")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
}