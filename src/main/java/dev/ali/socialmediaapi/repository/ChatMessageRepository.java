package dev.ali.socialmediaapi.repository;

import dev.ali.socialmediaapi.model.ChatMessage;
import dev.ali.socialmediaapi.model.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

    @Query("SELECT m FROM ChatMessage m WHERE (m.recipientId = :firstId AND m.senderId = :secondId) OR (m.senderId = :firstId AND m.recipientId = :secondId)")
    List<ChatMessage> findChatMessagesByRecipientIdAndSenderId(Long firstId, Long secondId);

    @Query("SELECT m FROM ChatMessage m WHERE (m.senderId = :userId OR m.recipientId = :userId) " +
            "AND m.id = (SELECT MAX(m2.id) FROM ChatMessage m2 WHERE m2.chatId = m.chatId)")
    List<ChatMessage> findConversationsByUserId(Long userId);

    long countBySenderIdAndRecipientIdAndStatus(Long senderId, Long recipientId, MessageStatus status);

}
