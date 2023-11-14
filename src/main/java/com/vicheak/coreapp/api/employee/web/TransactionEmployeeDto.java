package com.vicheak.coreapp.api.employee.web;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record TransactionEmployeeDto(@NotBlank(message = "First name should not be blank!")
                                     @Size(max = 50, message = "First name maximum length is 50 characters!")
                                     String firstName,

                                     @NotBlank(message = "First name should not be blank!")
                                     @Size(max = 50, message = "First name maximum length is 50 characters!")
                                     String lastName,

                                     @NotBlank(message = "Gender should not be blank!")
                                     @Size(max = 10, message = "Gender maximum length is 10 characters!")
                                     String gender,

                                     @NotNull(message = "Birth date should not be null!")
                                     @Past(message = "Birth date must be in the past!")
                                     LocalDate birthDate,

                                     @NotBlank(message = "Address should not be blank!")
                                     String address,

                                     @NotBlank(message = "Email should not be blank!")
                                     @Size(max = 100, message = "Email maximum length is 100 characters!")
                                     String email,

                                     @NotBlank(message = "Phone number should not be blank!")
                                     @Size(max = 100, message = "Phone number maximum length is 50 characters!")
                                     String phone,

                                     @NotNull(message = "Department should not be null!")
                                     @Positive(message = "Department code must be positive!")
                                     Long departmentId) {
}
