package com.nadavpiv.vacation.repo;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Map;
@Service
public class AuthenticationService {
    public String extractEmailFromAuthentication(Authentication authentication) {
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
