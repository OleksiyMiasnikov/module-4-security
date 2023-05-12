package com.epam.esm.model.DTO.login;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String name;
    private String password;
}
