package codeit.api.room.service;

import codeit.api.exception.ErrorCode;
import codeit.api.room.dto.response.ChatResponse;
import codeit.api.room.dto.response.RoomInfoResponse;
import codeit.api.room.dto.response.RoomResponse;
import codeit.api.room.exception.RoomException;
import codeit.domain.chat.repository.ChatMongoRepository;
import codeit.domain.room.entity.Room;
import codeit.domain.room.repository.RoomRepository;
import codeit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final ChatMongoRepository chatMongoRepository;

    public RoomInfoResponse retrieveRoom(User user, Long roomId) {
        return RoomInfoResponse.from(roomRepository.findParticipatedRoomById(roomId, user.getId())
                .orElseThrow(() -> new RoomException(ErrorCode.ROOM_NOT_FOUND)));
    }

    public Slice<RoomResponse> retrieveRooms(User user, Pageable pageable) {
        return roomRepository.findParticipatedRoomByUserId(user.getId(), pageable)
                .map(RoomResponse::from);
    }

    public Slice<ChatResponse> retrieveChats(User user, Long roomId, Pageable pageable) {
        validateRetrieveChats(roomId, user.getId());

        return chatMongoRepository.findByRoomId(roomId, pageable)
                .map(chat -> ChatResponse.from(chat, user.getId()));
    }

    private void validateRetrieveChats(Long roomId, Long userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException(ErrorCode.ROOM_NOT_FOUND));
        //TODO: validate that user is post's participant
    }

}
