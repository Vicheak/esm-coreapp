package com.vicheak.coreapp.api.employee;

import com.vicheak.coreapp.api.employee.web.BaseSalaryLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BaseSalaryLogMapper {

    @Mapping(target = "employeeName",
            expression = "java(baseSalaryLog.getEmployee().getFirstName() + ' ' + baseSalaryLog.getEmployee().getLastName())")
    BaseSalaryLogDto toBaseSalaryLogDto(BaseSalaryLog baseSalaryLog);

    List<BaseSalaryLogDto> toBaseSalaryLogDto(List<BaseSalaryLog> baseSalaryLogs);

}
