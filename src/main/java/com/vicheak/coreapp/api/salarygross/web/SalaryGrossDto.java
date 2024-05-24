package com.vicheak.coreapp.api.salarygross.web;

import java.math.BigDecimal;

public record SalaryGrossDto(String name,
                             BigDecimal amount,
                             String grossType) {
}
