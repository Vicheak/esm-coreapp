package com.vicheak.coreapp.api.salarygross.web;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionSalaryGrossDto(@NotBlank(message = "Salary Gross Name should not be blank!")
                                        String name,

                                        @NotNull(message = "Salary Gross Amount should not be null!")
                                        @Positive(message = "Salary Gross Amount should be positive!")
                                        @DecimalMin(value = "0.000001")
                                        BigDecimal amount,

                                        @NotNull(message = "Gross Type should not be null!")
                                        @Positive(message = "Gross Type Id should be positive!")
                                        Integer grossTypeId) {
}
