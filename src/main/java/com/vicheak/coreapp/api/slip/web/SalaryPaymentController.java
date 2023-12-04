package com.vicheak.coreapp.api.slip.web;

import com.vicheak.coreapp.api.slip.SalaryPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/salaryPayments")
@RequiredArgsConstructor
public class SalaryPaymentController {

    private final SalaryPaymentService salaryPaymentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    void createNewSalaryPayment(@RequestBody @Valid TransactionSalaryPaymentDto transactionSalaryPaymentDto){
        salaryPaymentService.createNewSalaryPayment(transactionSalaryPaymentDto);
    }

}
