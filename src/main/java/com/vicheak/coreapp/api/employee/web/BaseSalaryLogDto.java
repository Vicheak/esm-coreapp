package com.vicheak.coreapp.api.employee.web;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BaseSalaryLogDto(String uuid,
                               String description,
                               BigDecimal amount,
                               LocalDateTime dateTime,
                               String employeeName) {
}
