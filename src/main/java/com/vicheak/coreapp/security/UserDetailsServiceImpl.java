package com.vicheak.coreapp.security;

import com.vicheak.coreapp.api.user.User;
import com.vicheak.coreapp.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //load specific user by email and verify with security context
        User securityUser = userRepository.findByEmailAndIsVerifiedTrueAndIsDeletedFalse(username)
                .orElseThrow(() -> {
                    log.error("Email has not been found in the system or is unauthorized...!");
                    return new UsernameNotFoundException("Email has not been found in the system or is unauthorized...!");
                });

        log.info("Security user's username : {}", securityUser.getUsername());
        log.info("Security user's email : {}", securityUser.getEmail());

        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUser(securityUser);

        log.info("Security user's authorities : {}", customUserDetails.getAuthorities());
        log.info("Security user's authorities count : {}", customUserDetails.getAuthorities().size());

        return customUserDetails;
    }

}
