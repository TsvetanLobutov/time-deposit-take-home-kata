package org.ikigaidigital;

import org.springframework.stereotype.Component;

@Component
public class PremiumInterestPolicy implements InterestPolicy {
    @Override
    public boolean appliesTo(TimeDeposit timeDeposit) {
        return "premium".equals(timeDeposit.getPlanType()) && timeDeposit.getDays() > 45;
    }

    @Override
    public double monthlyInterest(TimeDeposit timeDeposit) {
        return timeDeposit.getBalance() * 0.05 / 12;
    }
}
