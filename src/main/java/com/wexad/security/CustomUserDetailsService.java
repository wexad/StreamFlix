package com.wexad.security;

import com.wexad.domains.user.AuthUser;
import com.wexad.service.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService service;

    public CustomUserDetailsService(UserService service) {
        this.service = service;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println(email);
        AuthUser authUser = service.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by email '%s'".formatted(email)));
        return new CustomUserDetails(authUser);
    }
}
