package com.merge.doongG.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostSummaryDTO {
    private Long postId;
    private String title;
    private LocalDateTime createdAt;
}