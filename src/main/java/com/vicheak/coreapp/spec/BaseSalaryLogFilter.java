package com.vicheak.coreapp.spec;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BaseSalaryLogFilter(String firstName,
                                  String lastName,
                                  LocalDate startDate,
                                  LocalDate endDate) {
}
