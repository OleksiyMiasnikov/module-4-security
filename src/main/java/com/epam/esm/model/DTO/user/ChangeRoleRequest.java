package com.epam.esm.model.DTO.user;

import com.epam.esm.model.entity.Role;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeRoleRequest {
    @NotEmpty(message = "Field 'role' can not be empty!")
    @Enumerated
    private Role role;
}
