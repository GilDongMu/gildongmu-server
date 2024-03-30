package codeit.api.room.service;

import codeit.api.exception.ErrorCode;
import codeit.api.room.dto.response.ChatGroupByDateResponse;
import codeit.api.room.dto.response.ChatResponse;
import codeit.api.room.dto.response.RoomInfoResponse;
import codeit.api.room.dto.response.RoomResponse;
import codeit.api.room.exception.RoomException;
import codeit.domain.chat.constant.ChatType;
import codeit.domain.chat.entity.Chat;
import codeit.domain.chat.entity.ChatUser;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static codeit.domain.post.constant.Status.OPEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {
    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ChatMongoRepository chatMongoRepository;

    @InjectMocks
    private RoomService roomService;

    static User userA = User.builder()
            .email("userA@google.com")
            .nickname("a")
            .role(Role.ROLE_USER)
            .password("encoded")
            .build();

    static User userB = User.builder()
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
    static Chat chatA;
    static Chat chatB;


    @BeforeAll
    static void init() {
        userC = mock(User.class);
        given(userC.getId()).willReturn(1L);
        given(userC.getNickname()).willReturn("a");
        chatA = getMockChat(1L, "안녕하세요", ChatType.MESSAGE, userA, LocalDateTime.MAX);
        chatB = getMockChat(1L, "/image-path", ChatType.IMAGE, userB, LocalDateTime.MIN);
    }

    static private Chat getMockChat(Long roomId, String content, ChatType type, User user, LocalDateTime createdAt) {
        Chat chat = mock(Chat.class);
        given(chat.getRoomId()).willReturn(roomId);
        given(chat.getType()).willReturn(type);
        given(chat.getContent()).willReturn(content);
        given(chat.getSender()).willReturn(ChatUser.from(user));
        given(chat.getCreatedAt()).willReturn(createdAt);
        return chat;
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
        Slice<RoomResponse> responses = roomService.retrieveRooms(userC, PageRequest.of(1, 1));
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
        given(roomRepository.existsParticipatedRoomById(anyLong(), anyLong()))
                .willReturn(true);

        given(chatMongoRepository.findByRoomId(anyLong(), any()))
                .willReturn(new SliceImpl<>(
                        List.of(chatA,
                                chatB)));

        Pageable requestdPageable = Pageable.ofSize(10);
        //when
        Slice<ChatGroupByDateResponse> responses = roomService.retrieveChats(userC, 1L, requestdPageable);
        //then
        List<ChatGroupByDateResponse> chatGroupByDateResponses = responses.getContent();
        List<ChatResponse> chats = chatGroupByDateResponses.get(0).chats();
        assertEquals(chatGroupByDateResponses.size(), 2);
        assertEquals(chats.get(0).content(), "안녕하세요");
        assertTrue(chats.get(0).isMessageType());
        assertEquals(chats.get(0).sender().nickname(), "a");
    }

    @Test
    @DisplayName("채팅방 채팅 조회 실패-ROOM_NOT_FOUND")
    void retrieveChatsTest_fail_ROOM_NOT_FOUND() {
        //given
        given(roomRepository.existsParticipatedRoomById(anyLong(), anyLong()))
                .willReturn(false);
        Pageable requestdPageable = Pageable.ofSize(10);
        //when
        RoomException e = assertThrows(RoomException.class,
                () -> roomService.retrieveChats(userC, 1L, requestdPageable));
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