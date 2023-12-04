package com.vicheak.coreapp.api.slip;

import com.vicheak.coreapp.api.slip.web.TransactionSalaryPaymentDto;

public interface SalaryPaymentService {

    /**
     * This method is used to create new salary slip for specific employee
     * ,including choosing specific employee to get slip and generating salary slip (benefit and deduction)
     * @param transactionSalaryPaymentDto is the request from client followed by DTO pattern
     */
    void createNewSalaryPayment(TransactionSalaryPaymentDto transactionSalaryPaymentDto);

}
