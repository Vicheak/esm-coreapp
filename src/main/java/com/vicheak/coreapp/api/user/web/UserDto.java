package com.vicheak.coreapp.api.user.web;

public record UserDto(String uuid,

                      String username,

                      String email,

                      String nickname,

                      Boolean status) {
}
