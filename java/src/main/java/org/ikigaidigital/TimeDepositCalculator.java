package org.ikigaidigital;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class TimeDepositCalculator {
    private final List<InterestPolicy> interestPolicies;

    public TimeDepositCalculator() {
        this(List.of(
            new BasicInterestPolicy(),
            new StudentInterestPolicy(),
            new PremiumInterestPolicy()
        ));
    }

    public TimeDepositCalculator(List<InterestPolicy> interestPolicies) {
        this.interestPolicies = List.copyOf(interestPolicies);
    }

    public void updateBalance(List<TimeDeposit> xs) {
        for (int i = 0; i < xs.size(); i++) {
            double interest = 0;

            for (InterestPolicy policy : interestPolicies) {
                if (policy.appliesTo(xs.get(i))) {
                    interest = policy.monthlyInterest(xs.get(i));
                    break;
                }
            }

            double a2d = xs.get(i).getBalance() + (new BigDecimal(interest).setScale(2, RoundingMode.HALF_UP)).doubleValue();
            xs.get(i).setBalance(a2d);
        }
    }
}
