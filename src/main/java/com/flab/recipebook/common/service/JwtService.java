package com.flab.recipebook.common.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.flab.recipebook.user.domain.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {
    Logger log = LoggerFactory.getLogger(getClass());

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

    private final UserDao userDao;

    public JwtService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * AccessToken 생성
     */
     public String createAccessToken(String userId) {
         Date now = new Date();
         return JWT.create()
                 .withSubject(ACCESS_TOKEN_SUBJECT)
                 .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
                 .withClaim(USER_CLAIM, userId)
                 .sign(Algorithm.HMAC512(secretKey));
     }

    /**
     * RefreshToken 생성
     */
    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPerios))
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * AccessToken, RefreshToken 발급
     * 로그인 시 헤더에 AccessToken, RefreshToken 추가
     */
    public void setHeaderAccessTokenRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, accessToken);
        response.setHeader(refreshHeader, refreshToken);

        log.info("AccessToken = {}", accessToken);
        log.info("RefreshToken = {}", refreshToken);
    }

    /**
     * 클라이언트 요청 시 헤더에서 AccessToken 수집
     */
    public Optional<String> getAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));   // type Prefix 제거
    }

    /**
     * 클라이언트 요청 시 헤더에서 RefreshToken 수집
     */
    public Optional<String> getRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));   // type Prefix 제거
    }

    /**
     * 토근에서 UserId 수집
     */
    public Optional<String> getUserId(String accessToken){
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)    //토근 검증, 오류 시 -> 예외발생
                    .getClaim(USER_CLAIM)
                    .asString());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * RefreshToken DB에 저장을 위한 User 객체 업데이트
     */
    public void updateRefreshToken(String userId, String refreshToken) {
        userDao.findByUserId(userId)
                .ifPresentOrElse(
                        user -> user.updateRefreshToken(refreshToken),
                        () -> new Exception("일치하는 회원이 없습니다. ")
                );
    }

    /**
     * 토큰 검증
     */
    public boolean isValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
