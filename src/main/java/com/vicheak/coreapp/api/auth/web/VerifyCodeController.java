package com.vicheak.coreapp.api.auth.web;

import com.vicheak.coreapp.api.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class VerifyCodeController {

    private final AuthService authService;

    @GetMapping("/auth/verify")
    public String viewVerifyCode(ModelMap modelMap,
                                 VerifyCodeDto verifyCodeDto,
                                 @RequestParam String email) {
        modelMap.addAttribute("verifyCodeDto", verifyCodeDto);
        modelMap.addAttribute("email", email);
        return "auth/verify-code";
    }

    @PostMapping("/auth/verify")
    public String doVerifyCode(VerifyCodeDto verifyCodeDto,
                               @RequestParam String email) {

        String sixDigits = verifyCodeDto.getSixDigits();

        //verified logic from auth service
        authService.verify(new VerifyDto(email, sixDigits));

        return "auth/verify-success";
    }

}
