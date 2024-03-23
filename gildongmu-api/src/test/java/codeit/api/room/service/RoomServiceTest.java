package codeit.api.room.service;

import codeit.api.exception.ErrorCode;
import codeit.api.room.dto.response.ChatResponse;
import codeit.api.room.exception.RoomException;
import codeit.domain.chat.constant.ChatType;
import codeit.domain.chat.entity.Chat;
import codeit.domain.chat.repository.ChatMongoRepository;
import codeit.domain.room.entity.Room;
import codeit.domain.room.repository.RoomRepository;
import codeit.domain.user.constant.Role;
import codeit.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {
    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ChatMongoRepository chatMongoRepository;

    @InjectMocks
    private RoomService roomService;

    User userA = User.builder()
            .email("userA@google.com")
            .nickname("a")
            .role(Role.ROLE_USER)
            .password("encoded")
            .build();

    User userB = User.builder()
            .email("userB@google.com")
            .nickname("b")
            .role(Role.ROLE_USER)
            .password("encoded")
            .build();

    @Test
    @DisplayName("채팅방 채팅 조회 성공")
    void retrieveChatsTest_success() {
        //given
        given(roomRepository.findById(anyLong()))
                .willReturn(Optional.of(Room.builder()
                        .lastChatAt(LocalDateTime.MIN)
                        .headcount(1)
                        .build()));

        given(chatMongoRepository.findByRoomId(anyLong(), any()))
                .willReturn(new SliceImpl<>(
                        List.of(Chat.builder()
                                        .roomId(1L)
                                        .content("안녕하세요")
                                        .type(ChatType.MESSAGE)
                                        .user(userA)
                                        .build(),
                                Chat.builder()
                                        .roomId(1L)
                                        .content("/image-path")
                                        .type(ChatType.IMAGE)
                                        .user(userB)
                                        .build())));

        Pageable requestdPageable = Pageable.ofSize(10);
        //when
        Slice<ChatResponse> responses = roomService.retrieveChats(userA, 1L, requestdPageable);
        //then
        List<ChatResponse> chats = responses.getContent();
        assertEquals(chats.size(), 2);
        assertEquals(chats.get(0).content(), "안녕하세요");
        assertTrue(chats.get(0).isMessageType());
        assertEquals(chats.get(0).sender().nickname(), "a");
        assertEquals(chats.get(1).content(), "/image-path");
        assertFalse(chats.get(1).isMessageType());
        assertEquals(chats.get(1).sender().nickname(), "b");
    }

    @Test
    @DisplayName("채팅방 채팅 조회 실패-ROOM_NOT_FOUND")
    void retrieveChatsTest_fail_ROOM_NOT_FOUND() {
        //given
        given(roomRepository.findById(anyLong())).willReturn(Optional.empty());
        Pageable requestdPageable = Pageable.ofSize(10);
        //when
        RoomException e = assertThrows(RoomException.class,
                () -> roomService.retrieveChats(userA, 1L, requestdPageable));
        //then
        assertEquals(ErrorCode.ROOM_NOT_FOUND, e.getErrorCode());
    }

}