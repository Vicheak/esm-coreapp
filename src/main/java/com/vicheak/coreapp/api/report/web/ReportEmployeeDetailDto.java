package com.vicheak.coreapp.api.report.web;

import com.vicheak.coreapp.api.employee.web.EmployeeDto;
import lombok.Builder;

import java.util.List;

@Builder
public record ReportEmployeeDetailDto(EmployeeDto employeeDto,
                                      List<BaseSalaryEmployeeDetailDto> baseSalaryEmployeeDetailDtos,
                                      List<SalarySlipDto> salarySlipDtos) {
}
