package org.ikigaidigital.application;

import java.math.BigDecimal;
import java.util.List;

public record TimeDepositData(
    int id,
    String planType,
    int days,
    BigDecimal balance,
    List<WithdrawalData> withdrawals
) {
    public TimeDepositData {
        withdrawals = List.copyOf(withdrawals);
    }

    public TimeDepositData withBalance(BigDecimal newBalance) {
        return new TimeDepositData(id, planType, days, newBalance, withdrawals);
    }
}
