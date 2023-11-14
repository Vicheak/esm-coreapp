package com.vicheak.coreapp.api.department.web;

import com.vicheak.coreapp.api.department.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public List<DepartmentDto> loadAllDepartments() {
        return departmentService.loadAllDepartments();
    }

    @GetMapping("/{name}")
    public DepartmentDto loadDepartmentByName(@PathVariable("name") String name) {
        return departmentService.loadDepartmentByName(name);
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

}
