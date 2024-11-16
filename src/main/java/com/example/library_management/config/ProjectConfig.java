package com.example.library_management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.example.library_management.controller")
@Configuration
public class ProjectConfig {
  @Bean
  OpenAPI openAPI() {
    return new OpenAPI().info(new Info().title("Hello Swagger, my name is Chinh").version("3.0.1"));
  }

  @Bean
  public AspectConfig aspect() {
    return new AspectConfig();
  }
}
