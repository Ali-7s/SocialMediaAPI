package dev.ali.socialmediaapi.utils;

import dev.ali.socialmediaapi.model.ApiResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class RequestUtils {

    public static ApiResponse getResponse(Map<?, ?> data, String message, HttpStatus status) {
        return new ApiResponse(LocalDateTime.now().toString(), status.value(), HttpStatus.valueOf(status.value()), message, EMPTY, data);
    }

}
