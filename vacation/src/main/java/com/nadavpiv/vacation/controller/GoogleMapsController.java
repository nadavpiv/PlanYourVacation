package com.nadavpiv.vacation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nadavpiv.vacation.service.GoogleMapsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/vacations")
public class GoogleMapsController {
    private static final Logger logger = LoggerFactory.getLogger(GoogleMapsController.class);
    private final GoogleMapsService googleMapsService; // Service to interact with Google Maps API.

    // Constructor-based dependency injection of GoogleMapsService.
    @Autowired
    public GoogleMapsController(GoogleMapsService googleMapsService) {
        this.googleMapsService = googleMapsService;
    }

    // Endpoint to get photos from Google Maps based on latitude and longitude.
    @RequestMapping(value = "/photos", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getPhotos(@RequestParam double latitude, @RequestParam double longitude) {
        try {
            logger.info("Fetching photos for coordinates: latitude={}, longitude={}", latitude, longitude);

             // Call the service to get photos by coordinates.
            List<String> photos = googleMapsService.getPhotosByCoordinate(latitude, longitude);

            // Return the photos as a successful response (HTTP 200).
            return ResponseEntity.ok(photos);
        } catch (JsonProcessingException e) {
            // Log the exception
            logger.error("Error while processing Google Maps photos for coordinates: latitude={}, longitude={}", latitude, longitude, e);

            // Return an internal server error response (HTTP 500).
            return ResponseEntity.status(500).body(null);
        }
    }
}
