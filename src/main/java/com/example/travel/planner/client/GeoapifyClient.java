package com.example.travel.planner.client;

import com.example.travel.planner.dto.geoapify.GeoapifyResponse;
import com.example.travel.planner.exception.ExternalApiException;
import com.example.travel.planner.exception.LocationNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GeoapifyClient {

    @Value("${geoapify.api.key}")
    private String apiKey;

    @Value("${geoapify.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String FEATURES = "features";
    private static final String PROPS = "properties";

    public GeoapifyResponse searchLocation(String encodedQuery) {
        String url = buildUrl(encodedQuery);

        try {
            ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
            return mapToGeoapifyResponse(response, encodedQuery);
        } catch (RestClientException ex) {
            throw new ExternalApiException("Failed to call Geoapify API");
        }
    }

    private String buildUrl(String encodedQuery) {
        return String.format(apiUrl, encodedQuery, apiKey);
    }

    private GeoapifyResponse mapToGeoapifyResponse(ResponseEntity<JsonNode> response, String query) {
        if (response.getBody() == null) {
            throw new LocationNotFoundException(query);
        }

        JsonNode body = response.getBody();

        if (!body.has(FEATURES) || body.get(FEATURES).isEmpty()) {
            throw new LocationNotFoundException(query);
        }

        JsonNode propertiesNode = body.get(FEATURES).get(0).get(PROPS);

        return objectMapper.convertValue(propertiesNode, GeoapifyResponse.class);
    }
}

