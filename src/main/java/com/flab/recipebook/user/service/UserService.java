package com.flab.recipebook.user.service;

import com.flab.recipebook.user.domain.User;
import com.flab.recipebook.user.domain.UserRole;
import com.flab.recipebook.user.domain.dao.UserDao;
import com.flab.recipebook.user.dto.SaveUserDto;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void save(SaveUserDto saveUserDto) {
        userDao.save(makeUser(saveUserDto));
    }

    public User makeUser(SaveUserDto saveUserDto){
        return new User(
                saveUserDto.getUserId(),
                saveUserDto.getPassword(),
                saveUserDto.getEmail(),
                UserRole.USER
        );
    }
}
