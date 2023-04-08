package com.flab.recipebook.common.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.flab.recipebook.common.service.JwtService;
import com.flab.recipebook.user.domain.User;
import com.flab.recipebook.user.domain.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Jwt 인증 필터
 * <p>
 * RefreshToken 이 없고, AcessToken 이 유효한 경우 -> 성공, RefreshToken 재발급
 * RefreshToken 이 없고, AcessToken 도 없는 경우 -> 인증 실패
 * RefreshToken이 있는 경우 -> DB의 RefreshToken과 비교 일치 -> AccessToken 재발급, RefreshToken 재발급
 */
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    private Logger log = LoggerFactory.getLogger(getClass());
    private static final String PASS_URL = "/login";
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    private final JwtService jwtService;
    private final UserDao userDao;

    public JwtAuthenticationProcessingFilter(JwtService jwtService, UserDao userDao) {
        this.jwtService = jwtService;
        this.userDao = userDao;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //로그인 요청은 토큰 발급 x
        if (request.getRequestURI().equals(PASS_URL)) {
            log.info("로그인 요청 : 토큰 발급 x");
            log.info("로그인 요청(Null예상) : AccessToken = {}", request.getHeader("Authorization"));
            log.info("로그인 요청(Null예상) : RefreshToken = {}", request.getHeader("Authorization-refresh"));
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtService.getRefreshToken(request)
                .filter(token -> jwtService.isValid(token))
                .orElse(null);

        //RefreshToken이 존재하는 경우 (토큰 재발급)
        if (refreshToken != null) {
            log.info("RefreshToken 존재 : 기존 AccessToken = {}", request.getHeader("Authorization"));
            log.info("RefreshToken 존재 : 기존 RefreshToken = {}", request.getHeader("Authorization-refresh"));

            //Db RefreshToken 확인
            userDao.findByRefreshToken(refreshToken)
                    .ifPresentOrElse(user -> {
                        //새로운 refreshToken 생성
                        String newRefreshToken = jwtService.createRefreshToken();
                        //DB refreshToken 업데이트
                        user.updateRefreshToken(newRefreshToken);
                        userDao.updateRefreshToken(user);

                        //AccessToken 생성
                        String newAccessToken = jwtService.createAccessToken(user.getUserId());
                        log.info("새로운 토큰 발급 : AccessToken = {}", newAccessToken);
                        log.info("새로운 토큰 발급 : RefreshToken = {}", newRefreshToken);

                        //header에 AccessToken, 새로 생성된 RefreshToken을 담아 응답
                        jwtService.setHeaderAccessTokenRefreshToken(
                                response, newAccessToken, newRefreshToken);
                    }, () -> {
                        throw new JWTVerificationException("RefreshToken을 찾을 수 없습니다.");
                    });
            return;
        }

        //RefreshToken 이 없고, AcessToken 이 유효한 경우
        if (refreshToken == null) {
            log.info("AccessToken 요청 : {}", request.getHeader("Authorization"));
            //요청에서 AccessToken 검증
            jwtService.getAccessToken(request)
                    .filter(accessToken -> jwtService.isValid(accessToken))
                    .ifPresent(accessToken -> jwtService.getUserId(accessToken)
                            .ifPresent(userId -> userDao.findByUserId(userId)
                                    .ifPresent(user -> saveAuthentication(user))));

            filterChain.doFilter(request, response);
        }
    }

    public void saveAuthentication(User user) {
        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserId())
                .password(user.getPassword())
                .roles(user.getUserRole().name())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
