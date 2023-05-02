package com.epam.esm.model.DTO.user;

import com.epam.esm.model.entity.Role;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private int id;
    private String name;
    private Role role;
}
