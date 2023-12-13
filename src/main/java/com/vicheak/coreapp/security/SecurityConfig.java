package com.vicheak.coreapp.security;

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
import org.springframework.security.web.SecurityFilterChain;

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
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/v1/auth/changePassword").hasAnyAuthority("user:profile")

                //allow these endpoints without security
                .requestMatchers(
                        "/swagger-ui/index.html",
                        "/file/**",
                        "/auth/**",
                        "/api/v1/auth/**").permitAll()

                //department security
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/departments/**").hasAnyAuthority("department:read")
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/v1/departments/**").hasAnyAuthority("department:write")
                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/v1/departments/**").hasAnyAuthority("department:update")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/v1/departments/**").hasAnyAuthority("department:delete")

                //employee security
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/employees/**").hasAnyAuthority("employee:read")
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/v1/employees/**").hasAnyAuthority("employee:write")
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/v1/employees/uploadProfile/**").hasAnyAuthority("employee:uploadProfile")
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/v1/employees/**").hasAnyAuthority("employee:update")
                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/v1/employees/**").hasAnyAuthority("employee:update")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/v1/employees/**").hasAnyAuthority("employee:delete")

                //base salary log security
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/baseSalarylogs/**").hasAnyAuthority("baseSalaryLog:read")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/v1/baseSalarylogs/**").hasAnyAuthority("baseSalaryLog:delete")

                //gross type security
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/grossTypes/**").hasAnyAuthority("grossType:read")
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/v1/grossTypes/**").hasAnyAuthority("grossType:write")
                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/v1/grossTypes/**").hasAnyAuthority("grossType:update")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/v1/grossTypes/**").hasAnyAuthority("grossType:delete")

                //salary gross security
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/salaryGross/**").hasAnyAuthority("salaryGross:read")
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/v1/salaryGross/**").hasAnyAuthority("salaryGross:write")
                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/v1/salaryGross/**").hasAnyAuthority("salaryGross:update")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/v1/salaryGross/**").hasAnyAuthority("salaryGross:delete")

                //payment state security
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/paymentStates/**").hasAnyAuthority("paymentState:read")
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/v1/paymentStates/**").hasAnyAuthority("paymentState:write")
                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/v1/paymentStates/**").hasAnyAuthority("paymentState:update")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/v1/paymentStates/**").hasAnyAuthority("paymentState:delete")

                //salary payment security
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/v1/salaryPayments/**").hasAnyAuthority("salaryPayment:write")
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/v1/salaryPayments/**").hasAnyAuthority("salaryPayment:update")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/v1/salaryPayments/**").hasAnyAuthority("salaryPayment:delete")

                //report security
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/reports/**").hasAnyAuthority("report:view")

                //file and directory security
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/v1/files/**").hasAnyAuthority("file:upload")
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/files/**").hasAnyAuthority("file:read")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/v1/files/**").hasAnyAuthority("file:delete")

                //user security
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/users/me").hasAnyAuthority("user:profile")
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/users/**").hasAnyAuthority("user:read")
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/v1/users/**").hasAnyAuthority("user:write")
                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/v1/users/**").hasAnyAuthority("user:update")
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/v1/users/**").hasAnyAuthority("user:update")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/v1/users/**").hasAnyAuthority("user:delete")

                //other endpoints not specified are authenticated
                .anyRequest().authenticated());

        //use default form login
        //http.formLogin(Customizer.withDefaults());

        //configure http basic for client application
        http.httpBasic(Customizer.withDefaults());

        //disable csrf -> cannot use default form login -> for post, put, patch, delete http
        http.csrf(AbstractHttpConfigurer::disable);

        //update API policy to STATELESS -> more secured
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
