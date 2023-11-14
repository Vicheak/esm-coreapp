package com.vicheak.coreapp.api.department;

import com.vicheak.coreapp.api.department.web.DepartmentDto;

import java.util.List;
import java.util.Map;

public interface DepartmentService {

    /**
     * This method is used to load all departments from db
     * @return List<DepartmentDto>
     */
    List<DepartmentDto> loadAllDepartments();

    /**
     * This method is used to load department by name
     * @param name is the path parameter from client
     * @return DepartmentDto
     */
    DepartmentDto loadDepartmentByName(String name);

    /**
     * This method is used to search any departments from criteria
     * @param requestMap is the request parameter from client
     * @return List<DepartmentDto>
     */
    List<DepartmentDto> searchDepartments(Map<String, String> requestMap);

    /**
     * This method is used to create new department resource
     * @param departmentDto is the request fromm client
     */
    void createNewDepartment(DepartmentDto departmentDto);

    /**
     * This method is used to update department resource by unique name
     * @param name is the path parameter from client
     * @param departmentDto is the request from client
     */
    void updateDepartmentByName(String name, DepartmentDto departmentDto);

    /**
     * This method is used to delete department resource by unique name
     * @param name is the path parameter from client
     */
    void deleteDepartmentByName(String name);

}
