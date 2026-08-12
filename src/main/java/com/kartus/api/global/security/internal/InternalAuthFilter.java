package com.kartus.api.global.security.internal;

import com.kartus.api.global.dto.GlobalApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class InternalAuthFilter extends OncePerRequestFilter {
    public static final String INTERNAL_TOKEN_HEADER = "X-Internal-Token";

    private final InternalTokenValidator internalTokenValidator;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(INTERNAL_TOKEN_HEADER);

        if (!internalTokenValidator.matches(token)) {
            log.warn("[InternalAuth] 인증 실패. method={}, uri={}, headerPresent={}",
                    request.getMethod(), request.getRequestURI(), token != null);

            sendNotFound(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendNotFound(HttpServletResponse response) throws IOException {
        if (response.isCommitted()) {
            return;
        }

        response.resetBuffer();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store");

        objectMapper.writeValue(response.getOutputStream(), GlobalApiResponse.fail(HttpStatus.NOT_FOUND, "not found"));
    }
}
