package com.merge.doongG.util;

import com.merge.doongG.domain.Board;
import com.merge.doongG.dto.BoardDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoardMapper {
    private final ModelMapper mapper;

    // MainConfig에서 등록한 ModelMapper 주입
    @Autowired
    public BoardMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    // 엔티티 -> DTO 변환
    public BoardDTO convertToDTO(Board board) {
        return mapper.map(board, BoardDTO.class);
    }

    // DTO -> 엔티티 변환
    public Board convertToEntity(BoardDTO boardDTO) {
        return mapper.map(boardDTO, Board.class);
    }
}
