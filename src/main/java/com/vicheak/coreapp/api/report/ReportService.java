package com.vicheak.coreapp.api.report;

import com.vicheak.coreapp.api.report.web.ReportEmployeeDetailDto;
import com.vicheak.coreapp.api.report.web.ReportNoEmpDto;
import com.vicheak.coreapp.api.report.web.ReportSalaryPaymentDto;

import java.util.List;

public interface ReportService {

    /**
     * This method is used to report number of employee of each department in the system
     * @return List<ReportNoEmpDto>
     */
    List<ReportNoEmpDto> reportNoEmp();

    /**
     * This method is used to report employee detail information including salary histories and salary payments
     * @param uuid is the path parameter from client
     * @return ReportEmployeeDetailDto
     */
    ReportEmployeeDetailDto reportEmployeeDetail(String uuid);

    /**
     * This method is used to report salary payment detail information based on salary payment uuid,
     * including salary payment gross (benefit and deduction)
     * @param uuid is the path parameter from client
     * @return ReportSalaryPaymentDto
     */
    ReportSalaryPaymentDto reportSalaryPaymentDetail(String uuid);

}
