package com.vicheak.coreapp.api.auth;

import com.vicheak.coreapp.api.auth.web.*;
import com.vicheak.coreapp.api.mail.Mail;
import com.vicheak.coreapp.api.mail.MailService;
import com.vicheak.coreapp.api.user.User;
import com.vicheak.coreapp.api.user.UserService;
import com.vicheak.coreapp.api.user.web.NewUserDto;
import com.vicheak.coreapp.security.CustomUserDetails;
import com.vicheak.coreapp.util.RandomUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final AuthMapper authMapper;
    private final UserService userService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtEncoder jwtEncoder;
    private JwtEncoder jwtRefreshTokenEncoder;

    @Autowired
    public void setJwtRefreshTokenEncoder(@Qualifier("jwtRefreshTokenEncoder") JwtEncoder jwtRefreshTokenEncoder) {
        this.jwtRefreshTokenEncoder = jwtRefreshTokenEncoder;
    }

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
        /*MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom(adminMail);
        helper.setTo(newUserDto.email());
        helper.setSubject("ESM Service Email Verification");
        //helper.setText(String.format("<h1>Your verification code is : %s</h1>", verifiedCode), true);

        //send verified link to verify user
        //helper.setText(String.format("<a href='http://localhost:8080/api/v1/auth/toVerify?email=%s&verifiedCode=%s'>Click here to verify your email</a>",
        //newUserDto.email(), verifiedCode), true);

        //can be used instead of model map (pass variables to template)
        Context context = new Context();
        context.setVariable("metaData", verifiedCode);
        String htmlTemplate = templateEngine.process("auth/verify-mail", context);

        helper.setText(htmlTemplate, true);

        //send to mailbox
        javaMailSender.send(mimeMessage);*/

        Mail<String> verifiedMail = new Mail<>();
        verifiedMail.setSender(adminMail);
        verifiedMail.setReceiver(newUserDto.email());
        verifiedMail.setSubject("ESM Service Email Verification");
        verifiedMail.setTemplate("auth/verify-mail");
        verifiedMail.setMetaData(verifiedCode);

        mailService.sendMail(verifiedMail);
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

    @Transactional
    @Override
    public void changePassword(ChangePasswordDto changePasswordDto, Authentication authentication) {
        //load the current logged user
        Jwt jwt = (Jwt) authentication.getPrincipal();
        User loggedUser = authRepository.findByEmailAndIsVerifiedTrueAndIsDeletedFalse(jwt.getId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                "The email is unauthorized...!")
                );

        //check the matched dto and current logged user
        if (!loggedUser.getEmail().equals(changePasswordDto.email()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "The email is unauthorized...!");

        //load verified user by email and valid password
        //valid user = already verified and active status
        /*User validUser = authRepository.findByEmailAndPasswordAndIsVerifiedTrueAndIsDeletedFalse(changePasswordDto.email(),
                        changePasswordDto.password())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                "Attempting to change password has been failed!")
                );*/

        User validUser = authRepository.findByEmailAndIsVerifiedTrueAndIsDeletedFalse(changePasswordDto.email())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                "Attempting to change password has been failed!")
                );

        if (!passwordEncoder.matches(changePasswordDto.password(), validUser.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Previous password is incorrect!");

        //set new password
        validUser.setPassword(passwordEncoder.encode(changePasswordDto.newPassword()));

        //save valid user to the database
        authRepository.save(validUser);
    }

    @Transactional
    @Override
    public void sendForgetPasswordCode(ForgetPasswordDto forgetPasswordDto) throws MessagingException {
        //check if the email is already verified
        if (authRepository.existsByEmailAndIsVerifiedTrueAndIsDeletedFalse(forgetPasswordDto.email())) {
            //generate six digit verified code
            String verifiedCode = RandomUtil.getRandomNumber();

            //update verified code into the database by specified email
            authRepository.updateVerifiedCodeByEmail(forgetPasswordDto.email(), verifiedCode);

            //send six digits code to the specified email
            Mail<String> verifiedMail = new Mail<>();
            verifiedMail.setSender(adminMail);
            verifiedMail.setReceiver(forgetPasswordDto.email());
            verifiedMail.setSubject("ESM Service Email Verification - For Forget Password");
            verifiedMail.setTemplate("auth/verify-mail");
            verifiedMail.setMetaData(verifiedCode);

            mailService.sendMail(verifiedMail);
            return;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "The email is unauthorized or it has not been verified yet!");
    }

    @Transactional
    @Override
    public void forgetPasswordReset(ForgetPasswordResetDto forgetPasswordResetDto) {
        //check if the email is already verified
        if (authRepository.existsByEmailAndIsVerifiedTrueAndIsDeletedFalse(forgetPasswordResetDto.email())) {

            //check if user has sent the forget password code to mailbox
            if (authRepository.existsByEmailAndVerifiedCodeNotNullAndIsVerifiedTrue(forgetPasswordResetDto.email())) {
                //load verified user by email and valid verified code
                User verifiedUser = authRepository.findByEmailAndVerifiedCodeAndIsDeletedFalse(
                                forgetPasswordResetDto.email(),
                                forgetPasswordResetDto.verifiedCode())
                        .orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                        "Email verification is unauthorized!")
                        );

                //reset user's password
                verifiedUser.setPassword(passwordEncoder.encode(forgetPasswordResetDto.password()));
                verifiedUser.setVerifiedCode(null);
                return;
            }

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Please send the forget password code to reset your password!");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "The email is unauthorized or it has not been verified yet!");
    }

    @Override
    public AuthDto login(LoginDto loginDto) {
        //authenticate with email and password
        Authentication auth = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password());
        //use bean dao provider in authenticate with user details service
        auth = daoAuthenticationProvider.authenticate(auth);

