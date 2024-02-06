package ch.reinhard.cashcontrol.modules.steuern.application.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaKostenRepository extends JpaRepository<Kosten, String>, QuerydslPredicateExecutor<Kosten> {}
