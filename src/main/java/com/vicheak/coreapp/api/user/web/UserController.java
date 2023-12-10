package com.vicheak.coreapp.api.user.web;

import com.vicheak.coreapp.api.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createNewUser(@RequestBody @Valid NewUserDto newUserDto) {
        userService.createNewUser(newUserDto);
    }

}
