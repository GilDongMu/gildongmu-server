package codeit.api.room.service;

import codeit.api.exception.ErrorCode;
import codeit.api.room.dto.response.ChatResponse;
import codeit.api.room.dto.response.RoomInfoResponse;
import codeit.api.room.dto.response.RoomResponse;
import codeit.api.room.exception.RoomException;
import codeit.domain.chat.constant.ChatType;
import codeit.domain.chat.entity.Chat;
import codeit.domain.chat.repository.ChatMongoRepository;
import codeit.domain.post.entity.Post;
import codeit.domain.room.entity.Room;
import codeit.domain.room.repository.RoomRepository;
import codeit.domain.user.constant.Role;
import codeit.domain.user.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Optional;

import static codeit.domain.post.constant.Status.OPEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

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

    Post postA = Post.builder()
            .user(userA)
            .status(OPEN)
            .title("다낭 가실 분 모집합니다")
            .thumbnail("/POST/thubmbnail")
            .content("다낭 가요~")
            .participants((short) 2)
            .build();

    static User userC;


    @BeforeAll
    static void init() {
        userC = mock(User.class);
        given(userC.getId()).willReturn(1L);
        given(userC.getNickname()).willReturn("a");
    }

    @Test
    @DisplayName("내가 속한 소통공간 조회 성공")
    void retrieveRoomsTest_success() {
        //given
        given(roomRepository.findParticipatedRoomByUserId(anyLong(), any()))
                .willReturn(new SliceImpl<>(
                        List.of(Room.builder()
                                .headcount(2)
                                .post(postA)
                                .build())));
        //when
        Slice<RoomResponse> responses = roomService.retrieveRooms(userC, PageRequest.of(1,1));
        //then
        RoomResponse response = responses.getContent().get(0);
        assertEquals(2, response.headCount());
        assertEquals("다낭 가실 분 모집합니다", response.title());
        assertEquals("/POST/thubmbnail", response.thumbnail());
    }

    @Test
    @DisplayName("채팅방 채팅 조회 성공")
    void retrieveChatsTest_success() {
        //given
        given(roomRepository.findById(anyLong()))
                .willReturn(Optional.of(Room.builder()
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

    @Test
    @DisplayName("소통공간 정보 조회 성공")
    void retrieveRoomTest_success() {
        //given
        given(roomRepository.findParticipatedRoomById(anyLong(), anyLong()))
                .willReturn(Optional.of(Room.builder()
                        .headcount(2)
                        .post(postA)
                        .build()));
        //when
        RoomInfoResponse response = roomService.retrieveRoom(userC, 1L);
        //then
        assertEquals(2, response.headCount());
        assertEquals("다낭 가실 분 모집합니다", response.title());
        assertEquals("/POST/thubmbnail", response.thumbnail());
    }

    @Test
    @DisplayName("소통공간 정보 조회 실패-ROOM_NOT_FOUND")
    void retrieveRoomTest_fail_ROOM_NOT_FOUND() {
        //given
        given(roomRepository.findParticipatedRoomById(anyLong(), anyLong()))
                .willReturn(Optional.empty());
        //when
        RoomException e = assertThrows(RoomException.class,
                () -> roomService.retrieveRoom(userC, 1L));
        //then
        assertEquals(ErrorCode.ROOM_NOT_FOUND, e.getErrorCode());
    }

}