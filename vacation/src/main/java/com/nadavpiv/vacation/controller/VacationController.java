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

    // Autowired services for vacation-related operations.
    private final ChatGPTService chatGPTService;
    private final GoogleMapsService googleMapsService;
    private final VacationService vacationService;

    // Constructor-based dependency injection.
    @Autowired
    public VacationController(ChatGPTService chatGPTService, GoogleMapsService googleMapsService, VacationService vacationService) {
        this.chatGPTService = chatGPTService;
        this.googleMapsService = googleMapsService;
        this.vacationService = vacationService;
    }

    // Endpoint to get vacations by various filters.
    @RequestMapping(value = "filter", method = RequestMethod.GET)
    public ResponseEntity<?> getVacationsByFilters(@Valid VacationRequest vacationRequest) {
        logger.info("Request for getting vacations by filters");

         // Fetch vacations that match the filters in the request.
        List<Vacation> existingVacations = vacationService.findVacationsByFilters(
                vacationRequest.getCountry(),
                vacationRequest.getPassengers(),
                vacationRequest.getMonth(),
                vacationRequest.getMinDays() - 1, // Adjust the day range for inclusive filter.
                vacationRequest.getMaxDays() + 1,
                vacationRequest.getMinBudget() - 1,
                vacationRequest.getMaxBudget() + 1,
                vacationRequest.getBaseCountry(),
                vacationRequest.getRoadTrip()
        );

        // Randomly shuffle the list to return a varied set of vacations.
        Collections.shuffle(existingVacations);

        // Limit the result to a maximum of 8 vacations.
        List<Vacation> randomVacations = existingVacations.size() > 8
                ? existingVacations.subList(0, 8)
                : existingVacations;

        // Return the filtered and randomized vacations.
        return new ResponseEntity<>(randomVacations, HttpStatus.OK);
    }

    // Endpoint to get all vacations.
    @RequestMapping(value = "",method = RequestMethod.GET)
    public ResponseEntity<?> getAllVacations() {
        logger.info("Request for getting all vacations");
        return new ResponseEntity<>(vacationService.getAllVacations(),HttpStatus.OK);
    }

    // Endpoint to get vacation details by vacation ID.
    @RequestMapping(value = "/vacation/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getVacationDetails(@PathVariable String id) {
        logger.info("Request for getting specific vacation by id");
        try {
            // Fetch the vacation by ID.
            Vacation vacation = vacationService.getVacationById(id);
            // Return the vacation details.
            return new ResponseEntity<>(vacation, HttpStatus.OK);

        } catch (Exception e) {
            // Handle errors
            logger.error("Error fetching vacation details for ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching vacation details.");
        }
    }

    // Endpoint to create a new vacation using ChatGPT-based generation.
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public ResponseEntity<?> createVacationWithGPT(@RequestBody VacationRequest vacationRequest) {
        // Generate vacation options using ChatGPT service.
        Vacation vacation = chatGPTService.getVacationOptions(vacationRequest);

        if (vacation == null) {
            // If no vacation is generated, log it and return an empty response.
            logger.info("No vacation options found, skipping further processing.");
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        // Check if the new vacation is a duplicate of the existing ones
        boolean isDuplicate = vacationService.isDuplicateVacation(vacation);

        // Check if the vacation is a duplicate before saving it.
        if (isDuplicate) {
            // If duplicate vacation is found, log and return empty response.
            logger.info("Duplicate vacation found for city: {}", vacation.getCity());
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        logger.info("Vacation city: {}", vacation.getCity());
        // Save the new vacation if it is not a duplicate.
        vacationService.saveVacation(vacation);

        // Return the newly created vacation.
        return new ResponseEntity<>(List.of(vacation), HttpStatus.CREATED);
    }

    // Endpoint to update an existing vacation by vacation ID.
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Vacation> updateVacationById(@PathVariable String id, @RequestBody Vacation updatedVacation) {
        logger.info("Request for update vacations by id");

        // Check if the vacation exists.
        Optional<Vacation> existingVacation = Optional.ofNullable(vacationService.getVacationById(id));
        if (existingVacation.isEmpty()) {
            // If vacation does not exist, throw an exception.
            throw new RuntimeException("Vacation with id: " + id + " not found");
        }

        // Update the vacation details.
        updatedVacation.updateVacation(existingVacation.get());
        vacationService.saveVacation(updatedVacation);

        // Return the updated vacation.
        return new ResponseEntity<>(updatedVacation, HttpStatus.OK);
    }

    // Endpoint to delete a vacation by vacation ID.
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteVacation(@PathVariable String id) {
        logger.info("Request for delete vacation by id");

        // Check if the vacation exists before deleting it.
        Optional<Vacation> dbVacation = Optional.ofNullable(vacationService.getVacationById(id));
        if (dbVacation.isEmpty())
            throw new RuntimeException("Vacation with id: " + id + " not found");

        // Delete the vacation from the database.
        vacationService.delete(dbVacation.get());

        // Return success message after deletion.
        return new ResponseEntity<>("DELETED", HttpStatus.OK);
    }
}








