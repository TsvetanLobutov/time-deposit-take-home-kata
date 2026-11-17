package org.ikigaidigital.persistence;

import org.ikigaidigital.application.TimeDepositData;
import org.ikigaidigital.application.TimeDepositStore;
import org.ikigaidigital.application.WithdrawalData;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class JpaTimeDepositStore implements TimeDepositStore {
    private final TimeDepositJpaRepository repository;

    public JpaTimeDepositStore(TimeDepositJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeDepositData> findAll() {
        return repository.findAll().stream()
            .map(entity -> new TimeDepositData(
                entity.getId(),
                entity.getPlanType(),
                entity.getDays(),
                entity.getBalance(),
                entity.getWithdrawals().stream()
                    .map(withdrawal -> new WithdrawalData(
                        withdrawal.getId(),
                        withdrawal.getAmount(),
                        withdrawal.getDate()
                    ))
                    .toList()
            ))
            .toList();
    }

    @Override
    @Transactional
    public void updateBalances(List<TimeDepositData> deposits) {
        Map<Integer, TimeDepositData> updatedBalances = deposits.stream()
            .collect(Collectors.toMap(TimeDepositData::id, Function.identity()));

        List<TimeDepositEntity> entities = repository.findAllById(updatedBalances.keySet());
        entities.forEach(entity ->
            entity.setBalance(updatedBalances.get(entity.getId()).balance())
        );
        repository.saveAll(entities);
    }
}
