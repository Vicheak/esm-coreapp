package com.vicheak.coreapp.api.employee;

import com.vicheak.coreapp.api.employee.web.EmployeeDto;
import com.vicheak.coreapp.api.employee.web.TransactionEmployeeDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "department.id", source = "departmentId")
    Employee fromTransactionEmployeeDto(TransactionEmployeeDto transactionEmployeeDto);

    @Mapping(target = "imageUri", source = "imagePath")
    @Mapping(target = "departmentName", source = "department.name")
    EmployeeDto toEmployeeDto(Employee employee);

    List<EmployeeDto> toEmployeeDto(List<Employee> employees);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "department.id", source = "departmentId")
    void fromTransactionEmployeeDto(@MappingTarget Employee employee, TransactionEmployeeDto transactionEmployeeDto);

}
