package com.flab.recipebook.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

//스프링 시큐리티의 폼 기반 필터인 UsernamePasswordAuthenticationFilter를 Json으로 받기 위해 수정.
public class JsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String SPRING_SECURITY_JSON_USERNAME_KEY = "userId";
    public static final String SPRING_SECURITY_JSON_PASSWORD_KEY = "password";
    private static final String CONTENT_TYPE = "application/json";
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login", "POST");
    private String usernameParameter = SPRING_SECURITY_JSON_USERNAME_KEY;
    private String passwordParameter = SPRING_SECURITY_JSON_PASSWORD_KEY;

    private final ObjectMapper objectMapper;

    public JsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);    //post 요청 처리
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {

        validateJson(request.getContentType());

        Map<String, String> userIdPassowordMap = convertJsonToMap(request);

        String username = userIdPassowordMap.get(usernameParameter);
        String password = userIdPassowordMap.get(passwordParameter);

        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private static void validateJson(String requestContentType) {
        if (requestContentType == null || !requestContentType.equals(CONTENT_TYPE)) {
            throw new AuthenticationServiceException("Authentication Content-Type not supported: " + requestContentType);
        }
    }

    /**
     * request messageBody를 JSON으로 변환
     */
    private Map<String, String> convertJsonToMap(HttpServletRequest request) throws IOException {
        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        //Json 변환
        Map<String, String> userIdPasswordMap = objectMapper.readValue(messageBody, Map.class);

        return userIdPasswordMap;
    }
}
