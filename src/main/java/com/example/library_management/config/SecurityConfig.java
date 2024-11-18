package com.example.library_management.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired private CustomUserDetails customUserDetails;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            (requests) ->
                requests
                    .requestMatchers("/", "/registration", "/assets/**", "/images/**", "/login", "/book/**")
                    .permitAll()
  // ROLE_MEMBER,    |
  // ROLE_LIBRARIAN, | Three fundamental roles define in AccountRole
  // ROLE_ADMIN,     | 
                    .requestMatchers("/table", "/blank").hasAnyAuthority("ROLE_ADMIN", "ROLE_LIBRARIAN")
                    .requestMatchers("/profile").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEMBER", "ROLE_LIBRARIAN")
                    .anyRequest()
                    .authenticated()).exceptionHandling(exception -> exception.accessDeniedPage("/access-denied"))
        .formLogin((form) -> form.loginPage("/login").permitAll())
        .logout(LogoutConfigurer::permitAll)
        .build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  UserDetailsService userDetailService() {
    return customUserDetails;
  }

  @Bean
  AuthenticationProvider provider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(customUserDetails);
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

    return daoAuthenticationProvider;
  }
}
