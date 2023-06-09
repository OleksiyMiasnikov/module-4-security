package com.epam.esm.security.filter;

import com.epam.esm.exception.ApiErrorResponse;
import com.epam.esm.exception.NonAuthorizedRequestException;
import com.epam.esm.security.UserPrincipalAuthenticationToken;
import com.epam.esm.security.jwt.JwtDecoder;
import com.epam.esm.security.jwt.JwtPrincipalConverter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * Class JwtAuthenticationFilter extends {@link OncePerRequestFilter}
 * Gets token from request, decoded it, convert to UserPrincipal,
 * create {@link UserPrincipalAuthenticationToken}
 * and put it into SecurityContextHolder
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:4200/"})
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

        log.info("--{}", request.getHeader("authorization"));
        if (!request.getRequestURI().equals("/login")
                && !request.getRequestURI().equals("/signup")
                && !request.getRequestURI().equals("/about")) {
            try {
                Optional<Authentication> authentication =
                        extractTokenFromRequest(request)
                            .map(decoder::decode)
                            .map(converter::convert)
                            .map(UserPrincipalAuthenticationToken::new);

                if (authentication.isEmpty()) {
                    log.warn("Authentication fail for {}", request.getRequestURI());
                    throw new NonAuthorizedRequestException("Authentication needed.");
                } else {
                    log.info("Authentication success.");
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication.get());
                }
            } catch (NonAuthorizedRequestException exception) {

                ApiErrorResponse.builder()
                        .message(exception.getMessage())
                        .errorCode(exception.getErrorCode())
                        .status(exception.getStatus())
                        .build()
                        .send(response);
            }
        }
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
        log.info("Getting JWT from request.");
        String token = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return Optional.of(token.substring("Bearer ".length()));
        }
        log.warn("There is no JWT in header.");
        return Optional.empty();
    }
}
