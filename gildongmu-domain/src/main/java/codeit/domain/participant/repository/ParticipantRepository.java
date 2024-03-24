package codeit.domain.participant.repository;

import codeit.domain.participant.entity.Participant;
import codeit.domain.post.entity.Post;
import codeit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    boolean existsByUserAndPost(User user, Post post);

    Optional<Participant> findByUserIdAndPostId(Long userId, Long postId);
}