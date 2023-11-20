package com.vicheak.coreapp.api.salarygross;

import com.vicheak.coreapp.api.salarygross.web.SalaryGrossDto;
import com.vicheak.coreapp.api.salarygross.web.TransactionSalaryGrossDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SalaryGrossMapper {

    @Mapping(target = "grossType", source = "grossType.name")
    SalaryGrossDto toSalaryGrossDto(SalaryGross salaryGross);

    List<SalaryGrossDto> toSalaryGrossDto(List<SalaryGross> salaryGrossList);

    @Mapping(target = "grossType.id", source = "grossTypeId")
    SalaryGross fromTransactionSalaryGrossDto(TransactionSalaryGrossDto transactionSalaryGrossDto);

}
