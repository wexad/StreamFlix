package com.wexad.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationFailureHandler authenticationFailureHandler;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomAuthenticationFailureHandler authenticationFailureHandler, CustomUserDetailsService customUserDetailsService) {
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(req -> req
                .requestMatchers("/auth/**", "/home/**", "/static/**")
                .permitAll()
                .anyRequest()
                .fullyAuthenticated()
        );
        http.formLogin(login -> login
                .loginPage("/auth/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/home", true)
                .failureHandler(authenticationFailureHandler)
        );
        http.logout(logout -> logout
                .logoutUrl("/auth/logout")
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "GET"))
        );
        http.rememberMe(rememberMe -> rememberMe
//                .rememberMeParameter("rememberMe")
                        .alwaysRemember(true)
                .rememberMeCookieName("rem-me")
                .tokenValiditySeconds(2 * 60 * 60)
                .key("secret-key")
                .userDetailsService(customUserDetailsService)
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
