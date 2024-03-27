package codeit.api.post.service;

import codeit.api.participant.service.ParticipantService;
import codeit.api.post.dto.PostItem;
import codeit.api.post.dto.response.PostListResponse;
import codeit.domain.Image.Repository.ImageRepository;
import codeit.domain.post.constant.MemberGender;
import codeit.domain.post.constant.Status;
import codeit.domain.post.entity.Post;
import codeit.domain.post.repository.PostRepository;
import codeit.domain.tag.entity.Tag;
import codeit.domain.user.constant.Role;
import codeit.domain.user.entity.User;
import codeit.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private TagService tagService;
    @Mock
    private ParticipantService participantService;
    @InjectMocks
    private PostService postService;

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
    @DisplayName("내 여행 글 조회 성공-모집중")
    void retrieveMyPostsTest_success_WhenRetrievingByLeader() {
        //given
        given(postRepository.findByUserOrderByStatusDesc(any(), any()))
                .willReturn(new SliceImpl<>(List.of(
                        Post.builder()
                                .title("도쿄 같이가실분")
                                .user(userB)
                                .content("도쿄 같이 가실 분 구해요~")
                                .destination("일본, 도쿄")
                                .memberGender(MemberGender.FEMALE)
                                .startDate(LocalDate.MIN)
                                .status(Status.OPEN)
                                .endDate(LocalDate.MAX)
                                .comments(new ArrayList<>())
                                .build(),
                        Post.builder()
                                .title("제주도 같이가실분")
                                .user(userB)
                                .content("제주도 같이 가실 분 구해요~")
                                .destination("한국, 제주특별자치도")
                                .memberGender(MemberGender.FEMALE)
                                .startDate(LocalDate.MIN)
                                .endDate(LocalDate.MAX)
                                .status(Status.CLOSED)
                                .comments(new ArrayList<>())
                                .build()
                )));
        given(tagService.findTagListByPost(any()))
                .willReturn(List.of(Tag.builder()
                                .tagName("여행")
                        .build()));
        //when
        Slice<PostItem> responses = postService.retrieveMyPosts(userB, "LEADER", Pageable.ofSize(3));
        //then
        assertEquals(responses.getContent().get(0).destination(), "일본, 도쿄");
        assertEquals(responses.getContent().get(0).status(), Status.OPEN.getCode());
        assertEquals(responses.getContent().get(0).tag().get(0), "여행");
        assertEquals(responses.getContent().get(1).destination(), "한국, 제주특별자치도");
        assertEquals(responses.getContent().get(1).status(), Status.CLOSED.getCode());
    }

    @Test
    @DisplayName("내 여행 글 조회 성공-참여중")
    void retrieveMyPostsTest_success_WhenRetrievingByParticipant() {
        //given
        given(postRepository.findByParticipantUserOrderByStatusDesc(anyLong(), any()))
                .willReturn(new SliceImpl<>(List.of(
                        Post.builder()
                                .title("도쿄 같이가실분")
                                .user(userB)
                                .content("도쿄 같이 가실 분 구해요~")
                                .destination("일본, 도쿄")
                                .memberGender(MemberGender.FEMALE)
                                .startDate(LocalDate.MIN)
                                .status(Status.OPEN)
                                .endDate(LocalDate.MAX)
                                .comments(new ArrayList<>())
                                .build(),
                        Post.builder()
                                .title("제주도 같이가실분")
                                .user(userB)
                                .content("제주도 같이 가실 분 구해요~")
                                .destination("한국, 제주특별자치도")
                                .memberGender(MemberGender.FEMALE)
                                .startDate(LocalDate.MIN)
                                .endDate(LocalDate.MAX)
                                .status(Status.CLOSED)
                                .comments(new ArrayList<>())
                                .build()
                )));
        given(tagService.findTagListByPost(any()))
                .willReturn(List.of(Tag.builder()
                        .tagName("여행")
                        .build()));
        //when
        Slice<PostItem> responses = postService.retrieveMyPosts(userA, "NONE", Pageable.ofSize(3));
        //then
        assertEquals(responses.getContent().get(0).destination(), "일본, 도쿄");
        assertEquals(responses.getContent().get(0).status(), Status.OPEN.getCode());
        assertEquals(responses.getContent().get(0).tag().get(0), "여행");
        assertEquals(responses.getContent().get(1).destination(), "한국, 제주특별자치도");
        assertEquals(responses.getContent().get(1).status(), Status.CLOSED.getCode());
    }

}