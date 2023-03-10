package com.flab.recipebook.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.recipebook.user.domain.User;
import com.flab.recipebook.user.domain.UserRole;
import com.flab.recipebook.user.dto.SaveUserDto;
import com.flab.recipebook.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    @Test
    @DisplayName("유저 생성 성공시 201 상태코드를 반환한다.")
    void save() throws Exception {
        //given
        SaveUserDto saveUserDto = new SaveUserDto("yoon", null, "jm@naver.com");
        String json = new ObjectMapper().writeValueAsString(saveUserDto);

        //when
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                ;
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("nullUserProvider")
    @DisplayName("유저생성 요청값이 유효하지 않을 때 400 상태코드를 반환한다.")
    void save_null_error(String description, SaveUserDto saveUserDto) throws Exception {
        //given
        String json = new ObjectMapper().writeValueAsString(saveUserDto);

        //when
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
        ;
    }
    private static Stream<Arguments> nullUserProvider(){
        return Stream.of(
                Arguments.of("아이디가 null인 경우", new SaveUserDto(null,"ab12345!","jm@naver.com")),
                Arguments.of("비밀번호가 null인 경우", new SaveUserDto("yoon1",null,"jm@naver.com")),
                Arguments.of("이메일이 null인 경우", new SaveUserDto("yoon2","ab12345!",null)),
                Arguments.of("비밀번호가 패턴에 맞지 않는 경우", new SaveUserDto("yoon", "123", "jm@naver.com")),
                Arguments.of("이메일이 패턴에 맞지 않는 경우", new SaveUserDto("yoon2","ab12345!","emailError"))
        );
    }

    @Test
    @DisplayName("존재하는 유저를 조회 하면 200 상태코드와 User 정보를 반환한다.")
    void findById() throws Exception {
        //given
        User user = new User(1L,"yoon", "ab12345!","jm@naver.com", UserRole.USER, LocalDateTime.now(),LocalDateTime.now());
        Long userNo = 1L;
        given(userService.findById(userNo)).willReturn(user);

        //when
        mvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //then
        then(userService).should().findById(userNo);
    }
}
