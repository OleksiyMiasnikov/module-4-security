package com.epam.esm.controller;

import com.epam.esm.model.DTO.user.CreateUserRequest;
import com.epam.esm.model.DTO.user.UserDTO;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    private final UserMapper mapper;

    @GetMapping("/signup")
    public UserDTO signUp(
        @Valid
        @RequestBody CreateUserRequest request) {
            log.info("Creating tag with name: {}.", request.getName());
            return mapper.toDTO(service.create(request));
    }

    @GetMapping("/users/{id}")
    public UserDTO findById(@PathVariable("id") Long id) {
        log.info("Locking for user by id: {}.", id);
        return mapper.toDTO(service.findById(id));
    }

    @GetMapping("/users/user")
    public UserDTO findByName(@Param("name") String name) {
        log.info("Locking for user by name: {}.", name);
        return mapper.toDTO(service.findByName(name));
    }

    @GetMapping("/users")
    public Page<UserDTO> findAll(Pageable pageable) {
        log.info("Getting all certificates");
        Page<User> page = service.findAll(pageable);
        return page.map(mapper::toDTO);
    }
}
