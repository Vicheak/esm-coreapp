package com.vicheak.coreapp.api.slip.web;

import jakarta.validation.constraints.NotNull;

public record UpdatePaymentStateDto(@NotNull(message = "isPaid should not be null!")
                                    Boolean isPaid) {
}
