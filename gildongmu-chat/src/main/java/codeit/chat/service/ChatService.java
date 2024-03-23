package codeit.chat.service;

import codeit.chat.controller.dto.request.ChatMessageRequest;
import codeit.chat.controller.dto.response.ChatResponse;
import codeit.chat.exception.ChatException;
import codeit.chat.exception.ErrorCode;
import codeit.domain.chat.constant.ChatType;
import codeit.domain.chat.entity.Chat;
import codeit.domain.chat.repository.ChatMongoRepository;
import codeit.domain.room.entity.Room;
import codeit.domain.room.repository.RoomRepository;
import codeit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMongoRepository chatMongoRepository;
    private final RoomRepository roomRepository;

    public ChatResponse message(Long roomId, ChatMessageRequest request, User user) {
        validateChat(roomId, user.getId());

        return ChatResponse.from(chatMongoRepository.save(Chat.builder()
                .roomId(roomId)
                .content(request.getMessage())
                .type(ChatType.MESSAGE)
                .user(user)
                .build()));
    }

    private void validateChat(Long roomId, Long userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(ErrorCode.ROOM_NOT_FOUND));
    }
}
