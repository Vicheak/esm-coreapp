package com.vicheak.coreapp.api.auth.web;

import com.vicheak.coreapp.api.auth.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody @Valid RegisterDto registerDto) throws MessagingException {
        authService.register(registerDto);
        return Map.of("message", "Please check your email for verification code!");
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

}
