package org.ikigaidigital.api;

import org.ikigaidigital.persistence.TimeDepositEntity;
import org.ikigaidigital.persistence.TimeDepositJpaRepository;
import org.ikigaidigital.persistence.SampleDataSeeder;
import org.ikigaidigital.persistence.WithdrawalEntity;
import org.ikigaidigital.persistence.WithdrawalJpaRepository;
import org.springframework.boot.DefaultApplicationArguments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TimeDepositApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TimeDepositJpaRepository timeDepositRepository;

    @Autowired
    private WithdrawalJpaRepository withdrawalRepository;

    @Autowired
    private SampleDataSeeder sampleDataSeeder;

    @BeforeEach
    void clearDatabase() {
        withdrawalRepository.deleteAll();
        timeDepositRepository.deleteAll();
    }

    @Test
    void getReturnsDepositsAndTheirWithdrawals() throws Exception {
        TimeDepositEntity deposit = timeDepositRepository.save(
            new TimeDepositEntity("basic", 45, new BigDecimal("12000.00"))
        );
        withdrawalRepository.save(
            new WithdrawalEntity(deposit, new BigDecimal("250.00"), LocalDate.of(2026, 1, 15))
        );

        mockMvc.perform(get("/api/time-deposits"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id").value(deposit.getId()))
            .andExpect(jsonPath("$[0].planType").value("basic"))
            .andExpect(jsonPath("$[0].balance").value(12000.00))
            .andExpect(jsonPath("$[0].days").value(45))
            .andExpect(jsonPath("$[0].withdrawals", hasSize(1)))
            .andExpect(jsonPath("$[0].withdrawals[0].amount").value(250.00))
            .andExpect(jsonPath("$[0].withdrawals[0].date").value("2026-01-15"));
    }

    @Test
    void putUpdatesEveryDepositBalance() throws Exception {
        TimeDepositEntity basic = timeDepositRepository.save(
            new TimeDepositEntity("basic", 31, new BigDecimal("12000.00"))
        );
        TimeDepositEntity student = timeDepositRepository.save(
            new TimeDepositEntity("student", 365, new BigDecimal("12000.00"))
        );

        mockMvc.perform(put("/api/time-deposits/balances"))
            .andExpect(status().isNoContent());

        assertThat(timeDepositRepository.findById(basic.getId()).orElseThrow().getBalance())
            .isEqualByComparingTo("12010.00");
        assertThat(timeDepositRepository.findById(student.getId()).orElseThrow().getBalance())
            .isEqualByComparingTo("12030.00");
    }

    @Test
    void servesTheSwaggerContract() throws Exception {
        mockMvc.perform(get("/openapi.yaml"))
            .andExpect(status().isOk())
            .andExpect(content().string(org.hamcrest.Matchers.containsString("/api/time-deposits/balances:")))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("/api/time-deposits:")));
    }

    @Test
    void seedsExampleDataOnceForAnEmptyDatabase() throws Exception {
        sampleDataSeeder.run(new DefaultApplicationArguments(new String[0]));
        sampleDataSeeder.run(new DefaultApplicationArguments(new String[0]));

        mockMvc.perform(get("/api/time-deposits"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].withdrawals", hasSize(1)));
    }
}
