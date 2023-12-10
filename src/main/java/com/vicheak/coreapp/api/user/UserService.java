package com.vicheak.coreapp.api.user;

import com.vicheak.coreapp.api.user.web.NewUserDto;

public interface UserService {

    /**
     * This method is used to create new user resource into the system
     * @param newUserDto is the request from client
     */
    void createNewUser(NewUserDto newUserDto);

}
