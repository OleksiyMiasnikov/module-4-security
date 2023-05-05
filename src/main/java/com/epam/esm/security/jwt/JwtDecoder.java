package com.epam.esm.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.epam.esm.exception.NonAuthorizedRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Decodes and verifies token.
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtDecoder {

    private final JwtProperties properties;

    /**
     * Decodes and verifies a given token.
     * If token has expired date, decoding or validation errors
     * {@link NonAuthorizedRequestException} will be thrown
     *
     * @param token json web token
     * @return decoded json web token
     */
    public DecodedJWT decode(String token) {
        log.info("Decoding and verifying JWT.");
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            if( decodedJWT.getExpiresAt().before(new Date())) {
                log.error("The Token has expired on {}",
                        decodedJWT.getExpiresAt().toString());
                throw new NonAuthorizedRequestException("The Token has expired on " +
                        decodedJWT.getExpiresAt().toString());
            }

            return JWT.require(Algorithm.HMAC256(properties.getSecretKey()))
                    .build()
                    .verify(token);
        } catch (JWTDecodeException exception) {
            log.error("Decoding error: {}", exception.getMessage());
            throw new NonAuthorizedRequestException(exception.getMessage());
        }
    }
}
