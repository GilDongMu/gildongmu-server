package codeit.api.post.service;

import codeit.api.bookmark.service.BookmarkService;
import codeit.api.participant.service.ParticipantService;
import codeit.api.post.dto.PostItem;
import codeit.api.post.dto.TripDate;
import codeit.api.post.dto.event.PostHitEvent;
import codeit.api.post.dto.request.ImageCreateRequest;
import codeit.api.post.dto.request.PostCreateRequest;
import codeit.api.post.dto.request.PostUpdateRequest;
import codeit.api.post.dto.request.RetrievingType;
import codeit.api.post.dto.response.PostListResponse;
import codeit.api.post.dto.response.PostResponse;
import codeit.api.post.dto.response.PostSummaryResponse;
import codeit.api.post.exception.PostException;
import codeit.api.security.UserPrincipal;
import codeit.common.client.S3Client;
import codeit.domain.Image.Repository.ImageRepository;
import codeit.domain.Image.entity.Image;
import codeit.domain.bookmark.entity.Bookmark;
import codeit.domain.bookmark.repository.BookmarkRepository;
import codeit.domain.history.entity.History;
import codeit.domain.post.constant.MemberGender;
import codeit.domain.post.constant.Status;
import codeit.domain.post.entity.Post;
import codeit.domain.post.repository.PostRepository;
import codeit.domain.room.entity.Room;
import codeit.domain.room.repository.RoomRepository;
import codeit.domain.tag.entity.Tag;
import codeit.domain.user.entity.User;
import codeit.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.net.URL;
import java.util.Optional;
import javax.swing.text.html.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

