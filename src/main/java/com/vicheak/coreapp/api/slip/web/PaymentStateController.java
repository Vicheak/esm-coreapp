package com.vicheak.coreapp.api.slip.web;

import com.vicheak.coreapp.api.slip.PaymentStateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/paymentStates")
@RequiredArgsConstructor
public class PaymentStateController {

    private final PaymentStateService paymentStateService;

    @GetMapping
    public List<PaymentStateDto> loadAllPaymentStates() {
        return paymentStateService.loadAllPaymentStates();
    }

    @GetMapping("/{status}")
    public PaymentStateDto loadPaymentStateByStatus(@PathVariable("status") String status) {
        return paymentStateService.loadPaymentStateByStatus(status);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createNewPaymentState(@RequestBody @Valid PaymentStateDto paymentStateDto) {
        paymentStateService.createNewPaymentState(paymentStateDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{status}")
    public void updatePaymentStateByStatus(@PathVariable("status") String status,
                                           @RequestBody PaymentStateDto paymentStateDto) {
        paymentStateService.updatePaymentStateByStatus(status, paymentStateDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{status}")
    public void deletePaymentStateByStatus(@PathVariable("status") String status) {
        paymentStateService.deletePaymentStateByStatus(status);
    }

}
