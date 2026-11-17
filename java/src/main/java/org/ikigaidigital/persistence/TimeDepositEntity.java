package org.ikigaidigital.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "time_deposits")
public class TimeDepositEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "plan_type", nullable = false)
    private String planType;

    @Column(nullable = false)
    private Integer days;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @OneToMany(mappedBy = "timeDeposit", fetch = FetchType.LAZY)
    private List<WithdrawalEntity> withdrawals = new ArrayList<>();

    protected TimeDepositEntity() {
    }

    public TimeDepositEntity(String planType, Integer days, BigDecimal balance) {
        this.planType = planType;
        this.days = days;
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    public String getPlanType() {
        return planType;
    }

    public Integer getDays() {
        return days;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<WithdrawalEntity> getWithdrawals() {
        return withdrawals;
    }
}
