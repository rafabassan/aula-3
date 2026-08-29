package com.example.crud.infra;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class RequestsExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDTO> threat404(){
        ExceptionDTO response = new ExceptionDTO("Data not found with provided ID", 404);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDTO> threatInvalidArgument(MethodArgumentTypeMismatchException ex){
        String message = "Invalid value '" + ex.getValue() + "' for parameter '" + ex.getName() + "'";
        ExceptionDTO response = new ExceptionDTO(message, 400);
        return ResponseEntity.badRequest().body(response);
    }
}
