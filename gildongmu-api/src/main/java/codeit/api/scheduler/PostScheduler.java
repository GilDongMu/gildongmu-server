package codeit.api.scheduler;

import codeit.domain.post.constant.Status;
import codeit.domain.post.entity.Post;
import codeit.domain.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostScheduler {

    private final PostRepository postRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updatePostStatus() {
        LocalDate today = LocalDate.now();

        List<Post> postList = postRepository.findAllByStartDateBeforeAndStatus(today, Status.OPEN);

        postList.forEach(post -> post.updateStatus(Status.CLOSED));
        postRepository.saveAll(postList);
    }


}
