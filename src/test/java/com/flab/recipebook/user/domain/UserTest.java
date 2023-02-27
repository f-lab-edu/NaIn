package com.flab.recipebook.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Role 생성 테스트")
    void builderTest() {
        User user = new User.Builder()
                .username("username")
                .nickname("nickname")
                .password("1234")
                .email("test@Test.com")
                .role(UserRole.ADMIN)
                .build();
        System.out.println(user);

        User user2 = new User.Builder()
                .username("username")
                .nickname("nickname")
                .password("1234")
                .email("test@Test.com")
                .build();
        System.out.println(user2);

    }


}