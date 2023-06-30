package com.epam.esm.controller;

import com.epam.esm.controller.advice.ApiExceptionHandler;
import com.epam.esm.model.DTO.user.UserDTO;
import com.epam.esm.model.entity.Role;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Mock
    UserService service;
    @Mock
    UserMapper mapper;
    @InjectMocks
    UserController subject;

    private User user;
    private UserDTO userDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subject)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
        user = User.builder().id(1L).role(Role.USER).build();
        userDTO = UserDTO.builder().id(1L).role(Role.USER).build();
        pageable = Pageable.ofSize(3).withPage(0);
    }

    @Test
    void signUp() throws Exception {
        String jsonCreateUser = "{\"name\":\"user\", \"password\":\"password\"}";

        when(service.create(any())).thenReturn(user);
        when(mapper.toDTO(user)).thenReturn(userDTO);

        this.mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateUser))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()));

        verify(service).create(any());
    }

    @Test
    void findAll() throws Exception {
        Page<User> page = new PageImpl<>(List.of(user));

        when(service.findAll(pageable)).thenReturn(page);
        when(mapper.toDTO(user)).thenReturn(userDTO);

        this.mockMvc.perform(get("/users")
                        .param("page", "0")
                        .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(userDTO.getId()));

        verify(service).findAll(pageable);
        verify(mapper).toDTO(user);
    }

    @Test
    void findById() throws Exception {
        when(service.findById(1L)).thenReturn(user);
        when(mapper.toDTO(user)).thenReturn(userDTO);

        this.mockMvc.perform(get("/users/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()));

        verify(service).findById(1L);
        verify(mapper).toDTO(user);
    }

    @Test
    void findByName() throws Exception {
        when(service.findByName(anyString())).thenReturn(user);
        when(mapper.toDTO(user)).thenReturn(userDTO);

        this.mockMvc.perform(get("/users/user", 1)
                        .param("name", "user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()));

        verify(service).findByName(anyString());
        verify(mapper).toDTO(user);
    }

    @Test
    void changeRoleByUserId() throws Exception {
        String jsonChangeRoleRequest = "{\"role\":\"USER\"}";

        when(service.changeRoleByUserId(any(), any())).thenReturn(user);
        when(mapper.toDTO(user)).thenReturn(userDTO);

        this.mockMvc.perform(patch("/users/{id}/role", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonChangeRoleRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()));

        verify(service).changeRoleByUserId(any(), any());
        verify(mapper).toDTO(user);
    }
}