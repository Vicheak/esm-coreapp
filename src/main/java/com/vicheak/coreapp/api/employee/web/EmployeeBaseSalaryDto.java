package com.vicheak.coreapp.api.employee.web;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record EmployeeBaseSalaryDto(@NotNull(message = "Base Salary cannot be null!")
                                    @Positive(message = "Base Salary must be positive!")
                                    @DecimalMin(value = "0.000001")
                                    BigDecimal baseSalary,

                                    @NotBlank(message = "Description should not be blank!")
                                    @Size(min = 5, message = "Description minimum length is from 5 characters!")
                                    String description) {
}
