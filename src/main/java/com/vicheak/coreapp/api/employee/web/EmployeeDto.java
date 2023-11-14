package com.vicheak.coreapp.api.employee.web;

import java.math.BigDecimal;
import java.time.LocalDate;

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
