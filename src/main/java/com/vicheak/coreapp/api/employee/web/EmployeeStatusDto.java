package com.vicheak.coreapp.api.employee.web;

import jakarta.validation.constraints.NotNull;

public record EmployeeStatusDto(@NotNull(message = "Status cannot be null!")
                                Boolean status) {
}
