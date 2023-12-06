package com.vicheak.coreapp.api.slip.web;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.hibernate.validator.constraints.Range;

import java.util.Set;

@Builder
public record TransactionSalaryPaymentDto(@NotNull(message = "Employee UUID should not be null!")
                                          String employeeUuid,

                                          @NotNull(message = "Month should not be null!")
                                          @Positive(message = "Month should be positive!")
                                          @Range(min = 1, max = 12, message = "Month should be between 1 and 12!")
                                          Integer month,

                                          @NotNull(message = "Year should not be null!")
                                          @Positive(message = "Year should be positive!")
                                          @Range(min = 2023, message = "Year should be minimum 2023!")
                                          Integer year,

                                          //allow salary gross id list to be null
                                          //@NotEmpty(message = "Salary Gross Id list should not be empty!")
                                          Set<@NotNull(message = "Salary Gross Id should not be null!")
                                          @Positive(message = "Salary Gross Id should be positive!")
                                                  Integer> salaryGrossIds) {
}
