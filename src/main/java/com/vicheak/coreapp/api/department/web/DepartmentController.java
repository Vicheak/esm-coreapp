package com.vicheak.coreapp.api.department.web;

import com.vicheak.coreapp.api.department.DepartmentService;
import com.vicheak.coreapp.api.employee.EmployeeService;
import com.vicheak.coreapp.api.employee.web.EmployeeDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    @GetMapping
    public List<DepartmentDto> loadAllDepartments() {
        return departmentService.loadAllDepartments();
    }

    @GetMapping("/{name}")
    public DepartmentDto loadDepartmentByName(@PathVariable("name") String name) {
        return departmentService.loadDepartmentByName(name);
    }

    @GetMapping("/search")
    public List<DepartmentDto> searchDepartments(@RequestParam(required = false) Map<String, String> requestMap) {
        return departmentService.searchDepartments(requestMap);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createNewDepartment(@RequestBody @Valid DepartmentDto departmentDto) {
        departmentService.createNewDepartment(departmentDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{name}")
    public void updateDepartmentByName(@PathVariable("name") String name,
                                       @RequestBody DepartmentDto departmentDto) {
        departmentService.updateDepartmentByName(name, departmentDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{name}")
    public void deleteDepartmentByName(@PathVariable("name") String name) {
        departmentService.deleteDepartmentByName(name);
    }

    @GetMapping("/{name}/employees")
    public List<EmployeeDto> loadEmployeesByDepartmentName(@PathVariable("name") String name) {
        return employeeService.loadEmployeesByDepartmentName(name);
    }

}
