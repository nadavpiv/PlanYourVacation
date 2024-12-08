package com.nadavpiv.vacation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nadavpiv.vacation.prompts.ChatGPTPromptBuilder;
import com.nadavpiv.vacation.model.Vacation;
import com.nadavpiv.vacation.model.VacationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ChatGPTService {
    private static final Logger logger = LoggerFactory.getLogger(ChatGPTService.class);

    @Value("${openai.api.key}") // Injecting the OpenAI API key from the application properties.
    private String apiKey;
    @Value("${openai.url}") // Injecting the OpenAI URL endpoint from the application properties.
    private String apiUrl;
    @Autowired
    private GoogleMapsService googleMapsService; // Injecting the Google Maps service to fetch photos for attractions.
    @Autowired
    private ObjectMapper objectMapper; // Injecting ObjectMapper for JSON parsing and object mapping.

    // Method to get vacation options based on the user's vacation request.
    public Vacation getVacationOptions(VacationRequest request) {
        // If the user doesn't provide a specific city, set it to "Surprise me".
        if (request.getCity().equals(""))
            request.setCity("Surprise me");

        String requestBody;
        try {
            // Build the prompt to send to ChatGPT using the user's vacation request.
            String userPrompt = ChatGPTPromptBuilder.buildVacationPrompt(request);
            requestBody = buildChatGPTRequestBody(userPrompt); // Prepare the request body for the ChatGPT API.
        } catch (Exception e) {
            logger.error("Error during building the prompt or the body request", e);
            return new Vacation(); // Return an empty vacation object if error occurs.
        }


        // Set up headers for the HTTP request including the authorization token.
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        ResponseEntity<String> response;
        try {
             // Create the HTTP entity (with headers and body), then send the POST request using RestTemplate.
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            RestTemplate restTemplate = new RestTemplate();
            response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class); // Send POST request.
        }
        catch (Exception e) {
            logger.error("Error Create HTTP entity, or send the post request", e); // Log error if HTTP request fails.
            return new Vacation();  // Return an empty vacation object if request fails.
        }

         // Try to parse the response and extract vacation details from ChatGPT's response.
        String content = null;
        Vacation vacation = new Vacation();  // Default empty vacation object.
        try {
            content = extractContent(response.getBody()); // Extract the content of the response.
            vacation = parseVacations(content); // Parse the content into a Vacation object.
        }
        catch (Exception e) {
            logger.error("Error during Chat GPT response, we cant build vacation", e); // Log error if response parsing fails.
            vacation = new Vacation();  // Ensure an empty vacation object is returned.
        }

        // Try to get photos for the attractions from Google API.
        try {
            // Get the photos from Google API for each attraction
            for (Vacation.DayOption dayOption : vacation.getDays()) {
                List<Vacation.Attraction> attractions = dayOption.getAttractions();
                List<Vacation.Attraction> enrichedAttractions = enrichAttractionsWithPhotos(attractions);
                dayOption.setAttractions(enrichedAttractions); // Update the attractions with photos.
            }
        }
        catch (Exception e){
            logger.error("Error during get the photos from google api", e); // Log error if fetching photos fails.
            vacation = new Vacation();   // Return empty vacation if photo fetching fails.
        }

        logger.info("Success in getting a vacation from Chat GPT");
        return vacation; // Return the vacation object (either populated or empty depending on success/failure).
    }

    // Helper method to build the request body for ChatGPT API.
    private String buildChatGPTRequestBody(String userPrompt) {
        return String.format(
                "{\n" +
                        "  \"model\": \"gpt-3.5-turbo\",\n" +
                        "  \"messages\": [\n" +
                        "    {\"role\": \"system\", \"content\": \"You are a vacation planner.\"},\n" +
                        "    {\"role\": \"user\", \"content\": \"%s\"}\n" +
                        "  ],\n" +
                        "  \"max_tokens\": 3000,\n" +
                        "  \"temperature\": 0.7\n" +
                        "}", userPrompt);
    }

    // Method to extract the content from ChatGPT's JSON response.
    private String extractContent(String responseJson) {
        try {
            JsonNode root = objectMapper.readTree(responseJson); // Parse the response JSON into a JsonNode.
            return root.path("choices").get(0).path("message").path("content").asText(); // Extract the content field.
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract content from OpenAI response", e); // Throw error if extraction fails.
        }
    }

    // Method to parse the vacation options from the ChatGPT response content.
    private Vacation parseVacations(String vacationJsonContent) {
        try {
            JsonNode rootNode = objectMapper.readTree(vacationJsonContent); // Parse the vacation content into a JsonNode.
            return objectMapper.treeToValue(rootNode.path("vacationOptions").get(0), Vacation.class); // Map to Vacation object.
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse vacation options", e); // Throw error if parsing fails.
        }
    }

    // Helper method to enrich attractions with photos from Google Maps API.
    private List<Vacation.Attraction> enrichAttractionsWithPhotos(List<Vacation.Attraction> attractions) throws JsonProcessingException {
        for (Vacation.Attraction attraction : attractions) {
            if (attraction.getLatitude() != 0 && attraction.getLongitude() != 0) { // Only fetch photos if valid coordinates exist.
                double latitude = attraction.getLatitude();
                double longitude = attraction.getLongitude();
                List<String> photos = googleMapsService.getPhotosByCoordinate(latitude, longitude);
                attraction.setPhotos(photos); // Set the photos on the attraction.
            } 
        }
        return attractions; // Return the enriched list of attractions with photos.
    }
}











