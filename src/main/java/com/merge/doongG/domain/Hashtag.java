package com.merge.doongG.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hashtagId;

    @Column(unique = true)
    private String hashtagName;

    @ManyToMany(mappedBy = "hashtags", cascade = CascadeType.PERSIST)
    private List<Post> posts;
}
