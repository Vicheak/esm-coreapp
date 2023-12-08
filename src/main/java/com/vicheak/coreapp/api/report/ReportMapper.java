package com.vicheak.coreapp.api.report;

import com.vicheak.coreapp.api.employee.BaseSalaryLog;
import com.vicheak.coreapp.api.report.web.BaseSalaryEmployeeDetailDto;
import com.vicheak.coreapp.api.report.web.ReportSalaryPaymentDto;
import com.vicheak.coreapp.api.report.web.SalarySlipDto;
import com.vicheak.coreapp.api.slip.SalaryPayment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    BaseSalaryEmployeeDetailDto toBaseSalaryEmployeeDetailDto(BaseSalaryLog salaryLog);

    List<BaseSalaryEmployeeDetailDto> toBaseSalaryEmployeeDetailDto(List<BaseSalaryLog> baseSalaryLogs);

    @Mapping(target = "salaryPaymentId", source = "id")
    @Mapping(target = "salaryPaymentUuid", source = "uuid")
    @Mapping(target = "generatedSlipDateTime", source = "dateTime")
    @Mapping(target = "paymentStatus", source = "salaryPayment.paymentState.status")
    SalarySlipDto toSalarySlipDto(SalaryPayment salaryPayment);

    List<SalarySlipDto> toSalarySlipDto(List<SalaryPayment> salaryPayments);

    @Mapping(target = "generatedSlipDateTime", source = "dateTime")
    @Mapping(target = "paymentStatus", source = "salaryPayment.paymentState.status")
    ReportSalaryPaymentDto toReportSalaryPaymentDto(SalaryPayment salaryPayment);

}
