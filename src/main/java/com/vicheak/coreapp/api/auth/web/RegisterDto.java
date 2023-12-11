package com.vicheak.coreapp.api.auth.web;

import jakarta.validation.constraints.*;

import java.util.Set;

public record RegisterDto(@NotBlank(message = "Username should not be blank!")
                          @Size(max = 80, message = "Username should not be more than 80 characters!")
                          String username,

                          @NotBlank(message = "Email should not be blank!")
                          @Size(max = 80, message = "Email should not be more than 80 characters!")
                          @Email(message = "Email should be in well-formed email address!")
                          String email,

                          @NotBlank(message = "Password should not be blank!")
                          @Size(min = 8, message = "Password must be at least 8 characters!")
                          String password,

                          @Size(max = 30, message = "Nickname should not be more than 30 characters!")
                          String nickname,

                          @NotNull(message = "Role IDs should not be null!")
                          @Size(min = 1, message = "Role IDs should be at least associated with one role!")
                          Set<@Positive(message = "Role ID should be positive!") Integer> roleIds) {
}
