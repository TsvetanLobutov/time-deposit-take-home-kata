package org.ikigaidigital;

import org.springframework.stereotype.Component;

@Component
public class StudentInterestPolicy implements InterestPolicy {
    @Override
    public boolean appliesTo(TimeDeposit timeDeposit) {
        return "student".equals(timeDeposit.getPlanType())
            && timeDeposit.getDays() > 30
            && timeDeposit.getDays() < 366;
    }

    @Override
    public double monthlyInterest(TimeDeposit timeDeposit) {
        return timeDeposit.getBalance() * 0.03 / 12;
    }
}
