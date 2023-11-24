package com.merge.doongG.service;

import com.merge.doongG.domain.Board;
import com.merge.doongG.domain.User;
import com.merge.doongG.repository.BoardRepository;
import com.merge.doongG.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.UUID;

// DB에 초기 데이터 추가
@Service
public class DBInitializer implements CommandLineRunner {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public DBInitializer(BoardRepository boardRepository,
                         UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        createDefaultBoardsIfNotExist();
        // 아래에 메서드 만들어서 여기에 초기 실행되도록 할 수 있음
    }

    private void createDefaultBoardsIfNotExist() {
        for (int i = 1; i <= 6; i++) {
            String boardName = "DefaultBoard" + i;
            if (boardRepository.findByBoardName(boardName).isEmpty()) {
                if (i % 2 == 1) {
                    Board defaultBoard = Board.builder().boardName(boardName).boardDefaultType("gallery").build();
                    boardRepository.save(defaultBoard);
                } else {
                    Board defaultBoard = Board.builder().boardName(boardName).boardDefaultType("list").build();
                    boardRepository.save(defaultBoard);
                }
            }
        }
    }

}
