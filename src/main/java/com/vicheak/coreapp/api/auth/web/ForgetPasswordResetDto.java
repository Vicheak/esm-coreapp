package com.vicheak.coreapp.api.auth.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ForgetPasswordResetDto(@NotBlank(message = "Email should not be blank!")
                                     @Size(max = 80, message = "Email should not be more than 80 characters!")
                                     @Email(message = "Email should be in well-formed email address!")
                                     String email,

                                     @NotBlank(message = "Verified code should not be blank!")
                                     @Size(min = 6, message = "Verified code must be a six-digit code!")
                                     String verifiedCode,

                                     @NotBlank(message = "New Password should not be blank!")
                                     @Size(min = 8, message = "New Password must be at least 8 characters!")
                                     String password) {
}
