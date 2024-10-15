package com.example.library_management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {
  @Bean
  OpenAPI openAPI() {
    return new OpenAPI().info(new Info().title("Hello Swagger, my name is Chinh").version("3.0.1"));
  }
}
