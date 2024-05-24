package com.vicheak.coreapp.api.user.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserDto(@Size(max = 80, message = "Username should not be more than 80 characters!")
                            String username,

                            @Size(max = 80, message = "Email should not be more than 80 characters!")
                            @Email(message = "Email should be in well-formed email address!")
                            String email,

                            @Size(max = 30, message = "Nickname should not be more than 30 characters!")
                            String nickname) {
}
