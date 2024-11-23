package dev.ali.socialmediaapi.service;

import dev.ali.socialmediaapi.dto.ConversationDTO;
import dev.ali.socialmediaapi.dto.UserSummaryDTO;
import dev.ali.socialmediaapi.model.ChatMessage;
import dev.ali.socialmediaapi.model.MessageStatus;
import dev.ali.socialmediaapi.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final UserService userService;


    public List<ConversationDTO> findAllConversationsWithLastMessage(Long userId) {


        List<ChatMessage> messages = repository.findConversationsByUserId(userId);

        Set<Long> partnerIds = new HashSet<>();
        List<ConversationDTO> conversations = new ArrayList<>();

        for (ChatMessage message : messages) {
            Long partnerId = message.getSenderId().equals(userId)
                    ? message.getRecipientId()
                    : message.getSenderId();

            if (!partnerIds.contains(partnerId)) {
                partnerIds.add(partnerId);
                UserSummaryDTO conversationPartner = userService.getUserSummary(partnerId);

                ConversationDTO conversation = new ConversationDTO(conversationPartner, message);
                conversations.add(conversation);
            }
        }

        return conversations;
    }

    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        repository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(Long senderId, Long recipientId) {


        return repository.findChatMessagesByRecipientIdAndSenderId(senderId, recipientId);
    }


}
