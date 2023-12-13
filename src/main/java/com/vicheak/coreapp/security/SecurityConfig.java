package com.vicheak.coreapp.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    /*@Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        //create user admin
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("12345"))
                .roles("ADMIN")
                .build();

        //create user accountant
        UserDetails accountant = User.withUsername("accountant")
                .password(passwordEncoder.encode("12345"))
                .roles("ACCOUNTANT")
                .build();

        //create user staff
        UserDetails staff = User.withUsername("staff")
                .password(passwordEncoder.encode("12345"))
                .roles("STAFF")
                .build();

        //add users to in-memory user manager
        manager.createUser(admin);
        manager.createUser(accountant);
        manager.createUser(staff);

        return manager;
    }*/

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //customize security filter chain
        http.authorizeHttpRequests(auth -> auth
                //auth security
                //SCOPE_ : default authority checking pattern of jwt
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/v1/auth/changePassword").hasAnyAuthority("SCOPE_user:profile")

                //allow these endpoints without security
                .requestMatchers(
                        "/swagger-ui/index.html",
                        "/file/**",
                        "/auth/**",
                        "/api/v1/auth/**").permitAll()

                //department security
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/departments/**").hasAnyAuthority("SCOPE_department:read")
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/v1/departments/**").hasAnyAuthority("SCOPE_department:write")
                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/v1/departments/**").hasAnyAuthority("SCOPE_department:update")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/v1/departments/**").hasAnyAuthority("SCOPE_department:delete")

                //employee security
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/employees/**").hasAnyAuthority("SCOPE_employee:read")
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/v1/employees/**").hasAnyAuthority("SCOPE_employee:write")
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/v1/employees/uploadProfile/**").hasAnyAuthority("SCOPE_employee:uploadProfile")
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/v1/employees/**").hasAnyAuthority("SCOPE_employee:update")
                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/v1/employees/**").hasAnyAuthority("SCOPE_employee:update")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/v1/employees/**").hasAnyAuthority("SCOPE_employee:delete")

                //base salary log security
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/baseSalarylogs/**").hasAnyAuthority("SCOPE_baseSalaryLog:read")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/v1/baseSalarylogs/**").hasAnyAuthority("SCOPE_baseSalaryLog:delete")

                //gross type security
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/grossTypes/**").hasAnyAuthority("SCOPE_grossType:read")
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/v1/grossTypes/**").hasAnyAuthority("SCOPE_grossType:write")
                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/v1/grossTypes/**").hasAnyAuthority("SCOPE_grossType:update")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/v1/grossTypes/**").hasAnyAuthority("SCOPE_grossType:delete")

                //salary gross security
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/salaryGross/**").hasAnyAuthority("SCOPE_salaryGross:read")
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/v1/salaryGross/**").hasAnyAuthority("SCOPE_salaryGross:write")
                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/v1/salaryGross/**").hasAnyAuthority("SCOPE_salaryGross:update")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/v1/salaryGross/**").hasAnyAuthority("SCOPE_salaryGross:delete")

                //payment state security
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/paymentStates/**").hasAnyAuthority("SCOPE_paymentState:read")
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/v1/paymentStates/**").hasAnyAuthority("SCOPE_paymentState:write")
                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/v1/paymentStates/**").hasAnyAuthority("SCOPE_paymentState:update")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/v1/paymentStates/**").hasAnyAuthority("SCOPE_paymentState:delete")

                //salary payment security
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/v1/salaryPayments/**").hasAnyAuthority("SCOPE_salaryPayment:write")
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/v1/salaryPayments/**").hasAnyAuthority("SCOPE_salaryPayment:update")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/v1/salaryPayments/**").hasAnyAuthority("SCOPE_salaryPayment:delete")

                //report security
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/reports/**").hasAnyAuthority("SCOPE_report:view")

                //file and directory security
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/v1/files/**").hasAnyAuthority("SCOPE_file:upload")
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/files/**").hasAnyAuthority("SCOPE_file:read")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/v1/files/**").hasAnyAuthority("SCOPE_file:delete")

                //user security
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/users/me").hasAnyAuthority("SCOPE_user:profile")
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/users/**").hasAnyAuthority("SCOPE_user:read")
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/v1/users/**").hasAnyAuthority("SCOPE_user:write")
                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/v1/users/**").hasAnyAuthority("SCOPE_user:update")
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/v1/users/**").hasAnyAuthority("SCOPE_user:update")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/v1/users/**").hasAnyAuthority("SCOPE_user:delete")

                //other endpoints not specified are authenticated
                .anyRequest().authenticated());

        //use default form login
        //http.formLogin(Customizer.withDefaults());

        //configure http basic for client application
        //http.httpBasic(Customizer.withDefaults());

        //configure JWT | OAuth2 Resource Server
        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(Customizer.withDefaults()));

        //disable csrf -> cannot use default form login -> for post, put, patch, delete http
        http.csrf(AbstractHttpConfigurer::disable);

        //update API policy to STATELESS -> more secured
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public KeyPair keyPair() {
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public RSAKey rsaKey(KeyPair keyPair) {
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
        var jwkSet = new JWKSet(rsaKey);
        return new JWKSource<SecurityContext>() {
            @Override
            public List<JWK> get(JWKSelector jwkSelector, SecurityContext context) throws KeySourceException {
                return jwkSelector.select(jwkSet);
            }
        };
    }

    @Bean
    JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

}
