package com.vicheak.coreapp.api.slip.web;

import com.vicheak.coreapp.api.slip.SalaryPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/uploadSalarySlip", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<Integer, String> uploadSalaryPayments(@RequestPart MultipartFile file) {
        return salaryPaymentService.uploadSalaryPayments(file);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{uuid}")
    void updatePaymentStateByUuid(@PathVariable("uuid") String uuid,
                                  @RequestBody @Valid UpdatePaymentStateDto updatePaymentStateDto) {
        salaryPaymentService.updatePaymentStateByUuid(uuid, updatePaymentStateDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{uuid}")
    void deleteSalaryPaymentByUuid(@PathVariable("uuid") String uuid) {
        salaryPaymentService.deleteSalaryPaymentByUuid(uuid);
    }

}
