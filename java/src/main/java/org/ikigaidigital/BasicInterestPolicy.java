package org.ikigaidigital;

import org.springframework.stereotype.Component;

@Component
public class BasicInterestPolicy implements InterestPolicy {
    @Override
    public boolean appliesTo(TimeDeposit timeDeposit) {
        return "basic".equals(timeDeposit.getPlanType()) && timeDeposit.getDays() > 30;
    }

    @Override
    public double monthlyInterest(TimeDeposit timeDeposit) {
        return timeDeposit.getBalance() * 0.01 / 12;
    }
}
