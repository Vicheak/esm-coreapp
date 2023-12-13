package com.vicheak.coreapp.api.user;

import com.vicheak.coreapp.api.user.web.NewUserDto;
import com.vicheak.coreapp.api.user.web.UpdateUserDto;
import com.vicheak.coreapp.api.user.web.UserDto;
import com.vicheak.coreapp.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto me(Authentication authentication) {
        /*log.info("Auth Principle : {}", authentication.getPrincipal());
        log.info("Auth Name : {}", authentication.getName());
        log.info("Auth Authorities : {}", authentication.getAuthorities());
        log.info("Auth Details : {}", authentication.getDetails());
        log.info("Auth Credentials : {}", authentication.getCredentials());*/

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return userMapper.toUserDto(customUserDetails.getUser());
    }

    @Override
    public List<UserDto> loadAllUsers() {
        return userMapper.toUserDto(userRepository.findAll());
    }

    @Override
    public UserDto loadUserByUuid(String uuid) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with uuid = %s has not been found!"
                                        .formatted(uuid))
                );

        return userMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public void createNewUser(NewUserDto newUserDto) {
        //check if username already exists
        if (userRepository.existsByUsernameIgnoreCase(newUserDto.username()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Username already existed in the system!");

        //check if email already exists
        if (userRepository.existsByEmailIgnoreCase(newUserDto.email()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email already existed in the system!");

        //check if role id does not exist
        boolean isNotFound = newUserDto.roleIds().stream()
                .anyMatch(roleId -> !roleRepository.existsById(roleId));

        if (isNotFound)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Role ID has not been found in the system!");

        //map from dto to entity
        User user = userMapper.fromNewUserDto(newUserDto);

        user.setUuid(UUID.randomUUID().toString());
        //encrypt raw password via bcrypt algorithm
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsVerified(false);
        user.setIsDeleted(false);

        //set user role
        Set<Role> roles = newUserDto.roleIds().stream()
                .map(roleId -> Role.builder()
                        .id(roleId)
                        .build())
                .collect(Collectors.toSet());

        user.setRoles(roles);

        //save user into the database
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updateUserByUuid(String uuid, UpdateUserDto updateUserDto) {
        //load user by uuid
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with uuid = %s has not been found!"
                                        .formatted(uuid))
                );

        //check if the user status is inactive
        if(user.getIsDeleted())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User status is inactive!");

        //check if username already existed
        if (Objects.nonNull(updateUserDto.username()))
            if (!updateUserDto.username().equalsIgnoreCase(user.getUsername()) &&
                    userRepository.existsByUsernameIgnoreCase(updateUserDto.username()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Username conflicts resources in the system!");

        //check if email already existed
        if (Objects.nonNull(updateUserDto.email()))
            if (!updateUserDto.email().equalsIgnoreCase(user.getEmail()) &&
                    userRepository.existsByEmailIgnoreCase(updateUserDto.email()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Email conflicts resources in the system!");

        //map from dto to entity (ignore null value)
        userMapper.fromUpdateUserDtoToUser(user, updateUserDto);

        //save user to the database
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updateIsDeletedByUuid(String uuid, Boolean isDeleted) {
        //check if user exists by uuid
        if (userRepository.checkUserByUuid(uuid)) {
            userRepository.updateIsDeletedStatusByUuid(uuid, isDeleted);
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User with uuid = %s has not been found!".formatted(uuid));
    }

    @Transactional
    @Override
    public void deleteUserByUuid(String uuid) {
        //load user by uuid
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User with uuid = %s has not been found!"
                                        .formatted(uuid))
                );

        //delete user from the database
        userRepository.delete(user);
    }

}
