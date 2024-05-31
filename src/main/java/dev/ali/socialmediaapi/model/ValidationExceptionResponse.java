package dev.ali.socialmediaapi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationExceptionResponse {
    private String field;
    private String message;

    public ValidationExceptionResponse(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
