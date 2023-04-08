package com.flab.recipebook.common.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.recipebook.common.dto.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    public JwtExceptionFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JWTVerificationException e) {
            setErrorMessage(response, e);
        }
    }

    private void setErrorMessage(HttpServletResponse response, JWTVerificationException e) throws IOException {
        //응답 객체 생성
        ResponseResult responseResult = getResponseResult(e);

        //Json 변환
        String jsonResult = convertObjectToJson(responseResult);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResult);
    }

    private String convertObjectToJson(ResponseResult responseResult) throws JsonProcessingException {
        return objectMapper.writeValueAsString(responseResult);
    }

    private ResponseResult getResponseResult(JWTVerificationException e) {
        return new ResponseResult(Arrays.asList(e.getMessage()));
    }
}
