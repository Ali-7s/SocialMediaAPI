package dev.ali.socialmediaapi.exception;

import dev.ali.socialmediaapi.model.ApiResponse;
import dev.ali.socialmediaapi.model.ValidationExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static dev.ali.socialmediaapi.utils.RequestUtils.getResponse;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@Slf4j
public class ValidationExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(Exception exception) {
        List<ValidationExceptionResponse> exceptionsList = new ArrayList<>();

        if (exception instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            for (FieldError error : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
                ValidationExceptionResponse validationError = new ValidationExceptionResponse(error.getField(), error.getDefaultMessage());
                exceptionsList.add(validationError);
            }
        }

        if (exception instanceof DataIntegrityViolationException integrityViolationException) {
            if (Objects.requireNonNull(integrityViolationException.getRootCause()).getMessage().contains("email")) {
                ValidationExceptionResponse validationError = new ValidationExceptionResponse("email", "Email already exists. Please try again or login to your existing account");
                exceptionsList.add(validationError);
            } else if (integrityViolationException.getRootCause().getMessage().contains("username")) {
                ValidationExceptionResponse validationError = new ValidationExceptionResponse("username", "Username already exists. Please try again or login to your existing account");
                exceptionsList.add(validationError);
            }
        }

        return new ResponseEntity<>(getResponse(Map.of("errors", exceptionsList), EMPTY), BAD_REQUEST);
    }
}
