package ch.reinhard.cashcontrol.modules.finanzen.application.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaKategorieRepository extends JpaRepository<KategorieEntity, String> {}
