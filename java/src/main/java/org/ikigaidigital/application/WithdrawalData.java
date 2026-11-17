package org.ikigaidigital.application;

import java.math.BigDecimal;
import java.time.LocalDate;

public record WithdrawalData(int id, BigDecimal amount, LocalDate date) {
}
