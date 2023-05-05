package com.flab.recipebook.common.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.flab.recipebook.user.domain.User;
import com.flab.recipebook.user.domain.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    private Long refreshTokenExpirationPeriod;

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
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * 클라이언트 요청 시 헤더에서 Token 수집
     */
    public String getHeaderToken(String headerToken) {
        return removeTokenPrefix(headerToken)
                .filter(this::isValid)
                .orElse(null);
    }

    /**
     * 토큰의 prefix 제거
     */
    public Optional<String> removeTokenPrefix(String headerToken) {
        return Optional.ofNullable(headerToken)
                .filter(token -> token.startsWith(BEARER))
                .map(token -> token.replace(BEARER, "").trim());     // type Prefix 제거
    }

    /**
     * 토근에서 UserId 수집
     */
    public String getUserId(String accessToken) {
        try {
            return JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)    //토근 검증, 오류 시 -> 예외발생
                    .getClaim(USER_CLAIM)
                    .asString();
        } catch (Exception e) {
            throw new JWTVerificationException(e.getMessage());
        }
    }

    /**
     * RefreshToken 재발급
     */
    public Map<String, String> ReCreateTokens(String refreshToken) {

        User findUser = checkRefreshToken(refreshToken);
        Map<String, String> tokens = new HashMap<>();

        String newRefreshToken = createRefreshToken();
        String newAccessToken = createAccessToken(findUser.getUserId());

        tokens.put(ACCESS_TOKEN_SUBJECT, newAccessToken);
        tokens.put(REFRESH_TOKEN_SUBJECT, newRefreshToken);

        //새로운 RefreshToken 반영
        updateRefreshToken(findUser, newRefreshToken);

        return tokens;
    }

    /**
     * DB RefreshToken 확인
     */
    public User checkRefreshToken(String refreshToken) {
        return userDao.findByRefreshToken(refreshToken).orElseThrow(() -> new JWTVerificationException("RefreshToken 을 찾을 수 없습니다."));
    }

    /**
     * RefreshToken DB에 저장을 위한 User 객체 업데이트
     */
    public void updateRefreshToken(User user, String refreshToken) {
        user.updateRefreshToken(refreshToken);
        userDao.updateRefreshToken(user);
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
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException(e.getMessage());
        }
    }

    /**
     * 토큰에서 userId 추출 
     */
    public String extractUserId(String headerToken) {
        String token = removeTokenPrefix(headerToken).orElse(null);
        return getUserId(token);
    }

    /**
     * userId Db 조회
     */
    public Optional<User> checkUserId(String userId) {
        return userDao.findByUserId(userId);
    }
}
