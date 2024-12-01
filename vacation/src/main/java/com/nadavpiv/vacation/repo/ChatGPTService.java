package com.nadavpiv.vacation.repo;

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

    @Value("${openai.api.key}")
    private String apiKey;
    @Autowired
    private GoogleMapsService googleMapsService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String apiUrl = "https://api.openai.com/v1/chat/completions";

    public Vacation getVacationOptions(VacationRequest request) {
        // If the user don't ask for specific city we replace it to "Surprise me"
        if (request.getCity().equals(""))
            request.setCity("Surprise me");

        try {
            // Build the user prompt from the request object
            String userPrompt = ChatGPTPromptBuilder.buildVacationPrompt(request);
            // Create the request body for the chat model
            String requestBody = buildChatGPTRequestBody(userPrompt);

            // Set up headers with the authorization token
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set("Content-Type", "application/json");

            // Create HTTP entity, Initialize RestTemplate and Send the post request
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

            // Parse and extract the "content" field from the response JSON and convert the content to Vacaction object
            String content = extractContent(response.getBody());
            Vacation vacation = parseVacations(content);
            /*
            // Get the photos from Google api for each attraction
            for (Vacation.DayOption dayOption : vacation.getDays()) {
                List<Vacation.Attraction> attractions = dayOption.getAttractions();
                List<Vacation.Attraction> enrichedAttractions = enrichAttractionsWithPhotos(attractions);
                dayOption.setAttractions(enrichedAttractions);
            }

             */

            return vacation;

        } catch (Exception e) {
            logger.error("Error during vacation request processing", e);
            throw new RuntimeException("Failed to get vacation options from ChatGPT API", e);
        }
    }
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

    private String extractContent(String responseJson) {
        try {
            JsonNode root = objectMapper.readTree(responseJson);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract content from OpenAI response", e);
        }
    }

    private Vacation parseVacations(String vacationJsonContent) {
        try {
            JsonNode rootNode = objectMapper.readTree(vacationJsonContent);
            return objectMapper.treeToValue(rootNode.path("vacationOptions").get(0), Vacation.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse vacation options", e);
        }
    }
    private List<Vacation.Attraction> enrichAttractionsWithPhotos(List<Vacation.Attraction> attractions) throws JsonProcessingException {
        for (Vacation.Attraction attraction : attractions) {
            if (attraction.getLatitude() != 0 && attraction.getLongitude() != 0) {
                double latitude = attraction.getLatitude();
                double longitude = attraction.getLongitude();
                List<String> photos = googleMapsService.getPhotosByCoordinate(latitude, longitude);
                attraction.setPhotos(photos);
            }
        }
        return attractions;
    }
}












