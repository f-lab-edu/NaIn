package com.flab.recipebook.common.filter;

import com.flab.recipebook.common.service.JwtService;
import com.flab.recipebook.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Map;

/**
 * Jwt 인증 필터
 * RefreshToken 이 없고, AcessToken 이 유효한 경우 -> 성공, RefreshToken 재발급
 * RefreshToken 이 없고, AcessToken 도 없는 경우 -> 인증 실패
 * RefreshToken 이 있는 경우 -> DB의 RefreshToken과 비교 일치 -> AccessToken 재발급, RefreshToken 재발급
 */
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    private Logger log = LoggerFactory.getLogger(getClass());

    private static final String PASS_URL = "/login";

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private final JwtService jwtService;

    public JwtAuthenticationProcessingFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //로그인 요청은 토큰 발급 x
        if (request.getRequestURI().equals(PASS_URL)) {
            log.info("로그인 요청 : 토큰 발급 x");
            log.info("로그인 요청(Null예상) : AccessToken = {}", request.getHeader(accessHeader));
            log.info("로그인 요청(Null예상) : RefreshToken = {}", request.getHeader(refreshHeader));
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtService.getHeaderToken(request.getHeader(refreshHeader));

        //RefreshToken이 존재하는 경우 (토큰 재발급)
        if (refreshToken != null) {
            log.info("RefreshToken 존재 : 요청 AccessToken = {}", request.getHeader(accessHeader));
            log.info("RefreshToken 존재 : 요청 RefreshToken = {}", request.getHeader(refreshHeader));

            //Token 재발급
            Map<String, String> tokens = jwtService.ReCreateTokens(refreshToken);

            //응답 반영
            String ReCreatedAccessToken = tokens.get(ACCESS_TOKEN_SUBJECT);
            String ReCreatedRefreshToken = tokens.get(REFRESH_TOKEN_SUBJECT);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader(accessHeader, ReCreatedAccessToken);
            response.setHeader(refreshHeader, ReCreatedRefreshToken);

            log.info("발급된 AccessToken = {}", ReCreatedAccessToken);
            log.info("발급된 RefreshToken = {}", ReCreatedRefreshToken);
            return;
        }

        //RefreshToken 이 없고, AcessToken 이 유효한 경우
        if (refreshToken == null) {
            String accessToken = request.getHeader(accessHeader);
            log.info("AccessToken 요청 : {}", accessToken);

            if (accessToken != null) {
                //요청에서 AccessToken 검증 후 userId 추출
                String userId = jwtService.extractUserId(accessToken);
                jwtService.checkUserId(userId).ifPresent(user -> saveAuthentication(user));
            }

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
