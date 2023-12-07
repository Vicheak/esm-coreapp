package com.vicheak.coreapp.api.report.web;

import com.vicheak.coreapp.api.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/noOfEmployees")
    public List<ReportNoEmpDto> reportNoEmp() {
        return reportService.reportNoEmp();
    }

    @GetMapping("/employeeDetail/{uuid}")
    public ReportEmployeeDetailDto reportEmployeeDetail(@PathVariable("uuid") String uuid) {
        return reportService.reportEmployeeDetail(uuid);
    }

}
