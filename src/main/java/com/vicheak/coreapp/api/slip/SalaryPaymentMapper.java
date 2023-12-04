package com.vicheak.coreapp.api.slip;

import com.vicheak.coreapp.api.slip.web.TransactionSalaryPaymentDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SalaryPaymentMapper {

    SalaryPayment fromTransactionSalaryPaymentDto(TransactionSalaryPaymentDto transactionSalaryPaymentDto);

}
