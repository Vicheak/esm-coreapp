package com.vicheak.coreapp.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;

@Configuration
@Slf4j
public class SecurityContextConfig {

    public String getAuthenticationName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Jwt getJwtPrincipal() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Collection<? extends GrantedAuthority> getAuthenticatedAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

    public boolean authenticatedRoles(List<String> authenticatedRoles) {
        boolean isAuthenticated = false;
        for (String auth : authenticatedRoles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("SCOPE_" + auth);
            if (this.getAuthenticatedAuthorities().contains(authority))
                isAuthenticated = true;
        }
        return isAuthenticated;
    }

}
