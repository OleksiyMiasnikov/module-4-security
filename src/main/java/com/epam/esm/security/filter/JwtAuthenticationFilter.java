package com.epam.esm.security.filter;

import com.epam.esm.security.jwt.JwtDecoder;
import com.epam.esm.security.jwt.JwtPrincipalConverter;
import com.epam.esm.security.UserPrincipalAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * Gets token from request, decoded it, convert to UserPrincipal,
 * create {@link UserPrincipalAuthenticationToken}
 * and put it into SecurityContextHolder
 *
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtDecoder decoder;
    private final JwtPrincipalConverter converter;

    /**
     * Gets token from request, decoded it, convert to UserPrincipal,
     * create {@link UserPrincipalAuthenticationToken}
     * and put it into SecurityContextHolder
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param filterChain FilterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        extractTokenFromRequest(request)
                .map(decoder::decode)
                .map(converter::convert)
                .map(UserPrincipalAuthenticationToken::new)
                .ifPresent(authentication -> SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication));

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts token from HttpServletRequest
     *
     * @param request HttpServletRequest to extract token
     * @return Optimal<String> with token if it presents in request
     * or Optional.empty() if it absents
     */
    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        var token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return Optional.of(token.substring(7));
        }
        return Optional.empty();
    }
}