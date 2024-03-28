package codeit.api.participant.service;

import codeit.api.exception.ErrorCode;
import codeit.api.participant.dto.ParticipantResponse;
import codeit.api.participant.exception.ParticipantException;
import codeit.api.post.exception.PostException;
import codeit.domain.participant.constant.Status;
import codeit.domain.participant.entity.Participant;
import codeit.domain.participant.repository.ParticipantRepository;
import codeit.domain.post.entity.Post;
import codeit.domain.post.repository.PostRepository;
import codeit.domain.room.entity.Room;
import codeit.domain.room.repository.RoomRepository;
import codeit.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static codeit.domain.post.constant.Status.*;
import static codeit.domain.post.constant.Status.OPEN;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final PostRepository postRepository;
    private final RoomRepository roomRepository;

    public void applyForParticipant(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
        if (participantRepository.existsByUserAndPost(user, post))
            throw new ParticipantException(ErrorCode.ALREADY_REGISTERED_PARTICIPANT);

        participantRepository.save(Participant.builder()
                .isLeader(false)
                .post(post)
                .status(Status.PENDING)
                .user(user)
                .build());
    }

    @Transactional
    public void exitParticipant(Long postId, User user) {
        Participant participant = participantRepository.findByUserIdAndPostIdAndStatusIsNot(user.getId(), postId, Status.DELETED)
                .orElseThrow(() -> new ParticipantException(ErrorCode.PARTICIPANT_NOT_FOUND));
        Status participantBeforeExit = participant.getStatus();
        participant.delete();
        roomRepository.findByPostId(postId)
                .ifPresent(room -> {
                    if(Status.ACCEPTED.equals(participantBeforeExit))
                        room.minusHeadCount();});
    }

    public void saveLeader(Post post, User user) {
        participantRepository.save(Participant.builder()
                .isLeader(true)
                .post(post)
                .status(Status.ACCEPTED)
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
        Post post = postRepository.findByIdAndStatus(postId, OPEN)
                .filter(p -> Objects.equals(p.getUser().getId(), user.getId())).stream().findFirst()
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));

        Participant participantToBeAccepted = participantRepository.findById(participantId)
                .stream().filter(participant -> Objects.equals(participant.getPost().getId(), postId)
                        && Objects.equals(participant.getStatus(), Status.PENDING))
                .findFirst().orElseThrow(() -> new ParticipantException(ErrorCode.PARTICIPANT_NOT_FOUND));
        participantToBeAccepted.accept();

        handlingParticipantAcceptedEvent(post);
    }

    // TODO: extract to event listener
    public void handlingParticipantAcceptedEvent(Post post) {
        Room room = roomRepository.findByPost(post)
                .orElse(roomRepository.save(Room.builder()
                                .post(post)
                                .headcount(2)
                                .build()));
        room.plusHeadCount();
        if(room.getHeadcount() == post.getParticipants())
            post.updateStatus(CLOSED);
    }


    private void validateLeaderUser(Long userId, Long postId) {
        participantRepository.findByUserIdAndPostId(userId, postId)
                .stream().filter(Participant::isLeader).findFirst()
                .orElseThrow(() -> new ParticipantException(ErrorCode.NOT_LEADER_USER));
    }

    public List<ParticipantResponse> retrieveParticipants(Long postId, User user, String status) {
        if (Status.PENDING.name().equals(status))
            return retrievePendingParticipants(postId, user);
        return retrieveAcceptedParticipants(postId, user);
    }


    private List<ParticipantResponse> retrievePendingParticipants(Long postId, User user) {
        validateLeaderUser(user.getId(), postId);
        return participantRepository.findByPostIdAndStatus(postId, Status.PENDING)
                .stream().map(ParticipantResponse::from)
                .collect(Collectors.toList());
    }

    private List<ParticipantResponse> retrieveAcceptedParticipants(Long postId, User user) {
        validateParticipantUser(user.getId(), postId);
        return participantRepository.findByPostIdAndStatusOrPostIdAndUser(postId, Status.ACCEPTED, postId, user)
                .stream().map(participant -> ParticipantResponse.from(participant, user.getId()))
                .sorted((o1, o2) -> {
                    if (o1.user().isCurrentUser() == o2.user().isCurrentUser())
                        return Boolean.compare(o2.isLeader(), o1.isLeader());
                    return Boolean.compare(o2.user().isCurrentUser(), o1.user().isCurrentUser());
                })
                .collect(Collectors.toList());
    }

    private void validateParticipantUser(Long userId, Long postId) {
        if (!participantRepository.existsByUserIdAndPostIdAndStatusIsNot(userId, postId, Status.DELETED))
            throw new ParticipantException(ErrorCode.NOT_PARTICIPANT_USER);
    }


}
