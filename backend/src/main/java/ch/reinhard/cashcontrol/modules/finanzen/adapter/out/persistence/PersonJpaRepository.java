package ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonJpaRepository extends JpaRepository<PersonEntity, String> {
}
