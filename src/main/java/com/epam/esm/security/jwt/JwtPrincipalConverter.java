package com.epam.esm.security.jwt;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.epam.esm.security.UserPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts json web token to object of {@link UserPrincipal}.
 *
 */
@Component
public class JwtPrincipalConverter {

    /**
     * Extracts fields from JWT and creates entity of {@link UserPrincipal}.
     *
     * @param jwt json web token
     * @return created {@link UserPrincipal}
     */
    public UserPrincipal convert(DecodedJWT jwt) {
        return UserPrincipal.builder()
                .userId(Long.parseLong(jwt.getSubject()))
                .name(jwt.getClaim("name").asString())
                .authorities(extractAuthoritiesFromClaim(jwt))
                .build();
    }

    /**
     * Extracts list of authorities from token claim with name 'authorities'
     * @param jwt json web token
     * @return list of {@link SimpleGrantedAuthority}
     */
    private List<SimpleGrantedAuthority> extractAuthoritiesFromClaim(DecodedJWT jwt) {
        Claim claim = jwt.getClaim("authorities");
        if (claim.isNull() || claim.isMissing()) return List.of();
        return claim.asList(SimpleGrantedAuthority.class);
    }

}
