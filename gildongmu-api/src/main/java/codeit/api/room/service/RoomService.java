package codeit.api.room.service;

import codeit.api.exception.ErrorCode;
import codeit.api.room.dto.response.ChatResponse;
import codeit.api.room.exception.RoomException;
import codeit.domain.chat.repository.ChatMongoRepository;
import codeit.domain.room.entity.Room;
import codeit.domain.room.repository.RoomRepository;
import codeit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final ChatMongoRepository chatMongoRepository;

    public Slice<ChatResponse> retrieveChats(User user, Long roomId, Pageable pageable) {
        validateRetrieveChats(roomId, user.getId());

        return chatMongoRepository.findByRoomId(roomId, pageable)
                .map(chat -> ChatResponse.from(chat, user.getId()));
    }

    private void validateRetrieveChats(Long roomId, Long userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException(ErrorCode.ROOM_NOT_FOUND));
        //TODO: validate that user is room's participant
    }

}
