package codeit.api.participant;

import codeit.api.exception.ErrorCode;
import codeit.api.post.exception.PostException;
import codeit.domain.participant.constant.ParticipantStatus;
import codeit.domain.participant.entity.Participant;
import codeit.domain.participant.repository.ParticipantRepository;
import codeit.domain.post.entity.Post;
import codeit.domain.post.repository.PostRepository;
import codeit.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final PostRepository postRepository;

    public void applyForParticipant(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
        if (participantRepository.existsByUserAndPost(user, post))
            throw new ParticipantException(ErrorCode.ALREADY_REGISTERED_PARTICIPANT);

        participantRepository.save(Participant.builder()
                .isLeader(false)
                .post(post)
                .status(ParticipantStatus.PENDING)
                .user(user)
                .build());
    }

    @Transactional
    public void exitParticipant(Long postId, User user) {
        Participant participant = participantRepository.findByUserIdAndPostId(user.getId(), postId)
                .orElseThrow(() -> new ParticipantException(ErrorCode.PARTICIPANT_NOT_FOUND));
        participant.delete();
    }
}
