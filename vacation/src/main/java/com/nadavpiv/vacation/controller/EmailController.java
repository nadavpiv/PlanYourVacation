package com.nadavpiv.vacation.controller;

import com.nadavpiv.vacation.model.Vacation;
import com.nadavpiv.vacation.oauth.AuthenticationEmailService;
import com.nadavpiv.vacation.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "http://localhost:3000") // React default port
@RestController
@RequestMapping("/api/vacations")
public class EmailController {

    @Autowired
    private EmailService emailService; // Injects EmailService to handle email sending.
    @Autowired
    private AuthenticationEmailService authenticationEmailService; // Injects service to extract email from authentication.
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    // Endpoint to send vacation details via email.
    @PostMapping("/sendVacationDetails")
    public ResponseEntity<String> sendVacationDetails(@RequestBody Vacation vacationDetails, Authentication authentication) {
        // Extract user email from the authentication object.
        String userEmail = authenticationEmailService.extractEmailFromAuthentication(authentication);

        // If the user email is not found or the user is not authenticated, return a BAD_REQUEST response.
        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User email not found or user not authenticated.");
        }

        try {
            // Send an email with the vacation details to the user's email.
            emailService.sendEmail(userEmail, "Vacation Details", vacationDetails);
            logger.info("Email sent successfully to " + userEmail);
            return ResponseEntity.ok("Email sent successfully to " + userEmail); // Return a successful response.
        } catch (Exception e) {
            // If there was an error while sending the email, log the error and return an INTERNAL_SERVER_ERROR response.
            logger.error("Failed to send email to " + userEmail, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email.");
        }
    }
}
