package com.flab.recipebook.user.service;

import com.flab.recipebook.user.domain.User;
import com.flab.recipebook.user.domain.dao.UserDao;
import com.flab.recipebook.user.dto.SaveUserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입 기능 테스트 - 성공")
    void addUser(){
        //given
        SaveUserDto saveUserDto = new SaveUserDto("yoon", "1008", "jm@naver.com");

        //when
        userService.save(saveUserDto);

        //then
        //dao가 호출되었는지 확인
        verify(userDao, times(1)).save(any(User.class));
    }
}