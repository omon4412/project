package com.omon4412.authservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@RequiredArgsConstructor
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String usernameOrEmail = authentication.getName();
        UserDetails userDetails = userDetailsService.loadUserByUsername(usernameOrEmail);
        additionalAuthenticationChecks(userDetails, (UsernamePasswordAuthenticationToken) authentication);
        return new UsernamePasswordAuthenticationToken(userDetails,
                authentication.getCredentials(), userDetails.getAuthorities());
    }
}
