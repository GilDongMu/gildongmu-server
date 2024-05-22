package codeit.domain.history.repository;

import codeit.domain.history.entity.History;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRedisRepository extends CrudRepository<History, Long> {
}
