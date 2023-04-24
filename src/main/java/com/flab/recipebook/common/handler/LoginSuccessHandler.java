package com.flab.recipebook.common.handler;

import com.flab.recipebook.common.service.JwtService;
import com.flab.recipebook.user.domain.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Value("${jwt.access.expiration}")
    private String accessTokenExpiration;

    private final JwtService jwtService;
    private final UserDao userDao;

    public LoginSuccessHandler(JwtService jwtService, UserDao userDao) {
        this.jwtService = jwtService;
        this.userDao = userDao;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();
        String accessToken = jwtService.createAccessToken(userId);
        String refreshToken = jwtService.createRefreshToken();

        jwtService.setHeaderAccessTokenRefreshToken(response, accessToken, refreshToken);

        userDao.findByUserId(userId)
                .ifPresent(user -> {
                    jwtService.updateRefreshToken(user, refreshToken);
                });

        log.info("로그인 성공 userId = {}", userId);
        log.info("로그인 성공 AccessToken = {}", accessToken);
        log.info("만료 기간 accessTokenExpiration = {}", accessTokenExpiration);
        log.info("로그인 성공 refreshToken = {}", refreshToken);
    }
}
