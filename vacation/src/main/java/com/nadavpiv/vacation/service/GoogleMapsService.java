package com.nadavpiv.vacation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleMapsService {
    @Value("${google.maps.api.key}")
    private String apiKey; // API key for Google Maps services.
    @Value("${google.places.api.url}")
    private String PLACES_API_URL; // URL for Google Places API.
    @Value("${google.photos.api.url}")
    private String PHOTOS_API_URL; // URL for Google Photos API.
    private final Integer MAX_PHOTOS = 2; // Maximum number of photos to retrieve.

    private final RestTemplate restTemplate = new RestTemplate(); // REST template for making API calls.

    // Method to get photos of tourist attractions near a given coordinate (latitude, longitude).
    public List<String> getPhotosByCoordinate(double latitude, double longitude) throws JsonProcessingException {

        // Build the Nearby Search API URL with the required query parameters.
        String nearbySearchUrl = UriComponentsBuilder.fromHttpUrl(PLACES_API_URL)
                .queryParam("location", latitude + "," + longitude) // Set the location based on coordinates.
                .queryParam("radius", 1000) // Set the search radius (in meters).
                .queryParam("type", "tourist_attraction") // Filter for tourist attractions.
                .queryParam("key", apiKey) // API key for authentication.
                .toUriString(); // Convert the URL to a string format.

        // Make the API call to get nearby places.
        String response = restTemplate.getForObject(nearbySearchUrl, String.class);

        // Parse the JSON response using Jackson.
        JsonNode jsonNode = new ObjectMapper().readTree(response);
        JsonNode results = jsonNode.path("results"); // Extract the "results" array from the response.

        List<String> photoUrls = new ArrayList<>(); // List to store photo URLs.
        int photoCount = 0; // Counter to limit the number of photos.

        // Iterate through the results to find photos.
        for (JsonNode result : results) {
            JsonNode photos = result.path("photos"); // Get the photos field from each result.
            if (photos.isArray()) {
                for (JsonNode photo : photos) {
                    if (photoCount == MAX_PHOTOS) break; // Stop after getting the max number of photos.
                    String photoReference = photo.path("photo_reference").asText();

                    // Build the photo URL
                    String photoUrl = UriComponentsBuilder.fromHttpUrl(PHOTOS_API_URL)
                            .queryParam("photoreference", photoReference) // Set the photo reference.
                            .queryParam("key", apiKey) // API key for authentication.
                            .queryParam("maxwidth", 800) // Set max width for the photo.
                            .toUriString(); // Convert the URL to a string format.

                    photoUrls.add(photoUrl); // Add the photo URL to the list.
                    photoCount++; // Increment the photo counter.
                }
            }
        }
        return photoUrls; // Return the list of photo URLs.
    }
}

