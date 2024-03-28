package codeit.domain.room.repository;

import codeit.domain.post.entity.Post;
import codeit.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByPost(Post post);
    Optional<Room> findByPostId(Long postId);
}
