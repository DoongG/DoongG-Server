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
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactionId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder.Default
    @Column(nullable = false)
    private boolean liked = false;

    @Builder.Default
    @Column(nullable = false)
    private boolean disliked = false;

    @Builder.Default
    @Column(nullable = false)
    private Integer likes = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer dislikes = 0;

    public void like() {
        this.liked = true;
        this.likes++;
    }

    public void dislike() {
        this.disliked = true;
        this.dislikes++;
    }

    public void undoLike() {
        this.liked = false;
        this.likes--;
    }

    public void undoDislike() {
        this.disliked = false;
        this.dislikes--;
    }
}