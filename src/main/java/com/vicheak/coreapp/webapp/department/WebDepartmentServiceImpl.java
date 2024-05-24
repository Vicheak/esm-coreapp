package com.vicheak.coreapp.webapp.department;

import com.vicheak.coreapp.api.department.DepartmentMapper;
import com.vicheak.coreapp.api.department.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebDepartmentServiceImpl implements WebDepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public List<WebDepartmentDto> loadAllDepartments() {
        return departmentMapper.toWebDepartmentDto(departmentRepository.findAll());
    }

}
