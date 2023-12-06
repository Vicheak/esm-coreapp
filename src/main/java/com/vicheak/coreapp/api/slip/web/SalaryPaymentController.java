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
    void createNewSalaryPayment(@RequestBody @Valid TransactionSalaryPaymentDto transactionSalaryPaymentDto) {
        salaryPaymentService.createNewSalaryPayment(transactionSalaryPaymentDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{uuid}")
    void updatePaymentStateByUuid(@PathVariable("uuid") String uuid,
                                  @RequestBody @Valid UpdatePaymentStateDto updatePaymentStateDto) {
        salaryPaymentService.updatePaymentStateByUuid(uuid, updatePaymentStateDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{uuid}")
    void deleteSalaryPaymentByUuid(@PathVariable("uuid") String uuid){
        salaryPaymentService.deleteSalaryPaymentByUuid(uuid);
    }

}
