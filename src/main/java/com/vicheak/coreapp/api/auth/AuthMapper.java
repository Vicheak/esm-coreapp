package com.vicheak.coreapp.api.auth;

import com.vicheak.coreapp.api.auth.web.RegisterDto;
import com.vicheak.coreapp.api.user.web.NewUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    NewUserDto mapFromRegisterDtoToNewUserDto(RegisterDto registerDto);

}