package com.flab.recipebook.user.domain.dao;

import com.flab.recipebook.user.domain.User;
import com.flab.recipebook.user.domain.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
class UserDaoTest {

    @Autowired
    UserDao userDao;

    @Test
    @DisplayName("아이디 중복 테스트 - 중복일 경우 true를 반환한다.")
    void existUserNo_true(){
        //given
        User user = new User(1L, "yoon", "abc1234!@#", "jm@naver.com", UserRole.USER, LocalDateTime.now(), LocalDateTime.now(), null);

        //when
        userDao.save(user);

        //then
        assertThat(userDao.existUserId(user.getUserId())).isEqualTo(true);
    }

    @Test
    @DisplayName("아이디 중복 테스트 - 중복이 아닐 경우 false를 반환한다.")
    void existUserNo_false(){
        //given
        User user = new User(1L, "yoon", "abc1234!@#", "jm@naver.com", UserRole.USER, LocalDateTime.now(), LocalDateTime.now(), null);

        //when
        userDao.save(user);

        //then
        assertThat(userDao.existUserId("kim")).isEqualTo(false);
    }

    @Test
    @DisplayName("이메일 중복 테스트 - 중복일 경우 true를 반환한다.")
    void existEmail_true(){
        //given
        User user = new User(1L, "yoon", "abc1234!@#", "jm@naver.com", UserRole.USER, LocalDateTime.now(), LocalDateTime.now(), null);

        //when
        userDao.save(user);

        //then
        assertThat(userDao.existEmail(user.getEmail())).isEqualTo(true);
    }

    @Test
    @DisplayName("이메일 중복 테스트 - 중복이 아닐 경우 false를 반환한다.")
    void existEamil_false(){
        //given
        User user = new User(1L, "yoon", "abc1234!@#", "jm@naver.com", UserRole.USER, LocalDateTime.now(), LocalDateTime.now(), null);

        //when
        userDao.save(user);

        //then
        assertThat(userDao.existEmail("kim@naver.com")).isEqualTo(false);
    }

    @Test
    @DisplayName("패스워드와 이메일을 변경하면 변경된 값으로 db에 저장된다.")
    void updateUser(){
        //given
        User currentUser = new User(1L, "yoon", "abc1234!@#", "jm@naver.com", UserRole.USER, LocalDateTime.now(), LocalDateTime.now(), null);
        userDao.save(currentUser);

        User modifyUser = new User(1L, "modify1234!","kim@naver.com");

        //when
        userDao.update(modifyUser);
        User resultUser = userDao.findById(1L);

        //then
        assertThat(resultUser.getPassword()).isEqualTo(modifyUser.getPassword());
        assertThat(resultUser.getEmail()).isEqualTo(modifyUser.getEmail());
        assertThat(resultUser.getModifyDate().toLocalDate()).isEqualTo(modifyUser.getModifyDate().toLocalDate());
    }
}