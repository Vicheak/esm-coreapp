package com.vicheak.coreapp.api.employee;

import com.vicheak.coreapp.api.employee.web.BaseSalaryLogDto;

import java.util.List;
import java.util.Map;

public interface BaseSalaryLogService {

    /**
     * This method is used to load all base salary histories
     * @return List<BaseSalaryLogDto>
     */
    List<BaseSalaryLogDto> loadAllBaseSalaryLogs();

    /**
     * This method is used to load specific base salary history by uuid
     * @param uuid is the path parameter from client
     * @return BaseSalaryLogDto
     */
    BaseSalaryLogDto loadBaseSalaryLogByUuid(String uuid);

    /**
     * This method is used to load base salary histories by any employee
     * @param requestMap is the request map from client
     * @return List<BaseSalaryLogDto>
     */
    List<BaseSalaryLogDto> loadBaseSalaryLogsByEmployee(Map<String, String> requestMap);

    /**
     * This method is used to delete specific base salary history by uuid
     * @param uuid is the path parameter from client
     */
    void deleteBaseSalaryLogByUuid(String uuid);

}
