package ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAusgabeRepository
        extends JpaRepository<AusgabeEntity, String>, QuerydslPredicateExecutor<AusgabeEntity> {}
