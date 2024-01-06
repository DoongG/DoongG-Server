package com.merge.doongG.service;

import com.merge.doongG.domain.Board;
import com.merge.doongG.repository.BoardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

// DB에 초기 데이터 추가
@Service
public class DBInitializer implements CommandLineRunner {
    private final BoardRepository boardRepository;

    public DBInitializer(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public void run(String... args) {
        createDefaultBoardsIfNotExist();
        // 아래에 메서드 만들어서 여기에 초기 실행되도록 할 수 있음
    }

    private void createDefaultBoardsIfNotExist() {
        createDefaultBoardIfNotExist("자유게시판", "list");
        createDefaultBoardIfNotExist("레시피게시판", "gallery");
        createDefaultBoardIfNotExist("맛집게시판", "gallery");
        createDefaultBoardIfNotExist("게임게시판", "list");
        createDefaultBoardIfNotExist("중고거래게시판", "gallery");
        createDefaultBoardIfNotExist("생활꿀팁게시판", "list");
    }

    private void createDefaultBoardIfNotExist(String boardName, String boardDefaultType) {
        if (boardRepository.findByBoardName(boardName).isEmpty()) {
            Board defaultBoard = Board.builder()
                    .boardName(boardName)
                    .boardDefaultType(boardDefaultType)
                    .build();
            boardRepository.save(defaultBoard);
        }
    }

}
