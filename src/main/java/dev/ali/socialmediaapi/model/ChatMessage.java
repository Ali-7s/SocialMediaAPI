package dev.ali.socialmediaapi.model;

import lombok.Data;

@Data
public class ChatMessage {
    private String content;
    private User sender;
    private MessageType type;
}
