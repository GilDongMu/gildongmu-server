package codeit.api.room.service;

import codeit.api.exception.ErrorCode;
import codeit.api.participant.dto.response.ParticipantResponse;
import codeit.api.participant.exception.ParticipantException;
import codeit.api.room.dto.response.ChatGroupByDateResponse;
import codeit.api.room.dto.response.ChatResponse;
import codeit.api.room.dto.response.RoomInfoResponse;
import codeit.api.room.dto.response.RoomResponse;
import codeit.api.room.exception.RoomException;
import codeit.domain.chat.entity.Chat;
import codeit.domain.chat.repository.ChatMongoRepository;
import codeit.domain.participant.repository.ParticipantRepository;
import codeit.domain.room.repository.RoomRepository;
import codeit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final ChatMongoRepository chatMongoRepository;
    private final ParticipantRepository participantRepository;

    public RoomInfoResponse retrieveRoom(User user, Long roomId) {
        return RoomInfoResponse.from(roomRepository.findParticipatedRoomById(roomId, user.getId())
                .orElseThrow(() -> new RoomException(ErrorCode.ROOM_NOT_FOUND)));
    }

    public Slice<RoomResponse> retrieveRooms(User user, Pageable pageable) {
        return roomRepository.findParticipatedRoomByUserId(user.getId(), pageable)
                .map(RoomResponse::from);
    }

    public Slice<ChatGroupByDateResponse> retrieveChats(User user, Long roomId, Pageable pageable) {
        validateRetrieveRoom(roomId, user.getId());

        return getGroupingChatSlices(chatMongoRepository.findByRoomId(roomId, pageable), user.getId());
    }

    public Slice<ChatGroupByDateResponse> getGroupingChatSlices(Slice<Chat> chatSlice, Long userId) {
        if (chatSlice.getContent().isEmpty())
            return new SliceImpl<>(new ArrayList<>(), chatSlice.getPageable(), chatSlice.hasNext());
        List<ChatGroupByDateResponse> content = new ArrayList<>();
        List<ChatResponse> sameDayChats = new ArrayList<>();
        LocalDate prevDate = chatSlice.getContent().get(0).getCreatedAt().toLocalDate();
        for (Chat chat : chatSlice.getContent()) {
            LocalDate curDate = chat.getCreatedAt().toLocalDate();
            if (!prevDate.equals(curDate)) {
                content.add(ChatGroupByDateResponse.of(prevDate, sameDayChats));
                sameDayChats = new ArrayList<>();
                prevDate = curDate;
            }
            sameDayChats.add(ChatResponse.from(chat, userId));
        }
        content.add(ChatGroupByDateResponse.of(prevDate, sameDayChats));
        return new SliceImpl<>(content, chatSlice.getPageable(), chatSlice.hasNext());
    }


    private void validateRetrieveRoom(Long roomId, Long userId) {
        if (!roomRepository.existsParticipatedRoomById(roomId, userId))
            throw new RoomException(ErrorCode.ROOM_NOT_FOUND);
    }


    public List<ParticipantResponse> retrieveParticipantsByRoomId(Long roomId, User user) {
        validateRetrieveRoom(roomId, user.getId());
        return participantRepository.findAcceptedParticipantsByRoomId(roomId)
                .stream().map(participant -> ParticipantResponse.from(participant, user.getId()))
                .sorted((o1, o2) -> {
                    if (o1.isLeader() == o2.isLeader()) {
                        if (o2.user().isCurrentUser() == o1.user().isCurrentUser())
                            return o2.user().nickname().compareTo(o1.user().nickname());
                        return Boolean.compare(o2.user().isCurrentUser(), o1.user().isCurrentUser());
                    }
                    return Boolean.compare(o2.isLeader(), o1.isLeader());
                }).collect(Collectors.toList());
    }

}
