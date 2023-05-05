package com.flab.recipebook.common.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.flab.recipebook.user.domain.dao.UserDao;
import com.flab.recipebook.user.dto.SaveUserDto;
import com.flab.recipebook.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@TestPropertySource("classpath:application-test.properties")
class JwtServiceTest {

    @Autowired
    JwtService jwtService;
    @Autowired
    UserService userService;
    @Autowired
    UserDao userDao;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPerios;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String USER_CLAIM = "userId";
    private static final String BEARER = "Bearer";
    private final String USER_NAME = "userId";

    @BeforeEach
    public void init() {
        SaveUserDto savedUser = new SaveUserDto("test", "123abc!", "jm@naver.com");
        userService.save(savedUser);
    }

    @Test
    @DisplayName("AccessToken 발급 성공")
    void createAccessToken() {
        String accessToken = jwtService.createAccessToken(USER_NAME);
        DecodedJWT verify = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(accessToken);
        String subject = verify.getSubject();
        String findUsername = verify.getClaim(USER_CLAIM).asString();

        assertThat(findUsername).isEqualTo(USER_NAME);
        assertThat(subject).isEqualTo(ACCESS_TOKEN_SUBJECT);
    }

    @Test
    @DisplayName("토큰 유효성 검사")
    void isValid() {
        String accessToken = jwtService.createAccessToken(USER_NAME);
        String refreshToken = jwtService.createRefreshToken();

        assertThat(jwtService.isValid(accessToken)).isTrue();
        assertThat(jwtService.isValid(refreshToken)).isTrue();
        assertThatThrownBy(() -> jwtService.isValid(accessToken + "a")).isInstanceOf(JWTVerificationException.class);
        assertThatThrownBy(() -> jwtService.isValid(refreshToken + "a")).isInstanceOf(JWTVerificationException.class);
    }

    @Test
    @DisplayName("헤더 설정")
    void setHeaderAccessTokenRefreshToken() {
        MockHttpServletResponse response = new MockHttpServletResponse();

        String accessToken = jwtService.createAccessToken(USER_NAME);
        String refreshToken = jwtService.createRefreshToken();

        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, accessToken);
        response.setHeader(refreshHeader, refreshToken);

        String headerAccessToken = response.getHeader(accessHeader);
        String headerRefreshHeader = response.getHeader(refreshHeader);

        assertThat(headerAccessToken).isEqualTo(accessToken);
        assertThat(headerRefreshHeader).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("요청 값에서 AccessToken 추출")
    void getAccessToken() throws Exception{
        String accessToken = jwtService.createAccessToken(USER_NAME);
        String refreshToken = jwtService.createRefreshToken();

        HttpServletRequest request = setRequest(accessToken, refreshToken);
        String headerAccessToken = request.getHeader(accessHeader);

        String getAccessToken = jwtService.removeTokenPrefix(headerAccessToken).orElseThrow(() -> new Exception("오류"));
        assertThat(getAccessToken).isEqualTo(accessToken);
        assertThat(JWT.require(Algorithm.HMAC512(secretKey)).build().verify(getAccessToken).getClaim(USER_CLAIM).asString()).isEqualTo(USER_NAME);
    }

    @Test
    @DisplayName("요청 값에서 RefreshToken 추출")
    void getRefreshToken() throws Exception{
        String accessToken = jwtService.createAccessToken(USER_NAME);
        String refreshToken = jwtService.createRefreshToken();

        HttpServletRequest request = setRequest(accessToken, refreshToken);
        String headerRefreshToken = request.getHeader(refreshHeader);

        String getRefreshToken = jwtService.removeTokenPrefix(headerRefreshToken).orElseThrow(() -> new Exception("오류"));

        assertThat(getRefreshToken).isEqualTo(getRefreshToken);
        assertThat(JWT.require(Algorithm.HMAC512(secretKey)).build().verify(getRefreshToken).getSubject()).isEqualTo(REFRESH_TOKEN_SUBJECT);
    }

    @Test
    public void getUsername() throws Exception{
        String accessToken = jwtService.createAccessToken(USER_NAME);
        String refreshToken = jwtService.createRefreshToken();

        HttpServletRequest request = setRequest(accessToken, refreshToken);
        String headerAccessToken = request.getHeader(accessHeader);

        String requestAccessToken = jwtService.removeTokenPrefix(headerAccessToken).orElseThrow(() -> new Exception("토큰 없음"));
        String getUsername = jwtService.getUserId(requestAccessToken);

        assertThat(getUsername).isEqualTo(USER_NAME);
    }


    private HttpServletRequest setRequest(String accessToken, String refreshToken) {
        MockHttpServletResponse response = new MockHttpServletResponse();

        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, accessToken);
        response.setHeader(refreshHeader, refreshToken);

        String headerAccessToken = response.getHeader(accessHeader);
        String headerRefreshToken = response.getHeader(refreshHeader);

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.addHeader(accessHeader, BEARER + headerAccessToken);
        request.addHeader(refreshHeader, BEARER + headerRefreshToken);

        return request;
    }
}