package codeit.api.history.service;

import codeit.api.post.dto.PostItem;
import codeit.api.post.dto.TripDate;
import codeit.api.post.service.PostService;
import codeit.domain.chat.constant.ChatType;
import codeit.domain.history.entity.History;
import codeit.domain.history.repository.HistoryRedisRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static codeit.domain.post.constant.Status.OPEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {
    @Mock
    private HistoryRedisRepository historyRedisRepository;

    @Mock
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private HistoryService historyService;
    static User userA = User.builder()
            .email("userA@google.com")
            .nickname("a")
            .role(Role.ROLE_USER)
            .password("encoded")
            .build();

    static User userC;

    @BeforeAll
    static void init() {
        userC = mock(User.class);
        given(userC.getId()).willReturn(1L);
        given(userC.getNickname()).willReturn("a");
    }

    @Test
    @DisplayName("조회 내역 저장 성공-히스토리가 이미 존재할때")
    void saveHistory_success_WhenHistoryIsAlreadyExisting() {
        //given
        List<Long> postIds = new ArrayList<>();
        postIds.addAll(List.of(1L, 2L, 3L));
        History history = History.builder()
                .userId(1L)
                .postIds(postIds)
                .build();
        given(historyRedisRepository.findById(anyLong()))
                .willReturn(Optional.of(history));
        given(postRepository.findByIdIn(anyList()))
                .willReturn(new ArrayList<>());
        //when
        historyService.saveHistory(1L, 4L);
        //then
        assertEquals(history.getPostIds().size(), 1);
        assertEquals(history.getPostIds().get(0), 4L);
    }

    @Test
    @DisplayName("조회 내역 저장 성공-히스토리가 없을때")
    void saveHistory_success_WhenHistoryIsNotExisting() {
        //given
        given(historyRedisRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        historyService.saveHistory(1L, 4L);
        ArgumentCaptor<History> historyCaptor = ArgumentCaptor.forClass(History.class);
        //then
        verify(historyRedisRepository, times(1)).save(historyCaptor.capture());
        assertEquals(historyCaptor.getValue().getPostIds().size(), 1);
        assertEquals(historyCaptor.getValue().getPostIds().get(0), 4L);
    }

    @Test
    @DisplayName("최근 읽은 포스트 조회 성공")
    void retrieveHistory_success() {
        //given
        History history = History.builder()
                .userId(1L)
                .postIds(List.of(1L, 2L, 3L))
                .build();
        given(historyRedisRepository.findById(anyLong()))
                .willReturn(Optional.of(history));
        given(postService.retrievePostsByPostId(any(), anyList()))
                .willReturn(List.of(
                        new PostItem(1L, "저랑 같이", "사과", "서울", TripDate.builder()
                                .endDate(LocalDate.MAX)
                                .startDate(LocalDate.EPOCH)
                                .build(), (short) 4, "FEMALE", "서울투어하실분", "OPEN", List.of("서울", "혜화"), null, 1L,  1L, false)
                ));
        //when
        List<PostItem> response = historyService.retrieveHistory(userC);
        //then
        assertEquals(response.size(), 1);
    }


}