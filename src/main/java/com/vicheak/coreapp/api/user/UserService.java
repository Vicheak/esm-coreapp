package com.vicheak.coreapp.api.user;

import com.vicheak.coreapp.api.user.web.NewUserDto;
import com.vicheak.coreapp.api.user.web.UpdateUserDto;
import com.vicheak.coreapp.api.user.web.UserDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    /**
     * This method is used to load current user's profile
     * @param authentication is the request from client
     * @return UserDto
     */
    UserDto me(Authentication authentication);

    /**
     * This method is used to load all user resources from the system
     * @return List<UserDto>
     */
    List<UserDto> loadAllUsers();

    /**
     * This method is used to load specific user resource by uuid
     * @param uuid is the path parameter from client
     * @return UserDto
     */
    UserDto loadUserByUuid(String uuid);

    /**
     * This method is used to create new user resource into the system
     * @param newUserDto is the request from client
     */
    void createNewUser(NewUserDto newUserDto);

    /**
     * This method is used to update specific user resource by uuid
     * @param uuid is the path parameter from client
     * @param updateUserDto is the request from client
     */
    void updateUserByUuid(String uuid, UpdateUserDto updateUserDto);

    /**
     * This method is used to update user status by uuid
     * @param uuid is the path parameter from client
     * @param isDeleted is the request from client
     */
    void updateIsDeletedByUuid(String uuid, Boolean isDeleted);

    /**
     * This method is used to delete user by uuid
     * @param uuid is the path parameter from client
     */
    void deleteUserByUuid(String uuid);

}
