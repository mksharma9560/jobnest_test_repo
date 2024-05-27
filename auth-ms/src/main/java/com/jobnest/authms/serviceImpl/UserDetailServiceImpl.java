package com.jobnest.authms.serviceImpl;

import com.jobnest.authms.entities.User;
import com.jobnest.authms.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
 * Custom UserDetailsService implementation
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    Logger LOGGER = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    @Autowired
    UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.warn("Executing LoadUserByUsername()");
        User user = userRepo.findByUsername(username).orElseThrow(() -> {
            LOGGER.warn("User " + username + " not found.");
            return new UsernameNotFoundException("User " + username + " not found.");
        });
        return new User(user.getUsername(), user.getPassword());
    }
}