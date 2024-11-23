package dev.ali.socialmediaapi.service;

import dev.ali.socialmediaapi.model.ChatRoom;
import dev.ali.socialmediaapi.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public String getChatId(Long senderId, Long recipientId) {
        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .orElseGet(() -> {
                    String chatId = String.format("%s_%s", senderId, recipientId);


                    ChatRoom senderRecipient = ChatRoom.builder()
                            .chatId(chatId)
                            .senderId(senderId)
                            .recipientId(recipientId)
                            .build();

                    ChatRoom recipientSender = ChatRoom.builder()
                            .chatId(chatId)
                            .senderId(recipientId)
                            .recipientId(senderId)
                            .build();


                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return chatId;
                });
    }

}