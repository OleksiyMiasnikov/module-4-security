package com.epam.esm.service;

import com.epam.esm.model.entity.User;
import com.epam.esm.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
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
@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    /**
     * Gets user with a given name from database
     * and creates {@link UserDetails} with user details
     *
     * @param name user name
     * @return {@link UserDetails} with user details
     * @throws UsernameNotFoundException if user is absent in database
     */
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userService.findByName(name);
        return UserPrincipal.builder()
                .userId(user.getId())
                .name(user.getName())
                .authorities(List.of(new SimpleGrantedAuthority(user.getRole().name())))
                .password(user.getPassword())
                .build();
    }
}
