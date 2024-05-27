package com.jobnest.authms.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Injecting bean of UserDetailsService implemented by UserDetailsServiceImpl
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * In AuthController.login() method, we call authenticationManager.authenticate(token)
     * then, Authentication Manager calls AuthenticationProvider's authenticate method.
     * but after creating custom provider JwtAuthenticationProvider, this method will be executed.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        // Fetching user as wrapped with UserDetails object
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // If user is not null, then we check if password matches
        if (userDetails != null) {
            // if input password and stored password matches,
            // then we can initialize UsernamePasswordAuthenticationToken.
            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
                return authenticationToken;
            }
        }
        throw new BadCredentialsException("Username or Password is not correct...please check");
    }

    // Authentication Manager checks if the token is supported by this filter
    // to avoid unnecessary checks.
    @Override
    public boolean supports(Class<?> authenticationType) {
        return UsernamePasswordAuthenticationToken.class.equals(authenticationType);
    }
}