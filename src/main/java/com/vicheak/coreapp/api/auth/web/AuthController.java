package com.vicheak.coreapp.api.auth.web;

import com.vicheak.coreapp.api.auth.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${app.base-uri}")
    private String appBaseUri;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody @Valid RegisterDto registerDto) throws MessagingException {
        authService.register(registerDto);
        return Map.of("message", "Please check your email for verification code!",
                "verifyUri", appBaseUri + "auth/verify?email=" + registerDto.email());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verify")
    public Map<String, String> verify(@RequestBody @Valid VerifyDto verifyDto) {
        authService.verify(verifyDto);
        return Map.of("message", "Congratulation! Your email has been verified...!");
    }

    @GetMapping("/toVerify")
    public Map<String, String> verify(@RequestParam(name = "email") String email,
                                      @RequestParam(name = "verifiedCode") String verifiedCode) {
        authService.verify(new VerifyDto(email, verifiedCode));
        return Map.of("message", "Congratulation! Your email has been verified...!");
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/changePassword")
    public Map<String, String> changePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto,
                                              Authentication authentication) {
        authService.changePassword(changePasswordDto, authentication);
        return Map.of("message", "Your password has been reset successfully...!");
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/sendForgetPasswordCode")
    public Map<String, String> sendForgetPasswordCode(@RequestBody @Valid ForgetPasswordDto forgetPasswordDto) throws MessagingException {
        authService.sendForgetPasswordCode(forgetPasswordDto);
        return Map.of("message", "Please check your email for verification code!");
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/forgetPasswordReset")
    public Map<String, String> forgetPasswordReset(@RequestBody @Valid ForgetPasswordResetDto forgetPasswordResetDto) {
        authService.forgetPasswordReset(forgetPasswordResetDto);
        return Map.of("message", "Your password has been reset successfully...!");
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public AuthDto login(@RequestBody @Valid LoginDto loginDto){
        return authService.login(loginDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/token")
    public AuthDto refreshToken(@RequestBody @Valid RefreshTokenDto refreshTokenDto){
        return authService.refreshToken(refreshTokenDto);
    }

}
