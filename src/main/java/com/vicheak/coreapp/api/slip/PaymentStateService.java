package com.vicheak.coreapp.api.slip;

import com.vicheak.coreapp.api.slip.web.PaymentStateDto;

import java.util.List;

public interface PaymentStateService {

    /**
     * This method is used to load all payment state resources from the system
     * @return List<PaymentStateDto>
     */
    List<PaymentStateDto> loadAllPaymentStates();

    /**
     * This method is used to load specific payment state resource by status
     * @param status is the path parameter from client
     * @return PaymentStateDto
     */
    PaymentStateDto loadPaymentStateByStatus(String status);

    /**
     * This method is used to create new payment state resource
     * @param paymentStateDto is the request from client
     */
    void createNewPaymentState(PaymentStateDto paymentStateDto);

    /**
     * This method is used to update specific payment state resource by status
     * @param status is the path parameter from client
     * @param paymentStateDto is the request from client
     */
    void updatePaymentStateByStatus(String status, PaymentStateDto paymentStateDto);

    /**
     * This method is used to delete specific payment state resource by status
     * @param status is the path parameter from client
     */
    void deletePaymentStateByStatus(String status);

}
