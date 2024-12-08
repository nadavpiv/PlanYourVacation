package com.nadavpiv.vacation.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Value("${frontend.url}")
    private String frontendUrl;

    private static final Logger logger = LoggerFactory.getLogger(OAuth2LoginFailureHandler.class);

    // Constructor that sets the default failure URL to "/login".
    public OAuth2LoginFailureHandler() {
        this.setDefaultFailureUrl("/login"); // Set the failure URL to redirect to in case of failure.
    }

    // This method is invoked when authentication fails (e.g., wrong credentials or authentication errors).
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        org.springframework.security.core.AuthenticationException exception)
            throws IOException, ServletException {

        logger.error("Authentication failed: {}", exception.getMessage()); // Log the authentication failure error message, including the exception message for better debugging.

        super.onAuthenticationFailure(request, response, exception);  // Call the superclass method to perform the default failure handling (e.g., redirect to failure URL).
    }
}
