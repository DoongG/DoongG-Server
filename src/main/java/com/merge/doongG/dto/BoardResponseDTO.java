package com.merge.doongG.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponseDTO {
    private String boardName;
    private String boardDefaultType;
    private List<PostDTO> posts;
    private Long postCount;
}