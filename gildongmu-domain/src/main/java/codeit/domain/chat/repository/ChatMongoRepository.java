package codeit.domain.chat.repository;

import codeit.domain.chat.entity.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ChatMongoRepository extends MongoRepository<Chat, String> {
    Slice<Chat> findByRoomId(Long roomId, Pageable pageable);
}
