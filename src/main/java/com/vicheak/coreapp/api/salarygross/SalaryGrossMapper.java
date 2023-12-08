package com.vicheak.coreapp.api.salarygross;

import com.vicheak.coreapp.api.salarygross.web.SalaryGrossDto;
import com.vicheak.coreapp.api.salarygross.web.TransactionSalaryGrossDto;
import com.vicheak.coreapp.api.slip.SalaryPaymentGross;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SalaryGrossMapper {

    @Mapping(target = "grossType", source = "grossType.name")
    SalaryGrossDto toSalaryGrossDto(SalaryGross salaryGross);

    List<SalaryGrossDto> toSalaryGrossDto(List<SalaryGross> salaryGrossList);

    @Mapping(target = "grossType.id", source = "grossTypeId")
    SalaryGross fromTransactionSalaryGrossDto(TransactionSalaryGrossDto transactionSalaryGrossDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "grossType.id", source = "grossTypeId")
    void fromTransactionSalaryGrossDto(@MappingTarget SalaryGross salaryGross, TransactionSalaryGrossDto transactionSalaryGrossDto);

    @Mapping(target = "name", source = "salaryPaymentGross.salaryGross.name")
    @Mapping(target = "grossType", source = "salaryPaymentGross.salaryGross")
    SalaryGrossDto toSalaryGrossDto(SalaryPaymentGross salaryPaymentGross);

    List<SalaryGrossDto> toSalaryGrossDtoList(List<SalaryPaymentGross> salaryPaymentGrossList);

    default String grossTypeName(SalaryGross salaryGross){
        return salaryGross.getGrossType().getName();
    }

}
