package com.vicheak.coreapp.api.employee;

import com.vicheak.coreapp.api.employee.web.EmployeeDto;
import com.vicheak.coreapp.api.employee.web.TransactionEmployeeDto;
import com.vicheak.coreapp.util.ValueInjectUtil;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class EmployeeMapper {

    protected ValueInjectUtil valueInjectUtil;

    @Autowired
    public void setValueInjectUtil(ValueInjectUtil valueInjectUtil) {
        this.valueInjectUtil = valueInjectUtil;
    }

    @Mapping(target = "department.id", source = "departmentId")
    public abstract Employee fromTransactionEmployeeDto(TransactionEmployeeDto transactionEmployeeDto);

    @Mapping(target = "baseSalary", source = "employee.baseSalary", defaultExpression = "java(valueInjectUtil.getBigDecimal(0.00))")
    @Mapping(target = "active", source = "employee.active", defaultExpression = "java(Boolean.FALSE)")
    @Mapping(target = "imageUri", expression = "java(valueInjectUtil.getImageUri(employee.getImageName()))")
    @Mapping(target = "departmentName", source = "department.name")
    public abstract EmployeeDto toEmployeeDto(Employee employee);

    public abstract List<EmployeeDto> toEmployeeDto(List<Employee> employees);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "department.id", source = "departmentId")
    public abstract void fromTransactionEmployeeDto(@MappingTarget Employee employee, TransactionEmployeeDto transactionEmployeeDto);

}
