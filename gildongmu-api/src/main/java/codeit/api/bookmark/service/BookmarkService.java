package codeit.api.bookmark.service;

import codeit.api.bookmark.exception.BookmarkException;
import codeit.api.post.dto.PostItem;
import codeit.api.post.dto.TripDate;
import codeit.api.post.service.TagService;
import codeit.domain.bookmark.entity.Bookmark;
import codeit.domain.bookmark.repository.BookmarkRepository;
import codeit.domain.post.entity.Post;
import codeit.domain.post.repository.PostRepository;
import codeit.domain.tag.entity.Tag;
import codeit.domain.user.entity.User;
import codeit.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static codeit.api.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagService tagService;

    public void createBookmark(String email, Long postId) {
        User user = getUserByEmail(email);
        Post post = getPostById(postId);

        validateBookmarkNotExist(user, post);

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .post(post)
                .build();

        bookmarkRepository.save(bookmark);

        postRepository.save(post);
    }

    public void deleteBookmark(String email, Long postId) {
        User user = getUserByEmail(email);
        Post post = getPostById(postId);

        validateBookmarkExist(user, post);

        bookmarkRepository.deleteByUserAndPost(user, post);

        postRepository.save(post);
    }

    public List<PostItem> findBookmarks(String email) {
        User user = getUserByEmail(email);

        List<Bookmark> bookmarks = bookmarkRepository.findByUser(user);

        return bookmarks.stream()
                .map(Bookmark::getPost)
                .map(this::mapToPostListItem)
                .collect(Collectors.toList());

    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BookmarkException(USER_NOT_FOUND));
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new BookmarkException(POST_NOT_FOUND));
    }

    private void validateBookmarkNotExist(User user, Post post) {
        if (bookmarkRepository.existsByUserAndPost(user, post)) {
            throw new BookmarkException(ALREADY_BOOKMARK);
        }
    }

    private void validateBookmarkExist(User user, Post post) {
        if (!bookmarkRepository.existsByUserAndPost(user, post)) {
            throw new BookmarkException(BOOKMARK_NOT_FOUND);
        }
    }

    private PostItem mapToPostListItem(Post post) {
        List<Tag> tags = tagService.findTagListByPost(post);
        List<String> tagList = tags.stream()
                .map(Tag::getTagName)
                .collect(Collectors.toList());

        return new PostItem(
                post.getId(),
                post.getTitle(),
                post.getUser().getNickname(),
                post.getDestination(),
                TripDate.of(post.getStartDate(), post.getEndDate()),
                post.getParticipants(),
                post.getMemberGender().toString(),
                post.getContent(),
                post.getStatus().getCode(),
                tagList,
                post.getThumbnail(),
                (long) post.getComments().size(),
                (long) post.getBookmarks().size()
        );
    }
}
