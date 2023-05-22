package ch.reinhard.cashcontrol.modules.zahlung.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface JpaZahlungViewRepository extends JpaRepository<ZahlungView, String>, QuerydslPredicateExecutor<ZahlungView> {
}
