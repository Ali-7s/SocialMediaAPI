package dev.ali.socialmediaapi.controller;

import dev.ali.socialmediaapi.dto.ConversationDTO;
import dev.ali.socialmediaapi.model.ChatMessage;
import dev.ali.socialmediaapi.repository.UserRepository;
import dev.ali.socialmediaapi.service.ChatMessageService;
import dev.ali.socialmediaapi.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/chat")
public class ChatController {

   private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;

    @GetMapping("/conversations/{userId}")
    public ResponseEntity<List<ConversationDTO>> findAllConversationsWithLastMessage(@PathVariable Long userId) {
        
        List<ConversationDTO> conversations = chatMessageService.findAllConversationsWithLastMessage(userId);
        return ResponseEntity.ok(conversations);
    }


    @MessageMapping("/chat")
    public void processMessage(
            @Payload ChatMessage chatMessage,
            @Header("simpSessionId") String sessionId,
            Principal user) {

        
        
        

        String chatId = chatRoomService
                .getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId());
        

        chatMessage.setChatId(chatId);
        

        ChatMessage saved = chatMessageService.save(chatMessage);

        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        messagingTemplate.convertAndSendToUser(
                userRepository.findById(chatMessage.getRecipientId()).orElseThrow().getEmail(),
                "/queue/messages",
                saved,
                headerAccessor.getMessageHeaders()
        );

        
    }





    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<?> findChatMessages ( @PathVariable Long senderId,
                                                @PathVariable Long recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

}

