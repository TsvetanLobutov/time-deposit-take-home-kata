package org.ikigaidigital.api;

import org.ikigaidigital.application.WithdrawalData;

import java.math.BigDecimal;
import java.util.List;

public record TimeDepositResponse(
    int id,
    String planType,
    BigDecimal balance,
    int days,
    List<WithdrawalData> withdrawals
) {
}
