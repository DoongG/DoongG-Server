package com.merge.doongG.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UnifiedBoardDTO {
    private Long boardId;
    private String boardName;
    private List<PostSummaryDTO> posts;
}
