package dev.ali.socialmediaapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record ApiResponse (String time, String message, Map<?, ?> data){
}
