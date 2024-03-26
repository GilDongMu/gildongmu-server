package codeit.api.participant.service;

import codeit.api.exception.ErrorCode;
import codeit.api.participant.dto.ParticipantResponse;
import codeit.api.participant.exception.ParticipantException;
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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        Participant participant = participantRepository.findByUserIdAndPostIdAndStatusIsNot(user.getId(), postId, ParticipantStatus.DELETED)
                .orElseThrow(() -> new ParticipantException(ErrorCode.PARTICIPANT_NOT_FOUND));
        participant.delete();
    }

    public void saveLeader(Post post, User user) {
        participantRepository.save(Participant.builder()
                .isLeader(true)
                .post(post)
                .status(ParticipantStatus.ACCEPTED)
                .user(user)
                .build());
    }

    @Transactional
    public void denyParticipant(Long postId, Long participantId, User user) {
        validateLeaderUser(user.getId(), postId);

        Participant participantToBeDeleted = participantRepository.findById(participantId)
                .stream().filter(participant -> Objects.equals(participant.getPost().getId(), postId))
                .findFirst().orElseThrow(() -> new ParticipantException(ErrorCode.PARTICIPANT_NOT_FOUND));

        participantToBeDeleted.delete();
    }

    @Transactional
    public void acceptParticipant(Long postId, Long participantId, User user) {
        validateLeaderUser(user.getId(), postId);

        Participant participantToBeAccepted = participantRepository.findById(participantId)
                .stream().filter(participant -> Objects.equals(participant.getPost().getId(), postId)
                        && Objects.equals(participant.getStatus(), ParticipantStatus.PENDING))
                .findFirst().orElseThrow(() -> new ParticipantException(ErrorCode.PARTICIPANT_NOT_FOUND));

        participantToBeAccepted.accept();
    }

    private void validateLeaderUser(Long userId, Long postId) {
        participantRepository.findByUserIdAndPostId(userId, postId)
                .stream().filter(Participant::isLeader).findFirst()
                .orElseThrow(() -> new ParticipantException(ErrorCode.NOT_LEADER_USER));
    }

    public List<ParticipantResponse> retrieveParticipants(Long postId, User user, String status) {
        if (ParticipantStatus.PENDING.name().equals(status))
            return retrievePendingParticipants(postId, user);
        return retrieveAcceptedParticipants(postId, user);
    }


    private List<ParticipantResponse> retrievePendingParticipants(Long postId, User user) {
        validateLeaderUser(user.getId(), postId);
        return participantRepository.findByPostIdAndStatus(postId, ParticipantStatus.PENDING)
                .stream().map(ParticipantResponse::from)
                .collect(Collectors.toList());
    }

    private List<ParticipantResponse> retrieveAcceptedParticipants(Long postId, User user) {
        validateAcceptedParticipant(user.getId(), postId);
        return participantRepository.findByPostIdAndStatus(postId, ParticipantStatus.ACCEPTED)
                .stream().map(participant -> ParticipantResponse.from(participant, user.getId()))
                .sorted((o1, o2) -> {
                    if (o1.user().isCurrentUser() == o2.user().isCurrentUser())
                        return Boolean.compare(o2.isLeader(), o1.isLeader());
                    return Boolean.compare(o2.user().isCurrentUser(), o1.user().isCurrentUser());
                })
                .collect(Collectors.toList());
    }

    private void validateAcceptedParticipant(Long userId, Long postId) {
        participantRepository.findByUserIdAndPostId(userId, postId)
                .stream().filter(Participant::isAccepted).findFirst()
                .orElseThrow(() -> new ParticipantException(ErrorCode.NOT_PARTICIPANT_USER));
    }
}