import static codeit.api.exception.ErrorCode.POST_NOT_FOUND;
import static codeit.api.exception.ErrorCode.USER_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookmarkRepository bookmarkRepository;
    private final TagService tagService;
    private final ImageService imageService;
    private final ParticipantService participantService;
    private final S3Client s3Client;
    private final ApplicationEventPublisher applicationEventPublisher;

    public PostListResponse findPosts(String keyword, String postFilter, String postSort, Pageable pageable, UserPrincipal auth) {
        Page<Post> postPage = postRepository.findFilteredAndSortedPosts(keyword, postFilter, postSort, pageable);

        User user = Optional.ofNullable(auth)
            .map(UserPrincipal::getUser)
            .orElse(null);

        List<PostItem> postListItems = postPage.getContent().stream()
                .map(post -> mapToPostListItem(post, user))
                .collect(Collectors.toList());

        return new PostListResponse(
                postListItems,
                postPage.getPageable(),
                postPage.isFirst(),
                postPage.isLast(),
                postPage.getSize(),
                postPage.getNumber(),
                postPage.getSort(),
                postPage.getNumberOfElements(),
                postPage.isEmpty(),
                postPage.getTotalPages()
        );
    }

    private PostItem mapToPostListItem(Post post, User user) {
        List<Tag> tags = tagService.findTagListByPost(post);
        List<String> tagList = tags.stream()
                .map(Tag::getTagName)
                .collect(Collectors.toList());

        boolean myBookmark = checkBookmarkedByUser(user, post);

        long countOfBookmarks =
                post.getBookmarks() != null ? post.getBookmarks().size() : 0;

        return new PostItem(
                post.getId(),
                post.getTitle(),
                post.getUser().getNickname(),
                post.getUser().isMalicious(),
                post.getDestination(),
                TripDate.of(post.getStartDate(), post.getEndDate()),
                post.getParticipants(),
                post.getMemberGender().toString(),
                post.getContent(),
                post.getStatus().getCode(),
                tagList,
                post.getThumbnail(),
                (long) post.getComments().size(),
                countOfBookmarks,
                myBookmark
        );
    }

    private boolean checkBookmarkedByUser(User user, Post post) {
        return bookmarkRepository.existsByUserAndPost(user, post);
    }

    public PostResponse findPost(Long postId, Optional<UserPrincipal> optionalUserPrincipal) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        List<Tag> tag = tagService.findTagListByPost(post);

        optionalUserPrincipal.ifPresent(principal -> applicationEventPublisher.publishEvent(PostHitEvent.of(principal.getUser().getId(), postId)));

        return PostResponse.from(post, tag, post.getImages());
    }

    public PostResponse findPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        List<Tag> tag = tagService.findTagListByPost(post);

        return PostResponse.from(post, tag, post.getImages());
    }

    public void createPost(PostCreateRequest postRequest, List<MultipartFile> images, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new PostException(USER_NOT_FOUND));

        Post post = Post.builder()
                .user(user)
                .title(postRequest.title())
                .content(postRequest.content())
                .destination(postRequest.destination())
                .startDate(postRequest.tripDate().startDate())
                .endDate(postRequest.tripDate().endDate())
                .memberGender(MemberGender.valueOf(postRequest.gender()))
                .participants(postRequest.numberOfPeople())
                .status(Status.OPEN)
                .build();

        List<Image> updatedImages = null;
        if (images != null) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile image : images) {
                String imageUrl = s3Client.upload(image);
                imageUrls.add(imageUrl);
            }

            updatedImages = imageService.saveImages(imageUrls, post);
            String thumbnail = updatedImages.get(0).getUrl();
            post.updateThumbnail(thumbnail);
        }

        postRepository.save(post);
        tagService.saveTag(post, postRequest.tag());
        participantService.saveLeader(post, user);
    }

    public PostResponse updatePost(Long postId, List<MultipartFile> images, PostUpdateRequest postUpdateRequest) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        post.updateTitle(postUpdateRequest.title());
        post.updateContent(postUpdateRequest.content());
        post.updateDestination(postUpdateRequest.destination());
        post.updateStartDate(postUpdateRequest.tripDate().startDate());
        post.updateEndDate(postUpdateRequest.tripDate().endDate());
        post.updateGender(MemberGender.valueOf(postUpdateRequest.gender()));
        post.updateParticipants(postUpdateRequest.numberOfPeople());

        List<Image> existImages = imageService.findAllByPostId(postId);
        imageService.deleteAllImagesFromS3(existImages);

        List<Long> ids = imageService.findAllId(postId);
        imageService.deleteAllImagesFromDB(ids);


        List<Image> updatedImages = null;
        if (images != null) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile image : images) {
                String imageUrl = s3Client.upload(image);
                imageUrls.add(imageUrl);
            }
            updatedImages = imageService.saveImages(imageUrls, post);
            String thumbnail = updatedImages.get(0).getUrl();
            post.updateThumbnail(thumbnail);
        }

        tagService.deleteTag(post);
        tagService.saveTag(post, postUpdateRequest.tag());
        postRepository.save(post);

        return PostResponse.from(post, tagService.findTagListByPost(post), updatedImages);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        List<Image> images = imageService.findAllByPostId(id);
        imageService.deleteAllImagesFromS3(images);

        tagService.deleteTag(post);
        postRepository.delete(post);
    }

    public Slice<PostItem> retrieveMyPosts(User user, String type, Pageable pageable) {
        if (RetrievingType.LEADER.name().equals(type))
            return postRepository.findByUserOrderByStatusDesc(user, pageable)
                    .map(post -> mapToPostListItem(post, user));
        return postRepository.findByParticipantUserOrderByStatusDesc(user.getId(), pageable)
                .map(post -> mapToPostListItem(post, user));
    }

    public PostSummaryResponse retrievePostSummary(User user, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
        int numberOfAccepted = roomRepository.findByPost(post)
                .map(Room::getHeadcount)
                .orElseGet(() -> 1);
        return PostSummaryResponse.from(post, numberOfAccepted, user.getId());
    }

    public List<PostItem> retrievePostsByPostId(User user, List<Long> postIds){
        return postRepository.findByIdIn(postIds)
                .stream().map(p -> mapToPostListItem(p, user))
                .collect(Collectors.toList());
    }

}
