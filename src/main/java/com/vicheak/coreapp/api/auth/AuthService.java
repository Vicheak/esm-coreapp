package com.vicheak.coreapp.api.auth;

import com.vicheak.coreapp.api.auth.web.RegisterDto;
import com.vicheak.coreapp.api.auth.web.VerifyDto;
import jakarta.mail.MessagingException;

public interface AuthService {

    /**
     * This method is used to register new user resource into the system
     * @param registerDto is the request from client
     */
    void register(RegisterDto registerDto) throws MessagingException;

    /**
     * This method is used to verify user resource (verify via six-digit code)
     * @param verifyDto is the request from client
     */
    void verify(VerifyDto verifyDto);

}
