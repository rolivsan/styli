package br.com.styli.api.handler;

import br.com.styli.domain.exception.BadRequestException;
import br.com.styli.domain.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        body.put("error", "Validation error");

        Map<String, String> fields = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fields.put(fe.getField(), fe.getDefaultMessage());
        }
        body.put("fields", fields);
        return ResponseEntity.unprocessableEntity().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        // Em DEV, log detalhado no console
        ex.printStackTrace();
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno. Tente novamente.");
    }

    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(jakarta.validation.ConstraintViolationException ex) {
        var body = new java.util.HashMap<String, Object>();
        body.put("timestamp", java.time.LocalDateTime.now().toString());
        body.put("status", 422);
        body.put("error", "Validation error");

        var fields = new java.util.HashMap<String, String>();
        ex.getConstraintViolations().forEach(v -> fields.put(v.getPropertyPath().toString(), v.getMessage()));
        body.put("fields", fields);
        return ResponseEntity.unprocessableEntity().body(body);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(org.springframework.security.access.AccessDeniedException ex) {
        var body = new java.util.HashMap<String, Object>();
        body.put("timestamp", java.time.LocalDateTime.now().toString());
        body.put("status", 403);
        body.put("error", "Forbidden");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(403).body(body);
    }



    private ResponseEntity<?> build(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
