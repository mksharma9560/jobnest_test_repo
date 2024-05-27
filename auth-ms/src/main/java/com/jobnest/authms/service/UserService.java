package com.jobnest.authms.service;

import com.jobnest.authms.dto.UserDto;
import com.jobnest.authms.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    boolean saveUser(User user);

    List<UserDto> getUsers();

}