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
import codeit.domain.user.constant.Role;
import codeit.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {
    @Mock
    private ChatMongoRepository chatMongoRepository;
    @Mock
    private RoomRepository roomRepository;
    @InjectMocks
    private ChatService chatService;

    User userA = User.builder()
            .email("userA@google.com")
            .nickname("a-a")
            .role(Role.ROLE_USER)
            .password("encoded")
            .build();

    Room room = Room.builder()
            .lastChatAt(LocalDateTime.MIN)
            .headcount(1)
            .build();

    @Test
    @DisplayName("메세지 전송 성공")
    void messageTest_success() {
        //given
        ChatMessageRequest request = new ChatMessageRequest("안녕하세요이");
        given(roomRepository.findById(anyLong())).willReturn(Optional.of(room));
        given(chatMongoRepository.save(any())).willReturn(Chat.builder()
                .content("안녕하세요이")
                .type(ChatType.MESSAGE)
                .user(userA)
                .roomId(1L).build());
        //when
        ChatResponse response = chatService.message(1L, request, userA);
        //then
        assertEquals(response.getContent(), "안녕하세요이");
        assertEquals(response.getType(), ChatType.MESSAGE);
        assertEquals(response.getSender().getNickname(), "a-a");
    }

    @Test
    @DisplayName("메세지 전송 실패_ROOM_NOT_FOUND")
    void messageTest_fail_ROOM_NOT_FOUND() {
        //given
        ChatMessageRequest request = new ChatMessageRequest("안녕하세요이");
        given(roomRepository.findById(anyLong())).willReturn(Optional.empty());
        //when
        ChatException e = assertThrows(ChatException.class,
                () -> chatService.message(1L, request, userA));
        //then
        assertEquals(ErrorCode.ROOM_NOT_FOUND, e.getErrorCode());
    }
}