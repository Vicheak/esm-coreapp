package com.vicheak.coreapp.api.slip;

import com.vicheak.coreapp.api.slip.web.PaymentStateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentStateServiceImpl implements PaymentStateService {

    private final PaymentStateRepository paymentStateRepository;
    private final PaymentStateMapper paymentStateMapper;

    @Override
    public List<PaymentStateDto> loadAllPaymentStates() {
        return paymentStateMapper.toPaymentStateDto(paymentStateRepository.findAll());
    }

    @Override
    public PaymentStateDto loadPaymentStateByStatus(String status) {
        //load specific payment state resource by status
        PaymentState paymentState = paymentStateRepository.findByStatusIgnoreCase(status)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Payment State with status = %s has not been found...!"
                                        .formatted(status))
                );

        return paymentStateMapper.toPaymentStateDto(paymentState);
    }

    @Override
    public void createNewPaymentState(PaymentStateDto paymentStateDto) {
        //check if payment status exists in the database
        if (paymentStateRepository.existsByStatusIgnoreCase(paymentStateDto.status()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Payment Status is conflicted in the system! please try again!");

        //map from dto to entity payment state
        paymentStateRepository.save(paymentStateMapper.fromPaymentStateDto(paymentStateDto));
    }

    @Override
    public void updatePaymentStateByStatus(String status, PaymentStateDto paymentStateDto) {
        //load specific payment state resource by status
        PaymentState paymentState = paymentStateRepository.findByStatusIgnoreCase(status)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Payment State with status = %s has not been found...!"
                                        .formatted(status))
                );

        //check if payment status exists in the database
        if (Objects.nonNull(paymentStateDto.status()))
            if (!paymentState.getStatus().equalsIgnoreCase(paymentStateDto.status()) &&
                    paymentStateRepository.existsByStatusIgnoreCase(paymentStateDto.status()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Payment Status is conflicted in the system! please try again!");

        //map from dto to entity except null value
        paymentStateMapper.fromPaymentStateDto(paymentState, paymentStateDto);

        //save to the database
        paymentStateRepository.save(paymentState);
    }

    @Override
    public void deletePaymentStateByStatus(String status) {
        //load specific payment state resource by status
        PaymentState paymentState = paymentStateRepository.findByStatusIgnoreCase(status)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Payment State with status = %s has not been found...!"
                                        .formatted(status))
                );

        //save to the database
        paymentStateRepository.delete(paymentState);
    }

}
