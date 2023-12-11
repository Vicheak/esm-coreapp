package com.vicheak.coreapp.api.auth;

import com.vicheak.coreapp.api.auth.web.*;
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

    /**
     * This method is used to change password of a specific user resource
     * @param changePasswordDto is the request from client
     */
    void changePassword(ChangePasswordDto changePasswordDto);

    /**
     * This method is used to send verification code for resetting password
     * @param forgetPasswordDto is the request from client
     */
    void sendForgetPasswordCode(ForgetPasswordDto forgetPasswordDto) throws MessagingException;

    /**
     * This method is used to reset password via verification code
     * @param forgetPasswordResetDto is the request from client
     */
    void forgetPasswordReset(ForgetPasswordResetDto forgetPasswordResetDto);

}
