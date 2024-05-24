package com.vicheak.coreapp.api.department;

import com.vicheak.coreapp.api.department.web.DepartmentDto;
import com.vicheak.coreapp.webapp.department.WebDepartmentDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(target = "departmentName", source = "name")
    @Mapping(target = "departmentDescription", source = "description")
    @Mapping(target = "departmentPhone", source = "phone")
    DepartmentDto toDepartmentDto(Department department);

    List<DepartmentDto> toDepartmentDto(List<Department> departments);

    @Mapping(target = "name", source = "departmentName")
    @Mapping(target = "description", source = "departmentDescription")
    @Mapping(target = "phone", source = "departmentPhone")
    Department fromDepartmentDto(DepartmentDto departmentDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "name", source = "departmentName")
    @Mapping(target = "description", source = "departmentDescription")
    @Mapping(target = "phone", source = "departmentPhone")
    void fromDepartmentDto(@MappingTarget Department department, DepartmentDto departmentDto);

    //mapper for web app
    WebDepartmentDto toWebDepartmentDto(Department department);

    List<WebDepartmentDto> toWebDepartmentDto(List<Department> departments);

}
