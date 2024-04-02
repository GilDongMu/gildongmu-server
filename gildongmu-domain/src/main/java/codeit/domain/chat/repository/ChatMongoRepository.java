package codeit.domain.chat.repository;

import codeit.domain.chat.entity.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface ChatMongoRepository extends MongoRepository<Chat, String> {
    Slice<Chat> findByRoomIdOrderByCreatedAtDesc(Long roomId, Pageable pageable);
    List<Chat> findBySenderUserId(Long userId);
}
