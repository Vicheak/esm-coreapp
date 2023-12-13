package com.vicheak.coreapp.api.user.web;

import com.vicheak.coreapp.api.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserDto me(Authentication authentication){
        return userService.me(authentication);
    }

    @GetMapping
    public List<UserDto> loadAllUsers() {
        return userService.loadAllUsers();
    }

    @GetMapping("/{uuid}")
    public UserDto loadUserByUuid(@PathVariable("uuid") String uuid) {
        return userService.loadUserByUuid(uuid);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createNewUser(@RequestBody @Valid NewUserDto newUserDto) {
        userService.createNewUser(newUserDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{uuid}")
    public void updateUserByUuid(@PathVariable("uuid") String uuid,
                                 @RequestBody @Valid UpdateUserDto updateUserDto) {
        userService.updateUserByUuid(uuid, updateUserDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{uuid}")
    public void updateIsDeletedByUuid(@PathVariable("uuid") String uuid,
                                      @RequestBody @Valid IsDeletedUserDto isDeletedUserDto){
        userService.updateIsDeletedByUuid(uuid, isDeletedUserDto.isDeleted());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{uuid}")
    public void deleteUserByUuid(@PathVariable("uuid") String uuid){
        userService.deleteUserByUuid(uuid);
    }

}
