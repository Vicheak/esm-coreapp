package com.vicheak.coreapp.api.slip;

import com.vicheak.coreapp.api.slip.web.PaymentStateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentStateMapper {

    PaymentStateDto toPaymentStateDto(PaymentState paymentState);

    List<PaymentStateDto> toPaymentStateDto(List<PaymentState> paymentStates);

    PaymentState fromPaymentStateDto(PaymentStateDto paymentStateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromPaymentStateDto(@MappingTarget PaymentState paymentState, PaymentStateDto paymentStateDto);

}
