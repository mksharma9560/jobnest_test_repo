package com.jobnest.authms.config;

import com.jobnest.authms.util.JwtAuthenticationProvider;
import com.jobnest.authms.util.JwtTokenFilter;
import com.jobnest.authms.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtTokenFilter jwtTokenFilter;
    private final UserRepository userRepo;
    Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    // Constructor Injection
    public SecurityConfig(
            JwtAuthenticationProvider jwtAuthenticationProvider, JwtTokenFilter jwtTokenFilter, UserRepository userRepo
    ) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.jwtTokenFilter = jwtTokenFilter;
        this.userRepo = userRepo;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // Authentication Method
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.info("Configuring authentication manager...");
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    // Authorization Method
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // disabling csrf since we won't use form based login
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/auth/register", "/auth/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage()
                            );
                        }
                );
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}