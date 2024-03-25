package codeit.api.post.service;

import codeit.api.participant.service.ParticipantService;
import codeit.api.post.dto.TripDate;
import codeit.api.post.dto.request.ImageCreateRequest;
import codeit.api.post.dto.request.PostCreateRequest;
import codeit.api.post.dto.request.PostUpdateRequest;
import codeit.api.post.dto.response.PostListResponse;
import codeit.api.post.dto.response.PostListResponse.PostListItemResponse;
import codeit.api.post.dto.response.PostResponse;
import codeit.api.post.exception.PostException;
import codeit.domain.Image.Repository.ImageRepository;
import codeit.domain.Image.entity.Image;
import codeit.domain.post.constant.MemberGender;
import codeit.domain.post.constant.Status;
import codeit.domain.post.entity.Post;
import codeit.domain.post.repository.PostRepository;
import codeit.domain.tag.entity.Tag;
import codeit.domain.user.entity.User;
import codeit.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static codeit.api.exception.ErrorCode.POST_NOT_FOUND;
import static codeit.api.exception.ErrorCode.USER_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final TagService tagService;
    private final ParticipantService participantService;

    public PostListResponse findPosts(String postFilter, Pageable pageable) {
        Specification<Post> specification = getFilter(postFilter);

        Page<Post> postPage;

        if (specification != null) {
            postPage = postRepository.findAll(specification, pageable);
        } else {
            postPage = postRepository.findAll(pageable);
        }

        List<PostListItemResponse> postListItems = postPage.getContent().stream()
            .map(this::mapToPostListItem)
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
            postPage.isEmpty()
        );
    }

    private PostListItemResponse mapToPostListItem(Post post) {
        List<Tag> tags = tagService.findTagListByPost(post);
        List<String> tagList = tags.stream()
            .map(Tag::getTagName)
            .collect(Collectors.toList());

        return new PostListItemResponse(
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
            post.getBookmarkCount()
        );
    }

    public Sort getSort(String postSort) {
        switch (postSort) {
            case "popular":
                return Sort.by(Direction.DESC, "countOfBookmarks");

            case "comment":
                return Sort.by(Direction.DESC, "countOfComments");

            case "latest-trip":
                return Sort.by(Direction.DESC, "startDate");

            default:
                return Sort.by(Direction.DESC, "updatedAt");
        }
    }

    private Specification<Post> getFilter(String postFilter) {
        Specification<Post> specification = Specification.where(null);
        if (postFilter != null && !postFilter.isEmpty()) {
            switch (postFilter) {
                case "female":
                    specification = specification.and((root, query, builder) -> builder.equal(root.get("memberGender"), "FEMALE"));
                    break;
                case "male":
                    specification = specification.and((root, query, builder) -> builder.equal(root.get("memberGender"), "MALE"));
                    break;
                case "none":
                    specification = specification.and((root, query, builder) -> builder.equal(root.get("memberGender"), "NONE"));
                    break;
                case "open":
                    specification = specification.and((root, query, builder) -> builder.equal(root.get("status"), "OPEN"));
                    break;
                case "close":
                    specification = specification.and((root, query, builder) -> builder.equal(root.get("status"), "CLOSE"));
                    break;

            }
            return specification;
        }
        return null;
    }

    public PostResponse findPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        List<Tag> tag = tagService.findTagListByPost(post);

        return PostResponse.from(post, tag, post.getImages());
    }

    public void createPost(PostCreateRequest postRequest, String email) {
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

        //TODO => ImageService 분리
        List<ImageCreateRequest> images = postRequest.images();

        for (ImageCreateRequest imageCreateRequest : images) {
            Image image = Image.builder()
                .url(imageCreateRequest.url())
                .post(post)
                .build();

            if (imageCreateRequest.thumbnail())
                post.add(image.getUrl());

            imageRepository.save(image);
        }

        postRepository.save(post);
        tagService.saveTag(post, postRequest.tag());
        participantService.saveLeader(post, user);
    }

    public PostResponse updatePost(Long postId, PostUpdateRequest postUpdateRequest) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        post.updateTitle(postUpdateRequest.title());
        post.updateContent(postUpdateRequest.content());
        post.updateDestination(postUpdateRequest.destination());
        post.updateStartDate(postUpdateRequest.tripDate().startDate());
        post.updateEndDate(postUpdateRequest.tripDate().endDate());
        post.updateGender(MemberGender.valueOf(postUpdateRequest.gender()));
        post.updateParticipants(postUpdateRequest.numberOfPeople());

        //TODO ==> ImageService 분리
        List<Image> existImages = post.getImages();
        for (Image existImage : existImages) {
            imageRepository.delete(existImage);
        }
        existImages.clear();

        List<ImageCreateRequest> images = postUpdateRequest.images();
        List<Image> updatedImages = new ArrayList<>();
        for (ImageCreateRequest imageCreateRequest : images) {
            Image image = Image.builder()
                .url(imageCreateRequest.url())
                .post(post)
                .build();
            updatedImages.add(image);
            imageRepository.save(image);

            if (imageCreateRequest.thumbnail())
                post.updateThumbnail(image.getUrl());
        }
        post.updateImages(updatedImages);


        tagService.deleteTag(post);
        tagService.saveTag(post, postUpdateRequest.tag());
        postRepository.save(post);

        return PostResponse.from(post, tagService.findTagListByPost(post), updatedImages);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        tagService.deleteTag(post);
        postRepository.delete(post);
    }

}
