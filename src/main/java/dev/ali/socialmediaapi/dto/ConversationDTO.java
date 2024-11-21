package dev.ali.socialmediaapi.dto;

import dev.ali.socialmediaapi.model.ChatMessage;

public class ConversationDTO {
    private UserSummaryDTO userSummary;
    private ChatMessage lastMessage;

    public ConversationDTO(UserSummaryDTO userSummary, ChatMessage lastMessage) {
        this.userSummary = userSummary;
        this.lastMessage = lastMessage;
    }
}
