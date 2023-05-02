package com.epam.esm.security;

import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = userService.findByName(username);
        if (users.size() == 0) {
            throw new UserNotFoundException("Unknown user");
        }
        User user = users.get(0);
        return UserPrincipal.builder()
                .userId(user.getId())
                .name(user.getName())
                .authorities(List.of(new SimpleGrantedAuthority(user.getRole().name())))
                .password(user.getPassword())
                .build();
    }
}
