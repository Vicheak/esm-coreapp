package com.vicheak.coreapp.api.department.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DepartmentDto(@NotBlank(message = "Department name should not be blank!")
                            @Size(min = 2, max = 50, message = "Department name should be between 2 to 50 characters!")
                            String departmentName,

                            @NotBlank(message = "Department description should not be blank!")
                            @Size(min = 10, max = 255, message = "Department description should be between 10 to 255 characters!")
                            String departmentDescription,

                            @NotBlank(message = "Department phone should not be blank!")
                            @Size(min = 5, max = 50, message = "Department phone should be from 5 digits!")
                            String departmentPhone) {
}
