package com.flab.recipebook.user.service;

import com.flab.recipebook.user.dao.UserDao;
import com.flab.recipebook.user.dto.SaveUserDto;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void save(SaveUserDto saveUserDto) {
        userDao.save(saveUserDto);
    }

}
