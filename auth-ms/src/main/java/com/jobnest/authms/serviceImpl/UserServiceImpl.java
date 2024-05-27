package com.jobnest.authms.serviceImpl;

import com.jobnest.authms.dto.UserDto;
import com.jobnest.authms.entities.User;
import com.jobnest.authms.repository.UserRepository;
import com.jobnest.authms.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDto userDto;
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    // Constructor Injection
    public UserServiceImpl(UserDto userDto, BCryptPasswordEncoder passwordEncoder, UserRepository userRepo) {
        this.userDto = userDto;
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    @Override
    public boolean saveUser(User user) {
        Optional<User> userExist = userRepo.findByUsername(user.getUsername());
        if (userExist.isPresent()) {
            log.info("User already exist");
            return false;
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        log.info("Encoded password is: {}", encodedPassword);
        User savedUser = userRepo.save(user);
        log.info("User registered successfully: {}", user.getUsername());
        return true;
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> userList = userRepo.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : userList) {
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setActive(user.isEnabled());
            userDto.setAccountNonLocked(user.isAccountNonLocked());
            userDto.setAccountNonExpired(user.isAccountNonExpired());
            userDto.setCredentialsNonExpired(user.isCredentialsNonExpired());
            userDto.setEnabled(user.isEnabled());
            userDtos.add(userDto);
        }
        return userDtos;
    }
}