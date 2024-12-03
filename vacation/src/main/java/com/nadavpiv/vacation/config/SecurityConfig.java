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
    @Autowired
    private OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable() // Consider enabling CSRF protection if needed
                .cors().configurationSource(corsConfigurationSource()) // CORS configuration
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/swagger-ui.html",
                        "/api/vacations/swagger-ui.html",
                        "/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/webjars/**",
                        "/**/*.js", "/**/*.css", "/index.html").permitAll() // Public endpoints
                .anyRequest().authenticated() // All other endpoints require authentication
                .and()
                .oauth2Login()
                    .failureHandler(oAuth2LoginFailureHandler)  // Custom failure handler
                    .successHandler(oAuth2LoginSuccessHandler)  // Custom success handler
                    .loginPage("/login")  // Optional custom login page
                .and()
                .exceptionHandling()
                    .authenticationEntryPoint(customAuthenticationEntryPoint); // Use custom entry point for expired token

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontendUrl)); // Secure origin URL from application properties
        configuration.addAllowedHeader("*"); // Allow all headers (ensure this is secure)
        configuration.addAllowedMethod("*"); // Allow all methods (GET, POST, etc.)
        configuration.setAllowCredentials(true); // Allow credentials (cookies, etc.)

        // Register CORS configuration
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);
        return urlBasedCorsConfigurationSource;
    }
}
