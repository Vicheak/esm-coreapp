package com.vicheak.coreapp.api.employee.web;

import com.vicheak.coreapp.api.employee.EmployeeService;
import com.vicheak.coreapp.api.file.web.FileDto;
import com.vicheak.coreapp.pagination.PageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public List<EmployeeDto> loadAllEmployees() {
        return employeeService.loadAllEmployees();
    }

    @GetMapping("/{uuid}")
    public EmployeeDto loadEmployeeByUuid(@PathVariable("uuid") String uuid) {
        return employeeService.loadEmployeeByUuid(uuid);
    }

    @GetMapping("/search")
    public List<EmployeeDto> searchEmployees(@RequestParam(required = false) Map<String, Object> requestMap) {
        return employeeService.searchEmployees(requestMap);
    }

    @GetMapping("/sort")
    public List<EmployeeDto> sortEmployees(@RequestParam(required = false) Map<String, String> requestMap) {
        return employeeService.sortEmployees(requestMap);
    }

    @GetMapping("/paginate")
    public PageDto paginateEmployees(@RequestParam(required = false) Map<String, String> requestMap) {
        return employeeService.paginateEmployees(requestMap);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createNewEmployee(@RequestBody @Valid TransactionEmployeeDto transactionEmployeeDto) {
        employeeService.createNewEmployee(transactionEmployeeDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{uuid}/updateBaseSalary")
    public void updateBaseSalaryByUuid(@PathVariable("uuid") String uuid,
                                       @RequestBody @Valid EmployeeBaseSalaryDto employeeBaseSalaryDto) {
        employeeService.updateBaseSalaryByUuid(uuid, employeeBaseSalaryDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/uploadProfile/{uuid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FileDto uploadEmployeeImageByUuid(@PathVariable("uuid") String uuid,
                                             @RequestPart MultipartFile file) {
        return employeeService.uploadEmployeeImageByUuid(uuid, file);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{uuid}")
    public void updateEmployeeByUuid(@PathVariable("uuid") String uuid,
                                     @RequestBody TransactionEmployeeDto transactionEmployeeDto) {
        employeeService.updateEmployeeByUuid(uuid, transactionEmployeeDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{uuid}/updateStatus")
    public void updateEmployeeStatusByUuid(@PathVariable("uuid") String uuid,
                                           @RequestBody @Valid EmployeeStatusDto employeeStatusDto) {
        employeeService.updateEmployeeStatusByUuid(uuid, employeeStatusDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{uuid}")
    public void deleteEmployeeByUuid(@PathVariable("uuid") String uuid) {
        employeeService.deleteEmployeeByUuid(uuid);
    }

}
