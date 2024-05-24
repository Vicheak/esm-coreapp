package com.vicheak.coreapp.api.salarygross.web;

import jakarta.validation.constraints.NotBlank;

public record GrossTypeDto(@NotBlank(message = "Gross type name should not be blank!")
                           String name,

                           @NotBlank(message = "Gross type description should not be blank!")
                           String description) {
}
