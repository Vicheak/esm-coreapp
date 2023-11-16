package com.vicheak.coreapp.api.employee.web;

import com.vicheak.coreapp.api.employee.BaseSalaryLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/baseSalarylogs")
@RequiredArgsConstructor
public class BaseSalaryLogController {

    private final BaseSalaryLogService baseSalaryLogService;

    @GetMapping
    public List<BaseSalaryLogDto> loadAllBaseSalaryLogs() {
        return baseSalaryLogService.loadAllBaseSalaryLogs();
    }

    @GetMapping("/{uuid}")
    public BaseSalaryLogDto loadBaseSalaryLogByUuid(@PathVariable("uuid") String uuid) {
        return baseSalaryLogService.loadBaseSalaryLogByUuid(uuid);
    }

    @GetMapping("/search")
    public List<BaseSalaryLogDto> loadBaseSalaryLogsByEmployee(@RequestParam(required = false) Map<String, String> requestMap) {
        return baseSalaryLogService.loadBaseSalaryLogsByEmployee(requestMap);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{uuid}")
    public void deleteBaseSalaryLogByUuid(@PathVariable("uuid") String uuid) {
        baseSalaryLogService.deleteBaseSalaryLogByUuid(uuid);
    }

}
