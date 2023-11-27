package com.vicheak.coreapp.spec;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SalaryGrossFilter(String name,
                                BigDecimal amount) {
}
