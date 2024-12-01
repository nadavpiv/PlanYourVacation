package com.nadavpiv.vacation.controller;

import com.nadavpiv.vacation.model.Vacation;
import com.nadavpiv.vacation.repo.AuthenticationService;
import com.nadavpiv.vacation.repo.EmailService;
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
    private EmailService emailService;
    @Autowired
    private AuthenticationService authenticationService;
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @PostMapping("/sendVacationDetails")
    public ResponseEntity<String> sendVacationDetails(@RequestBody Vacation vacationDetails, Authentication authentication) {
        // Fetch user email from authentication object
        String userEmail = authenticationService.extractEmailFromAuthentication(authentication);
        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User email not found or user not authenticated.");
        }

        try {
            // Send email to the user
            emailService.sendEmail(userEmail, "Vacation Details", vacationDetails);
            return ResponseEntity.ok("Email sent successfully to " + userEmail);
        } catch (Exception e) {
            logger.error("Failed to send email to " + userEmail, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email.");
        }
    }
}

