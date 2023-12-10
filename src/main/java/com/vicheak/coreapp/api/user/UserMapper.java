package com.vicheak.coreapp.api.user;

import com.vicheak.coreapp.api.user.web.NewUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromNewUserDto(NewUserDto newUserDto);

}
