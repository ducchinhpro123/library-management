package com.example.library_management.config;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired private CustomUserDetails customUserDetails;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
      // ROLE_MEMBER,    |
      // ROLE_LIBRARIAN, | Three fundamental roles define in AccountRole
      // ROLE_ADMIN,     | 
      .authorizeHttpRequests(
            (requests) ->
                requests
                    // .requestMatchers("/profile").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEMBER", "ROLE_LIBRARIAN")
                    .requestMatchers("/admin/**", "/blank").hasRole("ADMIN")
                    .requestMatchers("/", "/registration", "/assets/**", "/images/**", "/login", "/book/**", "api/book/filter/*")
                    .permitAll()
                    .anyRequest()
                    .authenticated()).exceptionHandling(exception -> exception.accessDeniedPage("/access-denied"))
        .formLogin((form) -> form.loginPage("/login")
            .defaultSuccessUrl("/")
            .failureUrl("/login?error=true")
            .failureHandler(authenticationFailureHander())
            .permitAll())
        .logout(LogoutConfigurer::permitAll)
        .build();
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHander() {
    return new SimpleUrlAuthenticationFailureHandler() {
      @Override
      public void onAuthenticationFailure(HttpServletRequest request, 
          HttpServletResponse response,
          AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "Invalid username or password";
        if (exception instanceof BadCredentialsException) {
          errorMessage = "Invalid username or password";
        } else if (exception instanceof LockedException) {
          errorMessage = "Account is locked";
        } else if (exception instanceof DisabledException) {
          errorMessage = "Account is disabled";
        } else if (exception instanceof AccountExpiredException) {
          errorMessage = "Account has expired";
        }

        String redirectUrl = "/login?error=true&&message=" +
          URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        setDefaultFailureUrl(redirectUrl);
        super.onAuthenticationFailure(request, response, exception);

      }
    };
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
  DaoAuthenticationProvider provider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(customUserDetails);
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

    return daoAuthenticationProvider;
  }
  // AuthenticationProvider provider() {
  // }
}
