package com.example.SpringWebDemo;

import com.example.SpringWebDemo.controller.PetController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHelper {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHelper.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServerErrorDto> handleValidationException(
            MethodArgumentNotValidException e
    ){
        log.error("Got validation exception");

        String detailedMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining());

        var serverError = new ServerErrorDto("Ошибка валидации запроса",
                detailedMessage,
                LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(serverError);
    }

    @ExceptionHandler()
    public ResponseEntity<ServerErrorDto> handleNotFoundException(
            Exception e
    ){
        log.error("Server error");

        var serverError = new ServerErrorDto("Ошибка сервера",
                e.getMessage(),
                LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(serverError);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ServerErrorDto> handleNoElementException(
            NoSuchElementException e
    ){
        log.error("Got NoSuchElementException exception");


        var serverError = new ServerErrorDto("Ошибка, данная сущность не найдена",
                e.getMessage(),
                LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(serverError);
    }

}
