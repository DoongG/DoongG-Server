package com.merge.doongG.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(RuntimeException.class) // RuntimeException이 발생하면 이 메서드를 실행
    public ResponseEntity<?> runtimeException(RuntimeException e) { // RuntimeException이 발생하면 e를 받아서 실행
        return ResponseEntity.status(HttpStatus.CONFLICT) // 409 에러
                .body(e.getMessage()); // e.getMessage()를 body에 담아서 리턴 -> userName + "는 이미 있습니다."
    }
}
