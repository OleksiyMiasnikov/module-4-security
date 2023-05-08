package com.epam.esm.model.DTO.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {
    @NotEmpty(message = "Field 'username' can not be empty!")
    private String username;
    @NotEmpty(message = "Field 'password' can not be empty!")
    private String password;
}
