package com.vicheak.coreapp.api.slip.web;

import jakarta.validation.constraints.NotBlank;

public record PaymentStateDto(@NotBlank(message = "Payment Status should not be blank!")
                              String status) {
}
