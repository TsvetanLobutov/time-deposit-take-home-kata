package org.ikigaidigital;

public interface InterestPolicy {
    boolean appliesTo(TimeDeposit timeDeposit);

    /** Returns one month's interest, prorating the plan's annual rate by 12. */
    double monthlyInterest(TimeDeposit timeDeposit);
}
