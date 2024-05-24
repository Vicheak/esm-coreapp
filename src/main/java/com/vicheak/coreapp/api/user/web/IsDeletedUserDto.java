package com.vicheak.coreapp.api.user.web;

import jakarta.validation.constraints.NotNull;

public record IsDeletedUserDto(@NotNull(message = "Is Deleted should not be null!")
                               Boolean isDeleted) {
}
