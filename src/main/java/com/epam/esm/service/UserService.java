package com.epam.esm.service;

import com.epam.esm.exception.ApiEntityNotFoundException;
import com.epam.esm.model.DTO.user.CreateUserRequest;
import com.epam.esm.model.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *  A service to work with {@link User}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository repo;
    private final UserMapper mapper;

    /**
     * Finds a {@link User} by its id.
     *
     * @param id user id
     * @return {@link User} user
     * @throws if a user with a given id doesn't exist
     *      {@link ApiEntityNotFoundException} will be thrown
     */
    public User findById(Long id) {
        log.info("Locking for user by id: {}.", id);
        Optional<User> result = repo.findById(id);
        return result.orElseThrow(() -> new ApiEntityNotFoundException(
                "Requested user is not found (id=" + id + ")"));
    }

    /**
     * Finds all users.
     *
     * @return List of {@link User} List of all users from database
     */
    public Page<User> findAll(Pageable pageable) {
        log.info("Getting all users.");
        return repo.findAll(pageable);
    }

    /**
     * Creates a new user.
     *
     * @param request - create user request
     * @return {@link User} created tag
     */
    public User create(CreateUserRequest request) {
        log.info("Creating a new user with name: {}.", request.getName());
        User user = mapper.toUser(request);
        return repo.save(user);
    }

    public User findByName(String name) {
        log.info("Locking for user by name: {}.", name);
        return repo.findByName(name).orElseThrow(() -> new ApiEntityNotFoundException(
                "Requested user is not found (name=" + name + ")"));
    }

}
