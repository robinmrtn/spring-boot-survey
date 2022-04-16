package com.roal.survey_engine.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext()
            .getAuthentication();
    }

    public UserDetails getUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();
    }

    public boolean isAdmin() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return false;
        }
        return SecurityContextHolder.getContext()
            .getAuthentication()
            .getAuthorities()
            .stream()
            .anyMatch(authority -> "ADMIN".equals(authority.getAuthority()));
    }
}
