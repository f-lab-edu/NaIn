package com.flab.recipebook.user.service;

import com.flab.recipebook.user.domain.User;
import com.flab.recipebook.user.domain.UserRole;
import com.flab.recipebook.user.domain.dao.UserDao;
import com.flab.recipebook.user.dto.SaveUserDto;
import com.flab.recipebook.user.dto.UpdateUserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
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
    @DisplayName("회원가입 시 초기값 테스트")
    void initUser(){
        //given
        SaveUserDto saveUserDto = new SaveUserDto("yoon", "1008", "jm@naver.com");

        //when
        User user = userService.makeUserFromSaveUserDto(saveUserDto);

        //then
        assertThat(user.getUserRole()).isEqualTo(UserRole.USER);
        assertThat(user.getCreateDate().toLocalDate()).isEqualTo(LocalDateTime.now().toLocalDate());
        assertThat(user.getModifyDate().toLocalDate()).isEqualTo(LocalDateTime.now().toLocalDate());
    }

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

    @Test
    @DisplayName("유저를 수정할 때 modifyPassword 가 null 이면 현재 패스워드가 저장된다.")
    void notUseModifyPassword(){
        //given
        UpdateUserDto updateUserDto = new UpdateUserDto(1L, "current1234!", null, "jm@naver.com");

        //when
        User user = userService.makeUserFromUpdateUserDto(updateUserDto);

        //then
        assertThat(user.getPassword()).isEqualTo(updateUserDto.getCurrentPassword());
        assertThat(user.getPassword()).isNotEqualTo(updateUserDto.getModifyPassword());
    }
}
