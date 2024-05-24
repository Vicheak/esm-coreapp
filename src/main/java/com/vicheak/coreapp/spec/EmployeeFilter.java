package com.vicheak.coreapp.spec;

import lombok.Builder;

@Builder
public record EmployeeFilter(String firstName,
                             String lastName,
                             String address,
                             String email,
                             String phone) {
}
