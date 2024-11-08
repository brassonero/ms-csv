package com.ebitware.users.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiError {
        private Date timestamp;
        private int status;
        private String error;
        private String message;
        private List<String> details;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationError {
        private Date timestamp;
        private int status;
        private String error;
        private String message;
        private List<FieldError> errors;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String message;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        List<FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldError(
                        error.getField(),
                        error.getDefaultMessage()))
                .collect(Collectors.toList());

        ValidationError errorResponse = new ValidationError(
                new Date(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Error de validación en los datos de entrada",
                fieldErrors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex,
            WebRequest request) {

        List<FieldError> fieldErrors = new ArrayList<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            FieldError fieldError = new FieldError(
                    violation.getPropertyPath().toString(),
                    violation.getMessage()
            );
            fieldErrors.add(fieldError);
        }

        ValidationError errorResponse = new ValidationError(
                new Date(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Error de validación en los datos de entrada",
                fieldErrors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<ApiError> handleEmailExists(EmailExistsException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ApiError errorResponse = new ApiError(
                new Date(),
                HttpStatus.CONFLICT.value(),
                "Conflict",
                "El email ya está registrado",
                details
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ApiError> handleUsuarioNotFound(UsuarioNotFoundException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ApiError errorResponse = new ApiError(
                new Date(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                "Usuario no encontrado",
                details
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaughtException(Exception ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ApiError errorResponse = new ApiError(
                new Date(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ha ocurrido un error inesperado",
                details
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}