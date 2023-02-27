package com.flab.recipebook.user.repository;

import com.flab.recipebook.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryImplTest {

    UserRepository userRepository = new UserRepositoryImpl();
    @Test
    @DisplayName("유저 생성")
    void Create_User(){
        //given
        User user = new User.Builder()
                .username("user1")
                .nickname("nick")
                .password("1234")
                .email("test@naver.com")
                .build();
        //when
        User result = userRepository.save(user);

        //then
        assertThat(result).isEqualTo(null);

    }

}