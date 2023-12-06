package com.vicheak.coreapp.api.slip;

import com.vicheak.coreapp.api.slip.web.TransactionSalaryPaymentDto;
import com.vicheak.coreapp.api.slip.web.UpdatePaymentStateDto;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface SalaryPaymentMapper {

    SalaryPayment fromTransactionSalaryPaymentDto(TransactionSalaryPaymentDto transactionSalaryPaymentDto);

    default SalaryPayment mapFromUpdatePaymentStateDto(SalaryPayment salaryPayment, UpdatePaymentStateDto updatePaymentStateDto) {
        if (Objects.nonNull(updatePaymentStateDto.isPaid())) {
            //2 = Paid
            //1 = Unpaid
            salaryPayment.setPaymentState(PaymentState.builder()
                    .id(updatePaymentStateDto.isPaid() ? 2 : 1)
                    .build());

            //set the payment date time
            salaryPayment.setPaymentDateTime(updatePaymentStateDto.isPaid() ? LocalDateTime.now() : null);
        }

        return salaryPayment;
    }

}
