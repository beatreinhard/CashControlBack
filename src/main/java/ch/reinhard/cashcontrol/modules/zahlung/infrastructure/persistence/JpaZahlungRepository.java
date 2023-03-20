package ch.reinhard.cashcontrol.modules.zahlung.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaZahlungRepository extends JpaRepository<ZahlungEntity, String> {
}
