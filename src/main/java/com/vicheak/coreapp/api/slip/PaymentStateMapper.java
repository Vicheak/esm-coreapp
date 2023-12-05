package com.vicheak.coreapp.api.slip;

import com.vicheak.coreapp.api.slip.web.PaymentStateDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentStateMapper {

    PaymentStateDto toPaymentStateDto(PaymentState paymentState);

    List<PaymentStateDto> toPaymentStateDto(List<PaymentState> paymentStates);

    PaymentState fromPaymentStateDto(PaymentStateDto paymentStateDto);

}
