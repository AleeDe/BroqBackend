package com.example.shared_auth.exception;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + " " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(new ErrorResponse(msg));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrity(DataIntegrityViolationException ex) {
        String msg = "Data integrity violation";
        if (ex.getMessage() != null && ex.getMessage().contains("foods_category_check")) {
            msg = "Invalid food category. Allowed values: BREAKFAST, LUNCH, DINNER";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(msg));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(msg));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleJsonParse(HttpMessageNotReadableException ex) {
        // Often occurs when invalid enum value is provided in JSON body
        String msg = "Malformed request";
        if (ex.getCause() != null && ex.getCause().getMessage() != null && ex.getCause().getMessage().contains("Cannot deserialize value of type")) {
            msg = "Invalid enum value provided in request";
            return ResponseEntity.badRequest().body(new ErrorResponse(msg));
        }
        return ResponseEntity.badRequest().body(new ErrorResponse(msg));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAny(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("server.error"));
    }

    static class ErrorResponse {
        public final String message;
        public ErrorResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
    }
}
