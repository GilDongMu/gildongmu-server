package codeit.api.history.service;

import codeit.api.post.dto.PostItem;
import codeit.api.post.service.PostService;
import codeit.domain.history.entity.History;
import codeit.domain.history.repository.HistoryRedisRepository;
import codeit.domain.post.entity.Post;
import codeit.domain.post.repository.PostRepository;
import codeit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRedisRepository historyRedisRepository;
    private final PostService postService;
    private final PostRepository postRepository;

    public void saveHistory(Long userId, Long postId) {
        History history = historyRedisRepository.findById(userId)
                .orElse(History.builder()
                        .userId(userId)
                        .build());

        removeDeletedPostIdsHistory(history);
        history.addPostId(postId);
        historyRedisRepository.save(history);
    }

    private void removeDeletedPostIdsHistory(History history) {
        if(history.getPostIds().isEmpty())
            return;
        history.removeDeletedPostIds(postRepository.findByIdIn(history.getPostIds())
                .stream().map(Post::getId)
                .collect(Collectors.toList()));
    }

    public List<PostItem> retrieveHistory(User user) {
        Optional<History> history = historyRedisRepository.findById(user.getId());
        if (history.isEmpty())
            return new ArrayList<>();
        return postService.retrievePostsByPostId(user, history.get().getPostIds());
    }

}
