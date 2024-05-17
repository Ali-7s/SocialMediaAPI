package dev.ali.socialmediaapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record ApiResponse (String time, int code, HttpStatus status, String message, String exception, Map<?, ?> data){
}
