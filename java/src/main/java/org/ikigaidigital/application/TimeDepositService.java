package org.ikigaidigital.application;

import org.ikigaidigital.TimeDeposit;
import org.ikigaidigital.TimeDepositCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class TimeDepositService {
    private final TimeDepositStore store;
    private final TimeDepositCalculator calculator;

    public TimeDepositService(TimeDepositStore store, TimeDepositCalculator calculator) {
        this.store = store;
        this.calculator = calculator;
    }

    @Transactional(readOnly = true)
    public List<TimeDepositData> findAll() {
        return store.findAll();
    }

    @Transactional
    public void updateBalances() {
        List<TimeDepositData> deposits = store.findAll();
        List<TimeDeposit> balances = deposits.stream()
            .map(deposit -> new TimeDeposit(
                deposit.id(),
                deposit.planType(),
                deposit.balance().doubleValue(),
                deposit.days()
            ))
            .toList();

        calculator.updateBalance(balances);

        List<TimeDepositData> updatedDeposits = IntStream.range(0, deposits.size())
            .mapToObj(index -> deposits.get(index).withBalance(
                BigDecimal.valueOf(balances.get(index).getBalance())
            ))
            .toList();
        store.updateBalances(updatedDeposits);
    }
}
