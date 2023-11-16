package com.vicheak.coreapp.api.employee;

import com.vicheak.coreapp.api.employee.web.BaseSalaryLogDto;
import com.vicheak.coreapp.spec.BaseSalaryLogFilter;
import com.vicheak.coreapp.spec.BaseSalaryLogSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BaseSalaryLogServiceImpl implements BaseSalaryLogService {

    private final BaseSalaryLogRepository baseSalaryLogRepository;
    private final BaseSalaryLogMapper baseSalaryLogMapper;

    @Override
    public List<BaseSalaryLogDto> loadAllBaseSalaryLogs() {
        return baseSalaryLogMapper.toBaseSalaryLogDto(baseSalaryLogRepository.findAll());
    }

    @Override
    public BaseSalaryLogDto loadBaseSalaryLogByUuid(String uuid) {
        BaseSalaryLog baseSalaryLog = baseSalaryLogRepository.findBaseSalaryLogByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Base salary history has not been found!")
                );

        return baseSalaryLogMapper.toBaseSalaryLogDto(baseSalaryLog);
    }

    @Override
    public List<BaseSalaryLogDto> loadBaseSalaryLogsByEmployee(Map<String, String> requestMap) {
        //extract data from request map
        BaseSalaryLogFilter.BaseSalaryLogFilterBuilder baseSalaryLogFilterBuilder =
                BaseSalaryLogFilter.builder();

        if (requestMap.containsKey(Employee_.FIRST_NAME))
            baseSalaryLogFilterBuilder.firstName(requestMap.get(Employee_.FIRST_NAME));

        if (requestMap.containsKey(Employee_.LAST_NAME))
            baseSalaryLogFilterBuilder.lastName(requestMap.get(Employee_.LAST_NAME));

        if (requestMap.containsKey("startDate"))
            baseSalaryLogFilterBuilder.startDate(LocalDate.parse(requestMap.get("startDate")));

        if (requestMap.containsKey("endDate"))
            baseSalaryLogFilterBuilder.endDate(LocalDate.parse(requestMap.get("endDate")));

        //using jpa specification to build dynamic query
        List<BaseSalaryLog> baseSalaryLogs = baseSalaryLogRepository.findAll(BaseSalaryLogSpec.builder()
                .baseSalaryLogFilter(baseSalaryLogFilterBuilder.build())
                .build());

        return baseSalaryLogMapper.toBaseSalaryLogDto(baseSalaryLogs);
    }

    @Override
    public void deleteBaseSalaryLogByUuid(String uuid) {
        BaseSalaryLog baseSalaryLog = baseSalaryLogRepository.findBaseSalaryLogByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Base salary history has not been found!")
                );

        baseSalaryLogRepository.delete(baseSalaryLog);
    }

}
