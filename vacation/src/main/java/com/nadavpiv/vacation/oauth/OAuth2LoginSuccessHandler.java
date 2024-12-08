package com.nadavpiv.vacation.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(OAuth2LoginSuccessHandler.class);
    @Value("${frontend.url}")
    private String frontendUrl;

    // This method is called when the OAuth2 login is successful.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        this.setAlwaysUseDefaultTargetUrl(true);  // Set this flag to always use the default target URL after successful login.
        this.setDefaultTargetUrl(frontendUrl); // Set the default target URL to the frontend URL, which the user will be redirected to after successful login.

        if (authentication != null) {
            Object principal = authentication.getPrincipal(); // Get the principal (the authenticated user).

             // If the principal is an OAuth2User (which it should be in the case of OAuth2 login).
            if (principal instanceof OAuth2User) {
                OAuth2User oAuth2User = (OAuth2User) principal; // Cast the principal to OAuth2User.
                Map<String, Object> attributes = oAuth2User.getAttributes(); // Get the attributes of the authenticated user.
                logger.info("Successful login email: {} ", (String) attributes.get("email")); // Log the email of the authenticated user.
            }
        }

        // Call the parent class's onAuthenticationSuccess method to perform the default behavior.
        super.onAuthenticationSuccess(request, response, authentication);

    }
}
