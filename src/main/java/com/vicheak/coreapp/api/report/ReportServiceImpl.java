package com.vicheak.coreapp.api.report;

import com.vicheak.coreapp.api.department.Department;
import com.vicheak.coreapp.api.department.DepartmentRepository;
import com.vicheak.coreapp.api.employee.*;
import com.vicheak.coreapp.api.employee.web.EmployeeDto;
import com.vicheak.coreapp.api.report.web.*;
import com.vicheak.coreapp.api.salarygross.SalaryGrossMapper;
import com.vicheak.coreapp.api.salarygross.web.SalaryGrossDto;
import com.vicheak.coreapp.api.slip.SalaryPayment;
import com.vicheak.coreapp.api.slip.SalaryPaymentGross;
import com.vicheak.coreapp.api.slip.SalaryPaymentGrossRepository;
import com.vicheak.coreapp.api.slip.SalaryPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final BaseSalaryLogRepository baseSalaryLogRepository;
    private final SalaryPaymentRepository salaryPaymentRepository;
    private final SalaryPaymentGrossRepository salaryPaymentGrossRepository;
    private final SalaryGrossMapper salaryGrossMapper;

    @Override
    public List<ReportNoEmpDto> reportNoEmp() {
        //create list of report dto
        List<ReportNoEmpDto> reportNoEmpDtos = new ArrayList<>();

        //load all departments in the system
        List<Department> departments = departmentRepository.findAll();

        departments.forEach(department -> {
            //find the number of employee in each department
            int numberOfEmployee = employeeRepository.findByDepartmentName(department.getName()).size();

            reportNoEmpDtos.add(ReportNoEmpDto.builder()
                    .departmentName(department.getName())
                    .numberOfEmployee(numberOfEmployee)
                    .build());
        });

        return reportNoEmpDtos;
    }

    @Override
    public ReportEmployeeDetailDto reportEmployeeDetail(String uuid) {
        //load specific employee via uuid
        Employee employee = employeeRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format("Employee with uuid = %s not found...!", uuid))
                );

        //build employee dto
        EmployeeDto employeeDto = employeeMapper.toEmployeeDto(employee);

        List<BaseSalaryLog> baseSalaryLogs = baseSalaryLogRepository.findByEmployeeId(employee.getId());

        //build base salary employee detail dto
        List<BaseSalaryEmployeeDetailDto> baseSalaryEmployeeDetailDtos = reportMapper.toBaseSalaryEmployeeDetailDto(baseSalaryLogs);

        List<SalaryPayment> salaryPayments = salaryPaymentRepository.findByEmployeeId(employee.getId());

        //map from entity to dto
        List<SalarySlipDto> salarySlipDtos = reportMapper.toSalarySlipDto(salaryPayments);

        //check if salary slips are empty
        if (!salarySlipDtos.isEmpty()) {
            //build salary slip dto
            salarySlipDtos.forEach(salarySlipDto -> {
                List<SalaryPaymentGross> salaryPaymentGrossList =
                        salaryPaymentGrossRepository.findBySalaryPaymentId(salarySlipDto.getSalaryPaymentId());

                Map<Integer, List<SalaryPaymentGross>> salaryPaymentGrossMap = buildSalaryPaymentGrossMap(salaryPaymentGrossList);

                if (!salaryPaymentGrossMap.isEmpty()) {
                    //find total benefit & deduction salary gross
                    double salaryGrossBenefitAmount = buildSalaryPaymentGrossBenefit(salaryPaymentGrossMap);
                    double salaryGrossDeductionAmount = buildSalaryPaymentGrossDeduction(salaryPaymentGrossMap);

                    BigDecimal finalSalary = salarySlipDto.getBaseSalary().add(BigDecimal.valueOf(salaryGrossBenefitAmount - salaryGrossDeductionAmount));

                    salarySlipDto.setSalaryGrossBenefitAmount(BigDecimal.valueOf(salaryGrossBenefitAmount));
                    salarySlipDto.setSalaryGrossDeductionAmount(BigDecimal.valueOf(salaryGrossDeductionAmount));
                    salarySlipDto.setFinalSalary(finalSalary);
                } else {
                    salarySlipDto.setSalaryGrossBenefitAmount(BigDecimal.valueOf(0));
                    salarySlipDto.setSalaryGrossDeductionAmount(BigDecimal.valueOf(0));
                    salarySlipDto.setFinalSalary(salarySlipDto.getBaseSalary());
                }
            });
        }

        return ReportEmployeeDetailDto.builder()
                .employeeDto(employeeDto)
                .baseSalaryEmployeeDetailDtos(baseSalaryEmployeeDetailDtos)
                .salarySlipDtos(salarySlipDtos)
                .build();
    }

    @Override
    public ReportSalaryPaymentDto reportSalaryPaymentDetail(String uuid) {
        //load salary payment resource by specific uuid
        SalaryPayment salaryPayment = salaryPaymentRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Salary payment with uuid = %s has not been found...!"
                                        .formatted(uuid))
                );

        //map from entity to report dto
        ReportSalaryPaymentDto reportSalaryPaymentDto = reportMapper.toReportSalaryPaymentDto(salaryPayment);

        //load salary payment gross list by salary payment id
        List<SalaryPaymentGross> salaryPaymentGrossList =
                salaryPaymentGrossRepository.findBySalaryPaymentId(salaryPayment.getId());

        Map<Integer, List<SalaryPaymentGross>> salaryPaymentGrossMap = buildSalaryPaymentGrossMap(salaryPaymentGrossList);

        if (!salaryPaymentGrossMap.isEmpty()) {
            //find total benefit & deduction salary gross
            double salaryGrossBenefitAmount = buildSalaryPaymentGrossBenefit(salaryPaymentGrossMap);
            double salaryGrossDeductionAmount = buildSalaryPaymentGrossDeduction(salaryPaymentGrossMap);

            BigDecimal finalSalary = salaryPayment.getBaseSalary().add(BigDecimal.valueOf(salaryGrossBenefitAmount - salaryGrossDeductionAmount));

            reportSalaryPaymentDto.setSalaryGrossBenefitAmount(BigDecimal.valueOf(salaryGrossBenefitAmount));
            reportSalaryPaymentDto.setSalaryGrossDeductionAmount(BigDecimal.valueOf(salaryGrossDeductionAmount));
            reportSalaryPaymentDto.setFinalSalary(finalSalary);

        } else {
            reportSalaryPaymentDto.setSalaryGrossBenefitAmount(BigDecimal.valueOf(0));
            reportSalaryPaymentDto.setSalaryGrossDeductionAmount(BigDecimal.valueOf(0));
            reportSalaryPaymentDto.setFinalSalary(salaryPayment.getBaseSalary());
        }

        //build salary gross for report
        List<SalaryGrossDto> salaryGrossDtos = salaryGrossMapper.toSalaryGrossDtoList(salaryPaymentGrossList);
        reportSalaryPaymentDto.setSalaryGrossDtos(salaryGrossDtos);

        return reportSalaryPaymentDto;
    }

    private Map<Integer, List<SalaryPaymentGross>> buildSalaryPaymentGrossMap(List<SalaryPaymentGross> salaryPaymentGrossList) {
        return salaryPaymentGrossList.stream()
                .collect(Collectors.groupingBy(salaryPaymentGross ->
                        salaryPaymentGross.getSalaryGross().getGrossType().getId()));
    }

    private double buildSalaryPaymentGrossBenefit(Map<Integer, List<SalaryPaymentGross>> salaryPaymentGrossMap) {
        if (Objects.nonNull(salaryPaymentGrossMap.get(1)))
            return salaryPaymentGrossMap.get(1).stream()
                    .mapToDouble(spg -> spg.getAmount().doubleValue())
                    .sum();
        return 0;
    }

    private double buildSalaryPaymentGrossDeduction(Map<Integer, List<SalaryPaymentGross>> salaryPaymentGrossMap) {
        if (Objects.nonNull(salaryPaymentGrossMap.get(2)))
            return salaryPaymentGrossMap.get(2).stream()
                    .mapToDouble(spg -> spg.getAmount().doubleValue())
                    .sum();
        return 0;
    }

}
