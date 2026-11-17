package org.ikigaidigital.application;

import java.util.List;

public interface TimeDepositStore {
    List<TimeDepositData> findAll();

    void updateBalances(List<TimeDepositData> deposits);
}
