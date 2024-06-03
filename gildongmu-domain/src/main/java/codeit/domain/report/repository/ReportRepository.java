package codeit.domain.report.repository;

import codeit.domain.report.entity.Report;
import codeit.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByTargetAndComplainant(User target, User complaint);

    Set<Long> findComplainantIdByTargetAndDeletedAtIsNull(User target);

    @EntityGraph(attributePaths = {"target"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Report> findByDeletedAtIsNullOrderByCreatedAtDescTargetId();
}
