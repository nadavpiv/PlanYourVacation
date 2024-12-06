package com.nadavpiv.vacation.controller;

import com.nadavpiv.vacation.model.Vacation;
import com.nadavpiv.vacation.model.VacationRequest;
import com.nadavpiv.vacation.service.ChatGPTService;
import com.nadavpiv.vacation.service.GoogleMapsService;
import com.nadavpiv.vacation.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000") // React default port
@RestController
@RequestMapping("/api/vacations")
public class VacationController {
    private static final Logger logger = LoggerFactory.getLogger(VacationController.class);

    private final ChatGPTService chatGPTService;
    private final GoogleMapsService googleMapsService;
    private final VacationService vacationService;
    @Autowired
    public VacationController(ChatGPTService chatGPTService, GoogleMapsService googleMapsService, VacationService vacationService) {
        this.chatGPTService = chatGPTService;
        this.googleMapsService = googleMapsService;
        this.vacationService = vacationService;
    }
    @RequestMapping(value = "filter", method = RequestMethod.GET)
    public ResponseEntity<?> getVacationsByFilters(@Valid VacationRequest vacationRequest) {
        logger.info("Request for getting vacations by filters");

        List<Vacation> existingVacations = vacationService.findVacationsByFilters(
                vacationRequest.getCountry(),
                vacationRequest.getPassengers(),
                vacationRequest.getMonth(),
                vacationRequest.getMinDays() - 1,
                vacationRequest.getMaxDays() + 1,
                vacationRequest.getMinBudget() - 1,
                vacationRequest.getMaxBudget() + 1,
                vacationRequest.getBaseCountry(),
                vacationRequest.getRoadTrip()
        );

        // Randomly shuffle the list
        Collections.shuffle(existingVacations);

        // Limit to a maximum of 8 vacations
        List<Vacation> randomVacations = existingVacations.size() > 8
                ? existingVacations.subList(0, 8)
                : existingVacations;

        return new ResponseEntity<>(randomVacations, HttpStatus.OK);
    }
    @RequestMapping(value = "",method = RequestMethod.GET)
    public ResponseEntity<?> getAllVacations() {
        logger.info("Request for getting all vacations");
        return new ResponseEntity<>(vacationService.getAllVacations(),HttpStatus.OK);
    }
    @RequestMapping(value = "/vacation/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getVacationDetails(@PathVariable String id) {
        logger.info("Request for getting specific vacation by id");
        try {
            // Fetch the vacation by ID
            Vacation vacation = vacationService.getVacationById(id);
            // Return the vacation with all its details
            return new ResponseEntity<>(vacation, HttpStatus.OK);

        } catch (Exception e) {
            // Handle errors
            logger.error("Error fetching vacation details for ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching vacation details.");
        }
    }
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public ResponseEntity<?> createVacationWithGPT(@RequestBody VacationRequest vacationRequest) {
        Vacation vacation = chatGPTService.getVacationOptions(vacationRequest);

        if (vacation == null) {
            // If vacation is empty, log it and exit the method
            logger.info("No vacation options found, skipping further processing.");
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        // Check if the new vacation is a duplicate of the existing ones
        boolean isDuplicate = vacationService.isDuplicateVacation(vacation);

        // If the new vacation is a duplicate, return existing vacations without saving the duplicate
        if (isDuplicate) {
            logger.info("Duplicate vacation found for city: {}", vacation.getCity());
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        logger.info("Vacation city: {}", vacation.getCity());
        // If the new vacation is not a duplicate, save the new vacation
        vacationService.saveVacation(vacation);

        // Return the new vacation
        return new ResponseEntity<>(List.of(vacation), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Vacation> updateVacationById(@PathVariable String id, @RequestBody Vacation updatedVacation) {
        logger.info("Request for update vacations by id");
        Optional<Vacation> existingVacation = Optional.ofNullable(vacationService.getVacationById(id));
        if (existingVacation.isEmpty()) {
            throw new RuntimeException("Vacation with id: " + id + " not found");
        }

        updatedVacation.updateVacation(existingVacation.get());
        vacationService.saveVacation(updatedVacation);
        return new ResponseEntity<>(updatedVacation, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteVacation(@PathVariable String id) {
        logger.info("Request for delete vacation by id");
        Optional<Vacation> dbVacation = Optional.ofNullable(vacationService.getVacationById(id));
        if (dbVacation.isEmpty())
            throw new RuntimeException("Vacation with id: " + id + " not found");
        vacationService.delete(dbVacation.get());
        return new ResponseEntity<>("DELETED", HttpStatus.OK);
    }
}








