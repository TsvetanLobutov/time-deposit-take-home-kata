package org.ikigaidigital.persistence;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeDepositJpaRepository extends JpaRepository<TimeDepositEntity, Integer> {
    @Override
    @EntityGraph(attributePaths = "withdrawals")
    List<TimeDepositEntity> findAll();
}
