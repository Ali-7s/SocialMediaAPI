package dev.ali.socialmediaapi.utils;

import dev.ali.socialmediaapi.model.ApiResponse;

import java.time.LocalDateTime;
import java.util.Map;

public class RequestUtils {

    public static ApiResponse getResponse(Map<?, ?> data, String message) {
        return new ApiResponse(LocalDateTime.now().toString(), message, data);
    }

}
