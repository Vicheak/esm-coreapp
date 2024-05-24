package com.vicheak.coreapp.api.user;

import com.vicheak.coreapp.api.user.web.NewUserDto;
import com.vicheak.coreapp.api.user.web.UpdateUserDto;
import com.vicheak.coreapp.api.user.web.UserDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "status", source = "isDeleted")
    UserDto toUserDto(User user);

    List<UserDto> toUserDto(List<User> users);

    User fromNewUserDto(NewUserDto newUserDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromUpdateUserDtoToUser(@MappingTarget User user, UpdateUserDto updateUserDto);

}
