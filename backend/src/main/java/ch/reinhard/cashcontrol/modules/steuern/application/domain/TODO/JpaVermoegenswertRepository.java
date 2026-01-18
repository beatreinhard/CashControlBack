package ch.reinhard.cashcontrol.modules.steuern.application.domain.TODO;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.Vermoegenswert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaVermoegenswertRepository
        extends JpaRepository<Vermoegenswert, String>, QuerydslPredicateExecutor<Vermoegenswert> {}
