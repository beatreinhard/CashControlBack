package ch.reinhard.cashcontrol.modules.steuern.application.domain.TODO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaKostenRepository extends JpaRepository<Kosten, String>, QuerydslPredicateExecutor<Kosten> {
    List<Kosten> findKostenByJahr(Integer jahr);
}
