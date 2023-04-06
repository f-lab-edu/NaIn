package com.flab.recipebook.common.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.recipebook.common.filter.JsonUsernamePasswordAuthenticationFilter;
import com.flab.recipebook.common.filter.JwtAuthenticationProcessingFilter;
import com.flab.recipebook.common.handler.LoginFailHandler;
import com.flab.recipebook.common.handler.LoginSuccessHandler;
import com.flab.recipebook.common.service.JwtService;
import com.flab.recipebook.common.service.LoginService;
import com.flab.recipebook.user.domain.dao.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * SpringSecurity 활성화
 */
@EnableWebSecurity
public class SecurityConfiguration {

    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final UserDao userDao;
    private final LoginService loginService;

    public SecurityConfiguration(ObjectMapper objectMapper, JwtService jwtService, UserDao userDao, LoginService loginService) {
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
        this.userDao = userDao;
        this.loginService = loginService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin().disable()
                .httpBasic().disable()       //Http basic Auth 기반으로 로그인 인증창이 뜸.  기본 인증 로그인을 이용하지 않으면 disable
                .csrf().disable()
                .headers().frameOptions().deny() //X-Frame-Options - clickjacking 보호

                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                //권한 설정
                .antMatchers("/", "/login", "/signup").permitAll()
                .anyRequest().authenticated();
        //순서 설정 LogoutFilter(스프링) -> jwtAuthenticationProcessFilter -> JsonUsernamePasswordAuthenticationFilter
        http.addFilterAfter(jsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), JsonUsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * AuthenticationManager 등록
     * AuthenticationProvider 지정 -> UserDetailsService 등록
     */
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    /**
     * 로그인 성공 핸들러
     */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, userDao);
    }

    /**
     * 로그인 실패 핸들러
     */
    public LoginFailHandler loginFailHandler() {
        return new LoginFailHandler();
    }

    /**
     * UsernamePasswordAuthenticationFileter 빈 등록
     * AuthenticationnManager, loginSuccessHandler, loginFailHandler 등록
     */
    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter(){
        JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter
                = new JsonUsernamePasswordAuthenticationFilter(objectMapper);
        jsonUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        jsonUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        jsonUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(loginFailHandler());
        return jsonUsernamePasswordAuthenticationFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter = new JwtAuthenticationProcessingFilter(jwtService, userDao);
        return jwtAuthenticationProcessingFilter;
    }
}
