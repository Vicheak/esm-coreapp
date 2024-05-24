package com.vicheak.coreapp.api.employee.web;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record EmployeeDto(String uuid,
                          String firstName,
                          String lastName,
                          String gender,
                          LocalDate birthDate,
                          String address,
                          String email,
                          String phone,
                          BigDecimal baseSalary,
                          Boolean active,
                          String imageUri,
                          String departmentName) {
}
