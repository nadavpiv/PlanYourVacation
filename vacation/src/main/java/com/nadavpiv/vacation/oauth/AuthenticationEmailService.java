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
    public String extractEmailFromAuthentication(Authentication authentication) {
        logger.info("Extract the email that the user login with for sending mail");
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof OAuth2User) {
                OAuth2User oAuth2User = (OAuth2User) principal;
                Map<String, Object> attributes = oAuth2User.getAttributes();
                return (String) attributes.get("email"); // Adjust the key if needed
            }
        }
        return null;
    }
}
