package ch.reinhard.cashcontrol.modules.zahlung.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaKategorieRepository extends JpaRepository<KategorieEntity, String> {
}
