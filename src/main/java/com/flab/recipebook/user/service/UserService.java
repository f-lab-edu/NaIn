package com.flab.recipebook.user.service;

import com.flab.recipebook.user.domain.User;
import com.flab.recipebook.user.domain.UserRole;
import com.flab.recipebook.user.domain.dao.UserDao;
import com.flab.recipebook.user.dto.SaveUserDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
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

    public User findById(Long userNo){
        return userDao.findById(userNo);
    }

    public void deleteById(Long userNo) {
        userDao.deleteById(userNo);
    }
}
