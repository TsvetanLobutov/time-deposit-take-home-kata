package org.ikigaidigital.api;

import org.ikigaidigital.application.TimeDepositData;
import org.ikigaidigital.application.TimeDepositService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/time-deposits")
public class TimeDepositController {
    private final TimeDepositService service;

    public TimeDepositController(TimeDepositService service) {
        this.service = service;
    }

    @GetMapping
    public List<TimeDepositResponse> findAll() {
        return service.findAll().stream()
            .map(TimeDepositController::toResponse)
            .toList();
    }

    @PutMapping("/balances")
    public ResponseEntity<Void> updateBalances() {
        service.updateBalances();
        return ResponseEntity.noContent().build();
    }

    private static TimeDepositResponse toResponse(TimeDepositData deposit) {
        return new TimeDepositResponse(
            deposit.id(),
            deposit.planType(),
            deposit.balance(),
            deposit.days(),
            deposit.withdrawals()
        );
    }
}
