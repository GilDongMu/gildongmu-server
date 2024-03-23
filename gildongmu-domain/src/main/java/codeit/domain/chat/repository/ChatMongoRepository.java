package codeit.domain.chat.repository;

import codeit.domain.chat.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMongoRepository extends MongoRepository<Chat, String> {

}
