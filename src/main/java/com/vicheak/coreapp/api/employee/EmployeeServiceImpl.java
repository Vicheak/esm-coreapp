package com.vicheak.coreapp.api.employee;

import com.vicheak.coreapp.api.department.DepartmentRepository;
import com.vicheak.coreapp.api.employee.web.EmployeeBaseSalaryDto;
import com.vicheak.coreapp.api.employee.web.EmployeeDto;
import com.vicheak.coreapp.api.employee.web.EmployeeStatusDto;
import com.vicheak.coreapp.api.employee.web.TransactionEmployeeDto;
import com.vicheak.coreapp.api.file.FileService;
import com.vicheak.coreapp.api.file.web.FileDto;
import com.vicheak.coreapp.pagination.PageDto;
import com.vicheak.coreapp.spec.EmployeeFilter;
import com.vicheak.coreapp.spec.EmployeeSpec;
import com.vicheak.coreapp.util.FormatUtil;
import com.vicheak.coreapp.util.PageUtil;
import com.vicheak.coreapp.util.SortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentRepository departmentRepository;
    private final BaseSalaryLogRepository baseSalaryLogRepository;
    private final FileService fileService;

    @Override
    public List<EmployeeDto> loadAllEmployees() {
        return employeeMapper.toEmployeeDto(employeeRepository.findAll());
    }

    @Override
    public EmployeeDto loadEmployeeByUuid(String uuid) {
        Employee employee = employeeRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format("Employee with uuid = %s not found...!", uuid))
                );
        return employeeMapper.toEmployeeDto(employee);
    }

    @Override
    public List<EmployeeDto> searchEmployees(Map<String, Object> requestMap) {
        //extract the data from request map
        EmployeeFilter.EmployeeFilterBuilder employeeFilterBuilder = EmployeeFilter.builder();
        if (requestMap.containsKey(Employee_.FIRST_NAME))
            employeeFilterBuilder.firstName((String) requestMap.get(Employee_.FIRST_NAME));

        if (requestMap.containsKey(Employee_.LAST_NAME))
            employeeFilterBuilder.lastName((String) requestMap.get(Employee_.LAST_NAME));

        if (requestMap.containsKey(Employee_.ADDRESS))
            employeeFilterBuilder.address((String) requestMap.get(Employee_.ADDRESS));

        if (requestMap.containsKey(Employee_.EMAIL))
            employeeFilterBuilder.email((String) requestMap.get(Employee_.EMAIL));

        if (requestMap.containsKey(Employee_.PHONE))
            employeeFilterBuilder.phone((String) requestMap.get(Employee_.PHONE));

        //using jpa specification to build dynamic query
        List<Employee> employees = employeeRepository.findAll(EmployeeSpec.builder()
                .employeeFilter(employeeFilterBuilder.build())
                .build());

        return employeeMapper.toEmployeeDto(employees);
    }

    @Override
    public List<EmployeeDto> sortEmployees(Map<String, String> requestMap) {
        //extract dynamic direction (asc/desc) and properties using jpa model generation

        //set default direction
        String direction = "asc";
        if (requestMap.containsKey(SortUtil.DIRECTION.label))
            direction = requestMap.get(SortUtil.DIRECTION.label);

        //set default property
        String field = Employee_.FIRST_NAME;
        if (requestMap.containsKey(SortUtil.FIELD.label))
            field = requestMap.get(SortUtil.FIELD.label);

        Sort sort = Sort.by(
                direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
                field.isEmpty() ? Employee_.FIRST_NAME : field
        );

        return employeeMapper.toEmployeeDto(employeeRepository.findAll(sort));
    }

    @Override
    public PageDto paginateEmployees(Map<String, String> requestMap) {
        //extract dynamic content (pageNumber & pageLimit) from request map

        //set up the default content
        int pageNumber = PageUtil.DEFAULT_PAGE_NUMBER;
        int pageLimit = PageUtil.DEFAULT_PAGE_LIMIT;

        if (requestMap.containsKey(PageUtil.PAGE_NUMBER))
            pageNumber = Integer.parseInt(requestMap.get(PageUtil.PAGE_NUMBER));

        if (requestMap.containsKey(PageUtil.PAGE_LIMIT))
            pageLimit = Integer.parseInt(requestMap.get(PageUtil.PAGE_LIMIT));

        Pageable pageable = PageUtil.getPageable(pageNumber, pageLimit);

        Page<Employee> pages = employeeRepository.findAll(pageable);

        //cast content from page of employee content to page of employee dto content
        List<EmployeeDto> employeeDtoContent = employeeMapper.toEmployeeDto(pages.getContent());

        return new PageDto(employeeDtoContent, pages);
    }

    @Override
    public void createNewEmployee(TransactionEmployeeDto transactionEmployeeDto) {
        //check a valid gender (male or female)
        if (!(transactionEmployeeDto.gender().equalsIgnoreCase("male") ||
                transactionEmployeeDto.gender().equalsIgnoreCase("female")))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Gender is not a valid one!");

        //check if email already exists
        if (employeeRepository.existsByEmailIgnoreCase(transactionEmployeeDto.email()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email conflicts resources in the system!");

        //check if phone number is correct format (all are digits including '-')
        if (!FormatUtil.checkPhoneFormat(transactionEmployeeDto.phone()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Phone number is not in correct format!");

        //check if phone number already exists
        if (employeeRepository.existsByPhone(transactionEmployeeDto.phone()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Phone conflicts resources in the system!");

        //check a valid department id
        if (!departmentRepository.existsById(transactionEmployeeDto.departmentId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Department does not exist!");

        Employee employee = employeeMapper.fromTransactionEmployeeDto(transactionEmployeeDto);
        employee.setUuid(UUID.randomUUID().toString());
        employee.setGender(employee.getGender().toLowerCase());

        employeeRepository.save(employee);
    }

    @Override
    public void updateBaseSalaryByUuid(String uuid, EmployeeBaseSalaryDto employeeBaseSalaryDto) {
        Employee employee = employeeRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format("Employee with uuid = %s not found...!", uuid))
                );

        employee.setBaseSalary(employeeBaseSalaryDto.baseSalary());

        //save history to base salary logs entity
        BaseSalaryLog baseSalaryLog = BaseSalaryLog.builder()
                .uuid(UUID.randomUUID().toString())
                .description(employeeBaseSalaryDto.description())
                .amount(employeeBaseSalaryDto.baseSalary())
                .dateTime(LocalDateTime.now())
                .employee(employee) //set the loaded employee
                .build();

        //update base salary from employee entity
        employeeRepository.save(employee);

        //save history
        baseSalaryLogRepository.save(baseSalaryLog);
    }

    @Override
    public FileDto uploadEmployeeImageByUuid(String uuid, MultipartFile file) {
        //load employee by uuid
        Employee employee = employeeRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format("Employee with uuid = %s not found...!", uuid))
                );

        FileDto fileDto = fileService.uploadSingleRestrictImage(file);
        //update the image name
        employee.setImageName(fileDto.name());

        employeeRepository.save(employee);

        return fileDto;
    }

    @Override
    public void updateEmployeeByUuid(String uuid, TransactionEmployeeDto transactionEmployeeDto) {
        //load employee by uuid
        Employee employee = employeeRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format("Employee with uuid = %s not found...!", uuid))
                );

        //check a valid gender (male or female)
        if (Objects.nonNull(transactionEmployeeDto.gender()))
            if (!(transactionEmployeeDto.gender().equalsIgnoreCase("male") ||
                    transactionEmployeeDto.gender().equalsIgnoreCase("female")))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Gender is not a valid one!");

        //check if email already exists
        if (Objects.nonNull(transactionEmployeeDto.email()))
            if (!transactionEmployeeDto.email().equalsIgnoreCase(employee.getEmail()) &&
                    employeeRepository.existsByEmailIgnoreCase(transactionEmployeeDto.email()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Email conflicts resources in the system!");

        if (Objects.nonNull(transactionEmployeeDto.phone())) {
            //check if phone number is correct format (all are digits including '-')
            if (!FormatUtil.checkPhoneFormat(transactionEmployeeDto.phone()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Phone number is not in correct format!");

            //check if phone number already exists
            if (!transactionEmployeeDto.phone().equals(employee.getPhone()) &&
                    employeeRepository.existsByPhone(transactionEmployeeDto.phone()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Phone conflicts resources in the system!");
        }

        //check a valid department id
        if (Objects.nonNull(transactionEmployeeDto.departmentId()))
            if (!departmentRepository.existsById(transactionEmployeeDto.departmentId()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Department does not exist!");

        //map non-null dto to entity
        employeeMapper.fromTransactionEmployeeDto(employee, transactionEmployeeDto);

        employeeRepository.save(employee);
    }

    @Override
    public void updateEmployeeStatusByUuid(String uuid, EmployeeStatusDto employeeStatusDto) {
        Employee employee = employeeRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format("Employee with uuid = %s not found...!", uuid))
                );

        employee.setActive(employeeStatusDto.status());

        employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployeeByUuid(String uuid) {
        Employee employee = employeeRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format("Employee with uuid = %s not found...!", uuid))
                );

        employeeRepository.delete(employee);
    }

    @Override
    public List<EmployeeDto> loadEmployeesByDepartmentName(String name) {
        return employeeMapper.toEmployeeDto(employeeRepository.findByDepartmentName(name));
    }

}
