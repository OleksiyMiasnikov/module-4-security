package com.epam.esm.model.DTO.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @NotEmpty(message = "Field 'name' can not be empty!")
    private String name;
    @NotEmpty(message = "Field 'name' can not be empty!")
    private String password;
}
