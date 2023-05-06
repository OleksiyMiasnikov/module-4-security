package com.epam.esm.config;

import com.epam.esm.model.entity.Role;
import com.epam.esm.security.filter.CustomAccessDeniedHandler;
import com.epam.esm.security.service.CustomUserDetailsService;
import com.epam.esm.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter filter;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAccessDeniedHandler handler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("SecurityFilterChain configuration started.");

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling().accessDeniedHandler(handler);

        http
            .cors().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(STATELESS).and()
            .formLogin().disable()
            .securityMatcher("/**")
            .authorizeHttpRequests(registry -> registry
                            .requestMatchers("/login").permitAll()
                            .requestMatchers("/signup").permitAll()
                            .requestMatchers("/secured").permitAll()
                            .requestMatchers(GET, "/**")
                            .hasAnyAuthority(Role.GUEST.name(), Role.USER.name(), Role.ADMIN.name())
                            .requestMatchers(POST, "/orders")
                            .hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                            .requestMatchers("/**").hasAuthority(Role.ADMIN.name())
                            .anyRequest()
                            .authenticated()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        log.info("AuthenticationManager configuration started.");
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())
                .and().build();
    }

}