//        log.info("Auth Name : {}", auth.getName());
//        log.info("Auth Authorities : {}", auth.getAuthorities());

        //Instant now = Instant.now();

        //claim payload must be joined with space and default authority pattern is SCOPE_
        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        //create payload for jwt
        //claim set for access token
        /*JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .issuer("public")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.SECONDS))
                .subject("Access Token")
                .audience(List.of("public client"))
                .claim("scope", scope)
                .build();

        //claim set for refresh token
        JwtClaimsSet refreshTokenJwtClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .issuer("public")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject("Refresh Token")
                .audience(List.of("public client"))
                .claim("scope", scope)
                .build();

        //encode for access token
        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        //encode for refresh token
        String refreshToken = jwtRefreshTokenEncoder.encode(JwtEncoderParameters.from(refreshTokenJwtClaimsSet)).getTokenValue();

        return AuthDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .type("Bearer")
                .build();*/

        return AuthDto.builder()
                .type("Bearer")
                .accessToken(generateAccessToken(GenerateTokenDto.builder()
                        .auth(auth.getName())
                        .scope(scope)
                        .expiration(Instant.now().plus(1, ChronoUnit.HOURS))
                        .build()))
                .refreshToken(generateRefreshToken(GenerateTokenDto.builder()
                        .auth(auth.getName())
                        .scope(scope)
                        .expiration(Instant.now().plus(30, ChronoUnit.DAYS))
                        .build()))
                .build();
    }

    @Override
    public AuthDto refreshToken(RefreshTokenDto refreshTokenDto) {
        //authenticate the refresh token using bearer token authentication
        Authentication auth = new BearerTokenAuthenticationToken(refreshTokenDto.refreshToken());
        auth = jwtAuthenticationProvider.authenticate(auth);

        //logg the authentication information
        //log.info("Auth Principle : {}", auth.getPrincipal());
//        log.info("Auth Name : {}", auth.getName());
//        log.info("Auth Authorities : {}", auth.getAuthorities());

        Jwt jwt = (Jwt) auth.getPrincipal();

        //Instant now = Instant.now();

        //create payload for jwt
        //claim set for access token
        /*JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(jwt.getId())
                .issuer("public")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.SECONDS))
                .subject("Access Token")
                .audience(List.of("public client"))
                .claim("scope", jwt.getClaimAsString("scope"))
                .build();

        //claim set for refresh token
        JwtClaimsSet refreshTokenJwtClaimsSet = JwtClaimsSet.builder()
                .id(jwt.getId())
                .issuer("public")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject("Refresh Token")
                .audience(List.of("public client"))
                .claim("scope", jwt.getClaimAsString("scope"))
                .build();

        //encode for access token
        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        //encode for refresh token
        String refreshToken = jwtRefreshTokenEncoder.encode(JwtEncoderParameters.from(refreshTokenJwtClaimsSet)).getTokenValue();

        return AuthDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .type("Bearer")
                .build();*/

        return AuthDto.builder()
                .type("Bearer")
                .accessToken(generateAccessToken(GenerateTokenDto.builder()
                        .auth(jwt.getId())
                        .scope(jwt.getClaimAsString("scope"))
                        .expiration(Instant.now().plus(1, ChronoUnit.HOURS))
                        .build()))
                .refreshToken(generateRefreshTokenCheckDuration(GenerateTokenDto.builder()
                        .auth(jwt.getId())
                        .scope(jwt.getClaimAsString("scope"))
                        .previousToken(refreshTokenDto.refreshToken())
                        .expiration(Instant.now().plus(30, ChronoUnit.DAYS))
                        .duration(Duration.between(Instant.now(), jwt.getExpiresAt()))
                        .checkDurationNumber(7)
                        .build()))
                .build();
    }

    private String generateAccessToken(GenerateTokenDto generateTokenDto) {
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(generateTokenDto.auth())
                .issuer("public")
                .issuedAt(Instant.now())
                .expiresAt(generateTokenDto.expiration())
                .subject("Access Token")
                .audience(List.of("Public Client"))
                .claim("scope", generateTokenDto.scope())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    private String generateRefreshToken(GenerateTokenDto generateTokenDto) {
        JwtClaimsSet jwtRefreshTokenClaimsSet = JwtClaimsSet.builder()
                .id(generateTokenDto.auth())
                .issuer("public")
                .issuedAt(Instant.now())
                .expiresAt(generateTokenDto.expiration())
                .subject("Refresh Token")
                .audience(List.of("Public Client"))
                .claim("scope", generateTokenDto.scope())
                .build();
        return jwtRefreshTokenEncoder.encode(JwtEncoderParameters.from(jwtRefreshTokenClaimsSet)).getTokenValue();
    }

    private String generateRefreshTokenCheckDuration(GenerateTokenDto generateTokenDto) {
        log.info("Duration : {}", generateTokenDto.duration().toDays());
        if (generateTokenDto.duration().toDays() < generateTokenDto.checkDurationNumber()) {
            JwtClaimsSet jwtRefreshTokenClaimsSet = JwtClaimsSet.builder()
                    .id(generateTokenDto.auth())
                    .issuer("public")
                    .issuedAt(Instant.now())
                    .expiresAt(generateTokenDto.expiration())
                    .subject("Refresh Token")
                    .audience(List.of("Public Client"))
                    .claim("scope", generateTokenDto.scope())
                    .build();
            return jwtRefreshTokenEncoder.encode(JwtEncoderParameters.from(jwtRefreshTokenClaimsSet)).getTokenValue();
        }
        return generateTokenDto.previousToken();
    }

}
