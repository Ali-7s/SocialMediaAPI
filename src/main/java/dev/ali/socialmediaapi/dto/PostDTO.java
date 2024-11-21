package dev.ali.socialmediaapi.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private UserSummaryDTO user;
}

