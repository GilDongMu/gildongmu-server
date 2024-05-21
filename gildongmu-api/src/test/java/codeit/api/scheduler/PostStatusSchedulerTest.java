package codeit.api.scheduler;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import codeit.domain.post.constant.MemberGender;
import codeit.domain.post.constant.Status;
import codeit.domain.post.entity.Post;
import codeit.domain.post.repository.PostRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostStatusSchedulerTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostScheduler postScheduler;

    static Post postA = Post.builder()
        .title("제목")
        .destination("서울")
        .memberGender(MemberGender.NONE)
        .startDate(LocalDate.now().minusDays(1))
        .build();

    static Post post = mock(Post.class);

    @Test
    public void testUpdatePostStatus() {
        //given
        LocalDate today = LocalDate.now();
        List<Post> posts = new ArrayList<>();
        posts.add(postA);
        //when
        when(postRepository.findAllByStartDateBeforeAndStatus(today, Status.OPEN)).thenReturn(posts);
        postScheduler.updatePostStatus();
        //then
        verify(postRepository, times(1)).findAllByStartDateBeforeAndStatus(today, Status.OPEN);
        assertEquals(Status.CLOSED, postA.getStatus());
    }
}
