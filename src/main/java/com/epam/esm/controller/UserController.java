package com.epam.esm.controller;

import com.epam.esm.model.DTO.user.UserDTO;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    private final UserMapper mapper;

    @GetMapping("/{id}")
    public UserDTO findById(@PathVariable("id") Long id) {
        log.info("Locking for user by id: {}.", id);
        return mapper.toDTO(service.findById(id));
    }

    @GetMapping("/user")
    public List<UserDTO> findByName(@Param("name") String name) {
        log.info("Locking for user by name: {}.", name);
        return service.findByName(name).stream().map(mapper::toDTO).toList();
    }

    @GetMapping()
    public Page<UserDTO> findAll(Pageable pageable) {
        log.info("Getting all certificates");
        Page<User> page = service.findAll(pageable);
        return page.map(mapper::toDTO);
    }
}
