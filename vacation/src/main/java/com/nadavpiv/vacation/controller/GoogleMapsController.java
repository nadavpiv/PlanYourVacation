package com.nadavpiv.vacation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nadavpiv.vacation.repo.GoogleMapsService;
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
    private final GoogleMapsService googleMapsService;

    @Autowired
    public GoogleMapsController(GoogleMapsService googleMapsService) {
        this.googleMapsService = googleMapsService;
    }
    @RequestMapping(value = "/photos", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getPhotos(@RequestParam double latitude, @RequestParam double longitude) {
        try {
            logger.info("Fetching photos for coordinates: latitude={}, longitude={}", latitude, longitude);
            List<String> photos = googleMapsService.getPhotosByCoordinate(latitude, longitude);
            return ResponseEntity.ok(photos);
        } catch (JsonProcessingException e) {
            // Log the exception
            logger.error("Error while processing Google Maps photos for coordinates: latitude={}, longitude={}", latitude, longitude, e);
            return ResponseEntity.status(500).body(null);
        }
    }
}
