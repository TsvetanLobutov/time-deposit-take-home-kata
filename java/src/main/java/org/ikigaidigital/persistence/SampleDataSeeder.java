package org.ikigaidigital.persistence;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@ConditionalOnProperty(name = "time-deposit.sample-data.enabled", havingValue = "true", matchIfMissing = true)
public class SampleDataSeeder implements ApplicationRunner {
    private final TimeDepositJpaRepository timeDepositRepository;
    private final WithdrawalJpaRepository withdrawalRepository;

    public SampleDataSeeder(
        TimeDepositJpaRepository timeDepositRepository,
        WithdrawalJpaRepository withdrawalRepository
    ) {
        this.timeDepositRepository = timeDepositRepository;
        this.withdrawalRepository = withdrawalRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (timeDepositRepository.count() > 0) {
            return;
        }

        List<TimeDepositEntity> deposits = timeDepositRepository.saveAll(List.of(
            new TimeDepositEntity("basic", 30, new BigDecimal("10000.00")),
            new TimeDepositEntity("student", 180, new BigDecimal("15000.00")),
            new TimeDepositEntity("premium", 60, new BigDecimal("25000.00"))
        ));

        withdrawalRepository.save(new WithdrawalEntity(
            deposits.get(0),
            new BigDecimal("500.00"),
            LocalDate.now()
        ));
    }
}
