package com.nadavpiv.vacation.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Map;
@Service
public class AuthenticationEmailService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationEmailService.class);

    // Method to extract email address from the Authentication object.
    public String extractEmailFromAuthentication(Authentication authentication) {
        logger.info("Extract the email that the user login with for sending mail");

        // Check if authentication is not null.
        if (authentication != null) {
            // Get the principal (user details) from the authentication object.
            Object principal = authentication.getPrincipal();

             // Check if the principal is of type OAuth2User (meaning OAuth2 login was used).
            if (principal instanceof OAuth2User) {
                OAuth2User oAuth2User = (OAuth2User) principal; // Cast the principal to OAuth2User.
                Map<String, Object> attributes = oAuth2User.getAttributes(); // Get the user attributes from OAuth2User.

                // Extract the email attribute from the user attributes map.
                return (String) attributes.get("email"); // Return the email associated with the user.
            }
        }
        return null;
    }
}
