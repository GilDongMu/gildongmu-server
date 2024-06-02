package codeit.api.comment.dto.response;

import java.util.List;

public record CommentListResponse(
        Long id,
        String nickname,
        String profilePath,
        boolean isMalicious,
        String content,
        boolean secret,
        boolean owner,
        List<CommentListResponse> children
) {
}
