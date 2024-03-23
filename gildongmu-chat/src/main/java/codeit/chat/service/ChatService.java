package codeit.chat.service;

import codeit.chat.controller.dto.request.ChatMessageRequest;
import codeit.chat.controller.dto.response.ChatMessageResponse;
import codeit.domain.chat.constant.ChatType;
import codeit.domain.chat.entity.Chat;
import codeit.domain.chat.repository.ChatMongoRepository;
import codeit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMongoRepository chatMongoRepository;

    public ChatMessageResponse message(Long roomId, ChatMessageRequest request, User user) {
        return ChatMessageResponse.from(chatMongoRepository.save(Chat.builder()
                .roomId(roomId)
                .content(request.getMessage())
                .type(ChatType.MESSAGE)
                .user(user)
                .build()));
    }
}
