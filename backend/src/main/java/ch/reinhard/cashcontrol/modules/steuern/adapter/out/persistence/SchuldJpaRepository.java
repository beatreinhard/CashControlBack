package ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SchuldJpaRepository extends JpaRepository<SchuldEntity, String>, QuerydslPredicateExecutor<SchuldEntity> {}
