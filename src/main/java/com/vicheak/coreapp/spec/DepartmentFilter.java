package com.vicheak.coreapp.spec;

import lombok.Builder;

@Builder
public record DepartmentFilter(String name,
                               String phone) {
}
