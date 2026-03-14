package ch.reinhard.cashcontrol.modules.steuern.application.domain.TODO;

import ch.reinhard.cashcontrol.core.persistence.ReadOnlyRepository;

import java.util.List;

public interface KostenViewJpaRepository extends ReadOnlyRepository<KostenView, String> {
    List<KostenView> findKostenByJahr(Integer jahr);
}
