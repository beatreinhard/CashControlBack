package ch.reinhard.cashcontrol.modules.steuern.application.domain.TODO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KostenJpaRepository extends JpaRepository<KostenEntity, String>, QuerydslPredicateExecutor<KostenEntity> {
    List<KostenEntity> findKostenByJahr(Integer jahr);
    KostenEntity getKostenByAusgabeId(String ausgabeId);
    void deleteKostenByAusgabeId(String ausgabeId);
}
