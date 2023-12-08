package com.vicheak.coreapp.api.report.web;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BaseSalaryEmployeeDetailDto(String description,

                                          BigDecimal amount,

                                          LocalDateTime dateTime) {
}
