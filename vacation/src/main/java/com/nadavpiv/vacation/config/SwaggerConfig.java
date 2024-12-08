package com.nadavpiv.vacation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2 // Enables Swagger 2 for the Spring Boot project, which is used to generate API documentation.
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2) // Specify that we're using Swagger 2 for documentation.
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build(); // Builds the Docket instance with the selected configurations.
    }
}

