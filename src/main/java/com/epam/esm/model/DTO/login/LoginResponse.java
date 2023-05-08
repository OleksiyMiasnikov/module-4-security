package com.epam.esm.model.DTO.login;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private final String accessToken;
    private final String refreshToken;
}
