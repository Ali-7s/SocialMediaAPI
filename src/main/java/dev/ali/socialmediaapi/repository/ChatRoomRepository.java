package dev.ali.socialmediaapi.repository;

import dev.ali.socialmediaapi.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    @Query("SELECT cm FROM ChatRoom cm WHERE (cm.recipientId = :firstId AND cm.senderId = :secondId) OR (cm.senderId = :firstId AND cm.recipientId = :secondId)")
    Optional<ChatRoom> findBySenderIdAndRecipientId(Long firstId, Long secondId);

}