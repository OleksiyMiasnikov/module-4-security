package com.epam.esm.service.mapper;

import com.epam.esm.model.DTO.user.UserDTO;
import com.epam.esm.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final ModelMapper mapper = new ModelMapper();
    public UserDTO toDTO (User user){
        return mapper.map(user, UserDTO.class);
    }
}
