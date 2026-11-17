package org.ikigaidigital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class TimeDepositApplication {
    public static void main(String[] args) {
        SpringApplication.run(TimeDepositApplication.class, args);
    }

    @Bean
    public TimeDepositCalculator timeDepositCalculator(List<InterestPolicy> interestPolicies) {
        return new TimeDepositCalculator(interestPolicies);
    }
}
