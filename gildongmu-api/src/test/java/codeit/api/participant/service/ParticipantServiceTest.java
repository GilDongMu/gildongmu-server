package codeit.api.participant.service;

import codeit.api.exception.ErrorCode;
import codeit.api.participant.dto.ParticipantResponse;
import codeit.api.participant.exception.ParticipantException;
import codeit.api.post.exception.PostException;
import codeit.domain.participant.constant.Status;
import codeit.domain.participant.entity.Participant;
import codeit.domain.participant.repository.ParticipantRepository;
import codeit.domain.post.entity.Post;
import codeit.domain.post.repository.PostRepository;
import codeit.domain.user.constant.Role;
import codeit.domain.user.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {
    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private ParticipantService participantService;

    static User userA;

    static Post post;
    User userB = User.builder()
            .email("userB@google.com")
            .nickname("b")
            .role(Role.ROLE_USER)
            .password("encoded")
            .build();

    User userC = User.builder()
            .email("userC@google.com")
            .nickname("c")
            .role(Role.ROLE_USER)
            .password("encoded")
            .build();

    @BeforeAll
    static void init() {
        userA = mock(User.class);
        given(userA.getId()).willReturn(1L);
        given(userA.getNickname()).willReturn("a");
        post = mock(Post.class);
        given(post.getId()).willReturn(1L);
    }

    @Test
    @DisplayName("여행글 참여 신청 성공")
    void applyForParticipantTest_success() {
        //given
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(participantRepository.existsByUserAndPost(any(), any())).willReturn(false);
        //when
        participantService.applyForParticipant(1L, userA);
        ArgumentCaptor<Participant> captor = ArgumentCaptor.forClass(Participant.class);
        //then
        verify(participantRepository, times(1)).save(captor.capture());
        Participant saved = captor.getValue();
        assertEquals(saved.getStatus(), Status.PENDING);
        assertFalse(saved.isLeader());
        assertEquals(post, saved.getPost());
        assertEquals(userA, saved.getUser());
    }

    @Test
    @DisplayName("여행글 참여 신청 실패_POST_NOT_FOUND")
    void applyForParticipantTest_success_POST_NOT_FOUND() {
        //given
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());
        //when
        PostException e = assertThrows(PostException.class,
                () -> participantService.applyForParticipant(1L, userA));
        //then
        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    @DisplayName("여행글 참여 신청 실패_ALREADY_REGISTERED_PARTICIPANT")
    void applyForParticipantTest_success_ALREADY_REGISTERED_PARTICIPANT() {
        //given
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(participantRepository.existsByUserAndPost(any(), any())).willReturn(true);
        //when
        ParticipantException e = assertThrows(ParticipantException.class,
                () -> participantService.applyForParticipant(1L, userA));
        //then
        assertEquals(ErrorCode.ALREADY_REGISTERED_PARTICIPANT, e.getErrorCode());
    }

    @Test
    @DisplayName("여행글 참여 취소 성공")
    void exitParticipantTest_success() {
        //given
        Participant participant = Participant.builder()
                .user(userA)
                .status(Status.PENDING)
                .isLeader(false)
                .post(post)
                .build();
        given(participantRepository.findByUserIdAndPostIdAndStatusIsNot(anyLong(), anyLong(), any()))
                .willReturn(Optional.of(participant));
        //when
        participantService.exitParticipant(1L, userA);
        //then
        assertEquals(Status.DELETED, participant.getStatus());
    }

    @Test
    @DisplayName("여행글 참여 취소 실패-PARTICIPANT_NOT_FOUND")
    void exitParticipantTest_fail_PARTICIPANT_NOT_FOUND() {
        //given
        given(participantRepository.findByUserIdAndPostIdAndStatusIsNot(anyLong(), anyLong(), any()))
                .willReturn(Optional.empty());
        //when
        ParticipantException e = assertThrows(ParticipantException.class,
                () -> participantService.exitParticipant(1L, userA));
        //then
        assertEquals(ErrorCode.PARTICIPANT_NOT_FOUND, e.getErrorCode());
    }

    @Test
    @DisplayName("신청자 거절 및 추방 성공")
    void denyParticipantTest_success() {
        //given
        given(participantRepository.findByUserIdAndPostId(anyLong(), anyLong()))
                .willReturn(Optional.of(Participant.builder()
                        .user(userA)
                        .status(Status.ACCEPTED)
                        .isLeader(true)
                        .post(post)
                        .build()));
        Participant participantToBeDeleted = Participant.builder()
                .user(userB)
                .status(Status.PENDING)
                .isLeader(false)
                .post(post)
                .build();
        given(participantRepository.findById(anyLong()))
                .willReturn(Optional.of(participantToBeDeleted));
        //when
        participantService.denyParticipant(1L, 1L, userA);
        //then
        assertEquals(participantToBeDeleted.getStatus(), Status.DELETED);
    }

    @Test
    @DisplayName("신청자 거절 및 추방 성공-NOT_LEADER_USER")
    void denyParticipantTest_fail_NOT_LEADER_USER() {
        //given
        given(participantRepository.findByUserIdAndPostId(anyLong(), anyLong()))
                .willReturn(Optional.of(Participant.builder()
                        .user(userA)
                        .status(Status.ACCEPTED)
                        .isLeader(false)
                        .post(post)
                        .build()));
        //when
        ParticipantException e = assertThrows(ParticipantException.class,
                () -> participantService.denyParticipant(1L, 1L, userA));
        //then
        assertEquals(ErrorCode.NOT_LEADER_USER, e.getErrorCode());
    }

    @Test
    @DisplayName("신청자 거절 및 추방 성공-PARTICIPANT_NOT_FOUND")
    void acceptParticipantTest_fail_PARTICIPANT_NOT_FOUND() {
        //given
        given(participantRepository.findByUserIdAndPostId(anyLong(), anyLong()))
                .willReturn(Optional.of(Participant.builder()
                        .user(userA)
                        .status(Status.ACCEPTED)
                        .isLeader(true)
                        .post(post)
                        .build()));
        given(participantRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        ParticipantException e = assertThrows(ParticipantException.class,
                () -> participantService.denyParticipant(1L, 1L, userA));
        //then
        assertEquals(ErrorCode.PARTICIPANT_NOT_FOUND, e.getErrorCode());
    }

    @Test
    @DisplayName("신청자 수락 성공")
    void acceptParticipantTest_success() {
        //given
        given(participantRepository.findByUserIdAndPostId(anyLong(), anyLong()))
                .willReturn(Optional.of(Participant.builder()
                        .user(userA)
                        .status(Status.ACCEPTED)
                        .isLeader(true)
                        .post(post)
                        .build()));
        Participant participantToBeDeleted = Participant.builder()
                .user(userB)
                .status(Status.PENDING)
                .isLeader(false)
                .post(post)
                .build();
        given(participantRepository.findById(anyLong()))
                .willReturn(Optional.of(participantToBeDeleted));
        //when
        participantService.acceptParticipant(1L, 1L, userA);
        //then
        assertEquals(participantToBeDeleted.getStatus(), Status.ACCEPTED);
    }

    @Test
    @DisplayName("신청자 수락 실패-NOT_LEADER_USER")
    void acceptParticipantTest_fail_NOT_LEADER_USER() {
        //given
        given(participantRepository.findByUserIdAndPostId(anyLong(), anyLong()))
                .willReturn(Optional.of(Participant.builder()
                        .user(userA)
                        .status(Status.ACCEPTED)
                        .isLeader(false)
                        .post(post)
                        .build()));
        //when
        ParticipantException e = assertThrows(ParticipantException.class,
                () -> participantService.acceptParticipant(1L, 1L, userA));
        //then
        assertEquals(ErrorCode.NOT_LEADER_USER, e.getErrorCode());
    }

    @Test
    @DisplayName("신청자 수락 실패-PARTICIPANT_NOT_FOUND")
    void denyParticipantTest_fail_PARTICIPANT_NOT_FOUND() {
        //given
        given(participantRepository.findByUserIdAndPostId(anyLong(), anyLong()))
                .willReturn(Optional.of(Participant.builder()
                        .user(userA)
                        .status(Status.ACCEPTED)
                        .isLeader(true)
                        .post(post)
                        .build()));
        given(participantRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        ParticipantException e = assertThrows(ParticipantException.class,
                () -> participantService.acceptParticipant(1L, 1L, userA));
        //then
        assertEquals(ErrorCode.PARTICIPANT_NOT_FOUND, e.getErrorCode());
    }

    @Test
    @DisplayName("신청자 조회 성공")
    void retrieveParticipantsTest_success_WhenRetrievingPendingUser() {
        //given
        given(participantRepository.findByUserIdAndPostId(anyLong(), anyLong()))
                .willReturn(Optional.of(Participant.builder()
                        .user(userA)
                        .status(Status.ACCEPTED)
                        .isLeader(true)
                        .post(post)
                        .build()));
        given(participantRepository.findByPostIdAndStatus(anyLong(), any()))
                .willReturn(List.of(Participant.builder()
                                .user(userB)
                                .status(Status.PENDING)
                                .isLeader(false)
                                .post(post)
                                .build(),
                        Participant.builder()
                                .user(userC)
                                .status(Status.PENDING)
                                .isLeader(false)
                                .post(post)
                                .build()));
        //when
        List<ParticipantResponse> response = participantService.retrieveParticipants(1L, userA, "PENDING");
        //then
        assertEquals("b", response.get(0).user().nickname());
        assertEquals("c", response.get(1).user().nickname());
    }

    @Test
    @DisplayName("신청자 조회 실패-NOT_LEADER_USER")
    void retrieveParticipantsTest_fail_NOT_LEADER_USER_WhenRetrievingPendingUser() {
        //given
        given(participantRepository.findByUserIdAndPostId(anyLong(), anyLong()))
                .willReturn(Optional.of(Participant.builder()
                        .user(userA)
                        .status(Status.ACCEPTED)
                        .isLeader(false)
                        .post(post)
                        .build()));
        //when
        ParticipantException e = assertThrows(ParticipantException.class,
                () -> participantService.retrieveParticipants(1L, userA, "PENDING"));
        //then
        assertEquals(ErrorCode.NOT_LEADER_USER, e.getErrorCode());
    }

    @Test
    @DisplayName("참여자 조회 성공-참여중")
    void retrieveParticipantsTest_success_WhenRetrievingAcceptedUser() {
        //given
        given(participantRepository.existsByUserIdAndPostIdAndStatusIsNot(anyLong(), anyLong(), any()))
                .willReturn(true);
        given(participantRepository.findByPostIdAndStatusOrPostIdAndUser(anyLong(), any(), anyLong(), any()))
                .willReturn(List.of(Participant.builder()
                                .user(userB)
                                .status(Status.ACCEPTED)
                                .isLeader(false)
                                .post(post)
                                .build(),
                        Participant.builder()
                                .user(userA)
                                .status(Status.ACCEPTED)
                                .isLeader(false)
                                .post(post)
                                .build(),
                        Participant.builder()
                                .user(userC)
                                .status(Status.ACCEPTED)
                                .isLeader(true)
                                .post(post)
                                .build()));
        //when
        List<ParticipantResponse> response = participantService.retrieveParticipants(1L, userA, "ACCEPTED");
        //then
        assertEquals("a", response.get(0).user().nickname());
        assertTrue(response.get(0).user().isCurrentUser());
        assertFalse(response.get(0).isLeader());
        assertEquals("c", response.get(1).user().nickname());
        assertFalse(response.get(1).user().isCurrentUser());
        assertTrue(response.get(1).isLeader());
        assertEquals("b", response.get(2).user().nickname());
        assertFalse(response.get(2).user().isCurrentUser());
        assertFalse(response.get(2).isLeader());
    }

    @Test
    @DisplayName("참여자 조회 실패-NOT_PARTICIPANT_USER-참여중")
    void retrieveParticipantsTest_fail_NOT_PARTICIPANT_USER_WhenRetrievingAcceptedUser() {
        //given
        given(participantRepository.existsByUserIdAndPostIdAndStatusIsNot(anyLong(), anyLong(), any()))
                .willReturn(false);
        //when
        ParticipantException e = assertThrows(ParticipantException.class,
                () -> participantService.retrieveParticipants(1L, userA, "ACCEPTED"));
        //then
        assertEquals(ErrorCode.NOT_PARTICIPANT_USER, e.getErrorCode());
    }

}