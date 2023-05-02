package com.epam.esm.model.login;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequest {
    private String name;
    private String password;
}
