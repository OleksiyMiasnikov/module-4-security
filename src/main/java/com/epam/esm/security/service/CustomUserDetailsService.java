package com.epam.esm.security.service;

import com.epam.esm.model.entity.User;
import com.epam.esm.security.UserPrincipal;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Gets user with a given name from database
 * and creates {@link UserDetails} with user details.
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    /**
     * Gets user with a given name from database
     * and creates {@link UserDetails} with user details
     *
     * @param name users name
     * @return {@link UserDetails} with user details
     */
    @Override
    public UserDetails loadUserByUsername(String name) {
        User user = userService.findByName(name);
        log.info("Creating UserPrincipal of authenticated user.");
        return UserPrincipal.builder()
                .userId(user.getId())
                .name(user.getName())
                .authorities(List.of(new SimpleGrantedAuthority(user.getRole().name())))
                .password(user.getPassword())
                .build();
    }
}
