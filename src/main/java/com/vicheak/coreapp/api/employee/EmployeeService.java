package com.vicheak.coreapp.api.employee;

import com.vicheak.coreapp.api.employee.web.EmployeeBaseSalaryDto;
import com.vicheak.coreapp.api.employee.web.EmployeeStatusDto;
import com.vicheak.coreapp.api.employee.web.TransactionEmployeeDto;
import com.vicheak.coreapp.api.employee.web.EmployeeDto;
import com.vicheak.coreapp.api.file.web.FileDto;
import com.vicheak.coreapp.pagination.PageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    /**
     * This method is used to load all employees from db
     * @return List<EmployeeDto>
     */
    List<EmployeeDto> loadAllEmployees();

    /**
     * This method is used to load specific employee by uuid
     * @param uuid is the path parameter from client
     * @return EmployeeDto
     */
    EmployeeDto loadEmployeeByUuid(String uuid);

    /**
     * This method is used to search employees from any criteria
     * @param requestMap is the request parameter or query string from client
     * @return List<EmployeeDto>
     */
    List<EmployeeDto> searchEmployees(Map<String, Object> requestMap);

    /**
     * This method is used to sort employees from any criteria
     * @param requestMap is the request parameter or query string from client
     * @return List<EmployeeDto>
     */
    List<EmployeeDto> sortEmployees(Map<String, String> requestMap);

    /**
     * This method is used to paginate employees from any criteria
     * @param requestMap is the request parameter or query string from client
     * @return Page<EmployeeDto>
     */
    PageDto paginateEmployees(Map<String, String> requestMap);

    /**
     * This method is used to create new employee resource
     * @param transactionEmployeeDto is the request from client
     */
    void createNewEmployee(TransactionEmployeeDto transactionEmployeeDto);

    /**
     * This method is used to update specific employee base salary by uuid
     * @param uuid is the path parameter from client
     * @param employeeBaseSalaryDto is the request from client
     */
    void updateBaseSalaryByUuid(String uuid, EmployeeBaseSalaryDto employeeBaseSalaryDto);

    /**
     * This method is used to upload specific employee image by uuid
     * @param uuid is the path parameter from client
     * @return FileDto
     */
    FileDto uploadEmployeeImageByUuid(String uuid, MultipartFile file);

    /**
     * This method is used to update specific employee by uuid (partially update)
     * @param uuid is the path parameter from client
     * @param transactionEmployeeDto is the request from client
     */
    void updateEmployeeByUuid(String uuid, TransactionEmployeeDto transactionEmployeeDto);

    /**
     * This method is used to update specific employee status by uuid
     * @param uuid is the path parameter from client
     * @param employeeStatusDto is the request from client
     */
    void updateEmployeeStatusByUuid(String uuid, EmployeeStatusDto employeeStatusDto);

    /**
     * This method is used to delete specific employee status by uuid
     * @param uuid is the path parameter from client
     */
    void deleteEmployeeByUuid(String uuid);

    /**
     * This method is used to load any employee within specific department
     * @param name is the path parameter from client
     * @return List<EmployeeDto>
     */
    List<EmployeeDto> loadEmployeesByDepartmentName(String name);

}
