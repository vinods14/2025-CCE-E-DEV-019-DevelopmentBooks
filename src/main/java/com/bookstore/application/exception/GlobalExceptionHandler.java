package com.bookstore.application.exception;

import com.bookstore.application.domain.pojo.ErrorMessage;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@Hidden
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BooksNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleResourceNotFound(BooksNotFoundException ex) {
        log.error("GlobalExceptionHandler#handleResourceNotFound::Message \n ### {}", ex.getMessage());
        return new ResponseEntity<>(ErrorMessage.builder()
                .code("EMPT404")
                .message(ex.getMessage())
                .build(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGeneralException(Exception ex) {
        log.error("GlobalExceptionHandler#handleGeneralException::Message \n ### {}", ex.getMessage());
        return new ResponseEntity<>(ErrorMessage.builder()
                .code("ERROR500")
                .message(ex.getMessage())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}