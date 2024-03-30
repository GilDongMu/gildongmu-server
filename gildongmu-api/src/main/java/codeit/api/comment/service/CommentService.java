package codeit.api.comment.service;

import static codeit.api.exception.ErrorCode.COMMENT_NOT_FOUND;
import static codeit.api.exception.ErrorCode.COMMENT_POST_NOT_FOUND;
import static codeit.api.exception.ErrorCode.COMMENT_USER_NOT_FOUND;
import static codeit.api.exception.ErrorCode.POST_NOT_FOUND;
import static codeit.api.exception.ErrorCode.USER_NOT_FOUND;

import codeit.api.comment.dto.request.CommentCreateRequest;
import codeit.api.comment.dto.request.CommentUpdateRequest;
import codeit.api.comment.dto.response.CommentListResponse;
import codeit.api.comment.dto.response.CommentUpdateResponse;
import codeit.api.comment.exception.CommentException;
import codeit.api.post.exception.PostException;
import codeit.api.user.exception.UserException;
import codeit.domain.comment.entity.Comment;
import codeit.domain.comment.repository.CommentRepository;
import codeit.domain.post.entity.Post;
import codeit.domain.post.repository.PostRepository;
import codeit.domain.user.entity.User;
import codeit.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createComment(CommentCreateRequest commentRequest, String email, Long postId) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserException(USER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(POST_NOT_FOUND));

        Comment parentComment = null;
        if (commentRequest.parentId() != null) {
            parentComment = commentRepository.findById(commentRequest.parentId())
                .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));
        }

        Comment comment = Comment.builder()
            .content(commentRequest.content())
            .secret(commentRequest.secret())
            .user(user)
            .post(post)
            .parent(parentComment)
            .build();

        commentRepository.save(comment);
    }

    public List<CommentListResponse> findAllComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(POST_NOT_FOUND));

        List<Comment> comments = commentRepository.findAllByPost(post);
        List<CommentListResponse> commentListResponses = new ArrayList<>();
        Map<Long, CommentListResponse> map = new HashMap<>();

        comments.stream().forEach(comment -> {
            CommentListResponse commentList = mapToCommentListResponse(comment);
            map.put(commentList.id(), commentList);
            if (comment.getParent() != null) map.get(comment.getParent().getId()).children().add(commentList);
            else commentListResponses.add(commentList);
        });

        return commentListResponses;
    }

    @Transactional
    public CommentUpdateResponse updateComment(Long postId, Long commentId, String email, CommentUpdateRequest commentRequest) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CommentException(POST_NOT_FOUND));

        if (!comment.getPost().getId().equals(postId)) {
            throw new CommentException(COMMENT_POST_NOT_FOUND);
        }

        if (!comment.getUser().getEmail().equals(email)) {
            throw new CommentException(COMMENT_USER_NOT_FOUND);
        }

        comment.updateContent(commentRequest.content());
        comment.updateSecret(commentRequest.secret());

        Comment updatedComment = commentRepository.save(comment);

        return CommentUpdateResponse.from(updatedComment);
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, String email) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CommentException(POST_NOT_FOUND));

        if (!comment.getUser().getEmail().equals(email)) {
            throw new CommentException(COMMENT_USER_NOT_FOUND);
        }

        commentRepository.delete(comment);
    }

    private CommentListResponse mapToCommentListResponse(Comment comment) {
        Boolean isOwner = comment.getUser().equals(comment.getPost().getUser());

        return new CommentListResponse(
            comment.getId(),
            comment.getUser().getNickname(),
            comment.getContent(),
            comment.isSecret(),
            isOwner,
            new ArrayList<>()
        );
    }
}
