package com.epam.esm.service;

import com.epam.esm.exception.ApiEntityNotFoundException;
import com.epam.esm.exception.NonAuthorizedRequestException;
import com.epam.esm.model.DTO.user.ChangeRoleRequest;
import com.epam.esm.model.DTO.user.CreateUserRequest;
import com.epam.esm.model.entity.Role;
import com.epam.esm.model.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repo;
    @Mock
    private UserMapper mapper;
    private UserService subject;
    private Long id;
    private User user;
    private User userBeforeSave;
    private  Pageable pageable;
    private Page<User> page;
    private String username;

    @BeforeEach
    void setUp() {
        subject = new UserService(repo, mapper);
        id = 1L;
        user = User.builder().id(id).build();
        userBeforeSave = User.builder().build();
        pageable = Pageable.ofSize(3).withPage(0);
        page = new PageImpl<>(List.of(user));
        username = "user";
    }

    @Test
    void findById() {
        when(repo.findById(id)).thenReturn(Optional.of(user));
        User result = subject.findById(id);
        assertThat(result).isEqualTo(user);
    }

    @Test
    void findByIdThrowException() {
        when(repo.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> subject.findById(id))
                .isInstanceOf(ApiEntityNotFoundException.class)
                .hasMessageContaining("Requested user was not found (id=" + id + ")");
    }

    @Test
    void findAll() {
        when(repo.findAll(pageable)).thenReturn(page);

        Page<User> result = subject.findAll(pageable);

        assertThat(result.stream().count()).isEqualTo(1);
        assertThat(result).isEqualTo(new PageImpl<>(List.of(user)));
    }

    @Test
    void create() {
        CreateUserRequest createUserRequest = CreateUserRequest.builder().build();

        when(repo.save(userBeforeSave)).thenReturn(user);
        when(mapper.toUser(createUserRequest)).thenReturn(userBeforeSave);

        User result = subject.create(createUserRequest);
        assertThat(result).isEqualTo(user);

        verify(repo).save(userBeforeSave);
        verify(mapper).toUser(createUserRequest);
    }

    @Test
    void findByName() {
        when(repo.findByName(username)).thenReturn(Optional.of(user));
        User result = subject.findByName(username);
        assertThat(result).isEqualTo(user);
    }

    @Test
    void findByNameThrowException() {
        when(repo.findByName(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subject.findByName(username))
                .isInstanceOf(ApiEntityNotFoundException.class)
                .hasMessageContaining("Requested user was not found (name=" + username + ")");
    }

    @Test
    void changeRoleByUserId() {
        ChangeRoleRequest request = ChangeRoleRequest.builder().role(Role.USER).build();

        when(repo.findById(id)).thenReturn(Optional.of(user));

        when(repo.save(user)).thenReturn(user);
        assertThat(subject.changeRoleByUserId(id, request)).isEqualTo(user);

    }

    @Test
    void changeRoleByUserIdThrowNonAuthorizedRequestException() {
        ChangeRoleRequest request = ChangeRoleRequest.builder().role(Role.ADMIN).build();

        when(repo.findById(id)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> subject.changeRoleByUserId(id, request))
                .isInstanceOf(NonAuthorizedRequestException.class)
                .hasMessageContaining("You do not able to set role ADMIN!");

    }

    @Test
    void changeRoleByUserIdThrowApiEntityNotFoundException() {
        ChangeRoleRequest request = ChangeRoleRequest.builder().role(Role.ADMIN).build();

        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subject.changeRoleByUserId(id, request))
                .isInstanceOf(ApiEntityNotFoundException.class)
                .hasMessageContaining("Requested user was not found (id=" + id + ").");

    }
}