package com.vicheak.coreapp.api.auth;

import com.vicheak.coreapp.api.auth.web.RegisterDto;
import com.vicheak.coreapp.api.auth.web.VerifyDto;
import com.vicheak.coreapp.api.user.User;
import com.vicheak.coreapp.api.user.UserService;
import com.vicheak.coreapp.api.user.web.NewUserDto;
import com.vicheak.coreapp.util.RandomUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final AuthMapper authMapper;
    private final UserService userService;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String adminMail;

    @Transactional
    @Override
    public void register(RegisterDto registerDto) throws MessagingException {
        //map from register dto to new user dto
        NewUserDto newUserDto = authMapper.mapFromRegisterDtoToNewUserDto(registerDto);
        userService.createNewUser(newUserDto);

        //generate six digit verified code
        String verifiedCode = RandomUtil.getRandomNumber();

        //update verified code into the database
        authRepository.updateVerifiedCode(newUserDto.username(), verifiedCode);

        //send verified code via email
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom(adminMail);
        helper.setTo(newUserDto.email());
        helper.setSubject("ESM Service Email Verification");
        helper.setText(String.format("<h1>Your verification code is : %s</h1>", verifiedCode), true);

        //send verified link to verify user
        //helper.setText(String.format("<a href='http://localhost:8080/api/v1/auth/toVerify?email=%s&verifiedCode=%s'>Click here to verify your email</a>",
        //newUserDto.email(), verifiedCode), true);

        //send to mailbox
        javaMailSender.send(mimeMessage);
    }

    @Transactional
    @Override
    public void verify(VerifyDto verifyDto) {
        //load verified user by email and valid verified code
        User verifiedUser = authRepository.findByEmailAndVerifiedCodeAndIsDeletedFalse(verifyDto.email(),
                        verifyDto.verifiedCode())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                "Email verification has been failed!")
                );

        //update verified status and verified code
        verifiedUser.setIsVerified(true);
        verifiedUser.setVerifiedCode(null);

        //save verified user to the database
        authRepository.save(verifiedUser);
    }

}