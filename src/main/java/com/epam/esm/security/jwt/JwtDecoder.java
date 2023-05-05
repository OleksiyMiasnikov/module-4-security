package com.epam.esm.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
     *
     * @param token json web token
     * @return decoded json web token
     */
    public DecodedJWT decode(String token) {
        log.info("Decoding and verifying JWT.");
        return JWT.require(Algorithm.HMAC256(properties.getSecretKey()))
                .build()
                .verify(token);
    }
}
