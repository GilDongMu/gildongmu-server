package codeit.chat.service;

import codeit.chat.controller.dto.request.ChatMessageRequest;
import codeit.chat.exception.ChatException;
import codeit.chat.exception.ErrorCode;
import codeit.common.dto.transfer.ChatDto;
import codeit.domain.chat.constant.ChatType;
import codeit.domain.chat.entity.Chat;
import codeit.domain.chat.entity.ChatUser;
import codeit.domain.chat.repository.ChatMongoRepository;
import codeit.domain.room.repository.RoomRepository;
import codeit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMongoRepository chatMongoRepository;
    private final RoomRepository roomRepository;

    public ChatDto message(Long roomId, ChatMessageRequest request, User user) {
        validateRoomParticipant(roomId, user.getId());

        return ChatDto.of(chatMongoRepository.save(Chat.builder()
                .roomId(roomId)
                .content(request.getMessage())
                .type(ChatType.MESSAGE)
                .chatUser(ChatUser.from(user))
                .build()));
    }

    private void validateRoomParticipant(Long roomId, Long userId) {
        if(!roomRepository.existsParticipatedRoomById(roomId, userId))
            throw new ChatException(ErrorCode.ROOM_NOT_FOUND);
    }
}
