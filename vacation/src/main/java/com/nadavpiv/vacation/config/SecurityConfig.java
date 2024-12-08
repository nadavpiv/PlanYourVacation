package com.nadavpiv.vacation.config;

import com.nadavpiv.vacation.oauth.CustomAuthenticationEntryPoint;
import com.nadavpiv.vacation.oauth.OAuth2LoginFailureHandler;
import com.nadavpiv.vacation.oauth.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Injecting custom handlers for OAuth2 login success and failure.
    @Autowired
    private OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    // The frontend URL is fetched from application properties to be used in CORS configuration.
    @Value("${frontend.url}")
    private String frontendUrl;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable() // Consider enabling CSRF protection if needed
                .cors().configurationSource(corsConfigurationSource()) // CORS configuration
                .and()
                .authorizeRequests() // Start defining access control rules.
                .antMatchers("/login", "/swagger-ui.html",
                        "/api/vacations/swagger-ui.html",
                        "/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/webjars/**",
                        "/**/*.js", "/**/*.css", "/index.html").permitAll() // Allow public access to login and Swagger-related endpoints.
                .anyRequest().authenticated() // All other endpoints require authentication.
                .and()
                .oauth2Login() // Enable OAuth2 login functionality.
                    .failureHandler(oAuth2LoginFailureHandler)  // Custom failure handler when OAuth2 login fails.
                    .successHandler(oAuth2LoginSuccessHandler)  // Custom success handler after successful OAuth2 login.
                    .loginPage("/login")  // Optional custom login page (could be a custom UI).
                .and()
                .exceptionHandling() // Define exception handling for unauthorized access.
                    .authenticationEntryPoint(customAuthenticationEntryPoint); // Custom entry point for unauthorized requests (e.g., expired token).

        return http.build(); // Return the fully configured HTTP security filter chain.
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        // Configure CORS to allow frontend requests.
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontendUrl)); // Allow requests only from the frontend URL (defined in application.properties).
        configuration.addAllowedHeader("*"); // Allow any header (make sure this is secure based on your use case).
        configuration.addAllowedMethod("*"); // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.).
        configuration.setAllowCredentials(true); // Allow cookies and credentials to be sent.

        // Register CORS configuration to apply for all routes.
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);
        return urlBasedCorsConfigurationSource; // Return the configured CORS source.
    }
}
