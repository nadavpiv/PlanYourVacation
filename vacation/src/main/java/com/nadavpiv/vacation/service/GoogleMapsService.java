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
    private String apiKey;
    @Value("${google.places.api.url}")
    private String PLACES_API_URL;
    @Value("${google.photos.api.url}")
    private String PHOTOS_API_URL;
    private final Integer MAX_PHOTOS = 3;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<String> getPhotosByCoordinate(double latitude, double longitude) throws JsonProcessingException {

        // Build the Nearby Search API URL
        String nearbySearchUrl = UriComponentsBuilder.fromHttpUrl(PLACES_API_URL)
                .queryParam("location", latitude + "," + longitude)
                .queryParam("radius", 1000) // Increased search radius
                .queryParam("type", "tourist_attraction") // Filter for attractions
                .queryParam("key", apiKey)
                .toUriString();

        // Call the API
        String response = restTemplate.getForObject(nearbySearchUrl, String.class);

        // Parse the response
        JsonNode jsonNode = new ObjectMapper().readTree(response);
        JsonNode results = jsonNode.path("results");

        List<String> photoUrls = new ArrayList<>();
        int photoCount = 0;

        for (JsonNode result : results) {
            JsonNode photos = result.path("photos");
            if (photos.isArray()) {
                for (JsonNode photo : photos) {
                    if (photoCount == MAX_PHOTOS) break;
                    String photoReference = photo.path("photo_reference").asText();

                    // Build the photo URL
                    String photoUrl = UriComponentsBuilder.fromHttpUrl(PHOTOS_API_URL)
                            .queryParam("photoreference", photoReference)
                            .queryParam("key", apiKey)
                            .queryParam("maxwidth", 800) // Adjust width as needed
                            .toUriString();

                    photoUrls.add(photoUrl);
                    photoCount++;
                }
            }
        }
        return photoUrls;
    }
}

