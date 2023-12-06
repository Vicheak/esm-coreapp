package com.vicheak.coreapp.api.slip;

import com.vicheak.coreapp.api.slip.web.TransactionSalaryPaymentDto;
import com.vicheak.coreapp.api.slip.web.UpdatePaymentStateDto;

public interface SalaryPaymentService {

    /**
     * This method is used to create new salary slip for specific employee
     * ,including choosing specific employee to get slip and generating salary slip (benefit and deduction)
     * @param transactionSalaryPaymentDto is the request from client followed by DTO pattern
     */
    void createNewSalaryPayment(TransactionSalaryPaymentDto transactionSalaryPaymentDto);

    /**
     * This method is used to update payment status of specific salary payment
     * @param uuid is the path parameter from client
     * @param updatePaymentStateDto is the request from client
     */
    void updatePaymentStateByUuid(String uuid, UpdatePaymentStateDto updatePaymentStateDto);

    /**
     * This method is used to delete specific salary payment by uuid
     * @param uuid is the path parameter from client
     */
    void deleteSalaryPaymentByUuid(String uuid);

}
