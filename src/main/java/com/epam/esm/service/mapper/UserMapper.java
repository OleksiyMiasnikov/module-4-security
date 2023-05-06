package com.epam.esm.service.mapper;

import com.epam.esm.model.DTO.user.CreateUserRequest;
import com.epam.esm.model.DTO.user.UserDTO;
import com.epam.esm.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper apiMapper;

    public UserDTO toDTO (User user){
        return apiMapper.map(user, UserDTO.class);
    }

    public User toUser (CreateUserRequest request) {
        return apiMapper.map(request, User.class);
    }
}
