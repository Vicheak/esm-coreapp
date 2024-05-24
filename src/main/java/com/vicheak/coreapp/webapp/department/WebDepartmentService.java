package com.vicheak.coreapp.webapp.department;

import java.util.List;

public interface WebDepartmentService {

    /**
     * This method is used to load all departments for web app template
     * @return List<WebDepartmentDto>
     */
    List<WebDepartmentDto> loadAllDepartments();

}
