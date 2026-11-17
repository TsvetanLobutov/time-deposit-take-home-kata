package org.ikigaidigital;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeDepositCalculatorTest {
    @Test
    void updateBalanceDoesNotApplyInterestBeforeThirtyDays() {
        TimeDepositCalculator calc = new TimeDepositCalculator();
        List<TimeDeposit> plans = List.of(
            new TimeDeposit(1, "basic", 12000.00, 29),
            new TimeDeposit(2, "student", 12000.00, 29),
            new TimeDeposit(3, "premium", 12000.00, 29)
        );

        calc.updateBalance(plans);

        assertThat(plans).extracting(TimeDeposit::getBalance)
            .containsExactly(12000.00, 12000.00, 12000.00);
    }

    @Test
    void updateBalanceAppliesExistingPlanRules() {
        TimeDepositCalculator calc = new TimeDepositCalculator();
        List<TimeDeposit> plans = List.of(
            new TimeDeposit(1, "basic", 12000.00, 30),
            new TimeDeposit(2, "basic", 12000.00, 31),
            new TimeDeposit(3, "student", 12000.00, 365),
            new TimeDeposit(4, "student", 12000.00, 366),
            new TimeDeposit(5, "premium", 12000.00, 45),
            new TimeDeposit(6, "premium", 12000.00, 46),
            new TimeDeposit(7, "unknown", 12000.00, 365)
        );
        calc.updateBalance(plans);

        assertThat(plans).extracting(TimeDeposit::getBalance)
            .containsExactly(12000.00, 12010.00, 12030.00, 12000.00, 12000.00, 12050.00, 12000.00);
    }

    @Test
    void updateBalanceAcceptsAdditionalInterestPolicies() {
        TimeDepositCalculator calc = new TimeDepositCalculator(List.of(new InterestPolicy() {
            @Override
            public boolean appliesTo(TimeDeposit timeDeposit) {
                return "custom".equals(timeDeposit.getPlanType());
            }

            @Override
            public double monthlyInterest(TimeDeposit timeDeposit) {
                return timeDeposit.getBalance() * 0.12 / 12;
            }
        }));
        TimeDeposit plan = new TimeDeposit(1, "custom", 1000.00, 1);

        calc.updateBalance(List.of(plan));

        assertThat(plan.getBalance()).isEqualTo(1010.00);
    }
}
