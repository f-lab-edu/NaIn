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
        //id 중복검사
        if (existUserId(saveUserDto.getUserId())) {
            throw new IllegalStateException("아이디가 사용중 입니다.");
        }
        //email 중복검사
        if (existEmail(saveUserDto.getEmail())) {
            throw new IllegalStateException("이메일이 사용중 입니다.");
        }
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

    public boolean existUserId(String userId){
        return userDao.existUserId(userId);
    }

    public boolean existEmail(String email){
        return userDao.existEmail(email);
    }
}
