package com.example.travel.planner.client;

import com.example.travel.planner.dto.geoapify.GeoapifyResponse;
import com.example.travel.planner.exception.ExternalApiException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class GeoapifyClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GeoapifyClient geoapifyClient;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(geoapifyClient, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(geoapifyClient, "apiUrl", "https://api.geoapify.com/v1/geocode/search?text=%s&apiKey=%s");
    }

    @Test
    void searchLocation_shouldReturnSuccess_whenValidResponse() {
        JsonNode mockJson = new ObjectMapper().createObjectNode();
        ((ObjectNode) mockJson).set("features", new ObjectMapper().createArrayNode().add(
                new ObjectMapper().createObjectNode().set("properties", new ObjectMapper().createObjectNode().put("key", "value"))
        ));

        ResponseEntity<JsonNode> response = ResponseEntity.ok(mockJson);
        when(restTemplate.getForEntity(anyString(), eq(JsonNode.class))).thenReturn(response);

        GeoapifyResponse result = geoapifyClient.searchLocation("Kyiv");

        assertTrue(result.isSuccess());
    }

    @Test
    void searchLocation_shouldReturnEmpty_whenNoFeatures() {
        JsonNode body = new ObjectMapper().createObjectNode().putArray("features");
        ResponseEntity<JsonNode> response = ResponseEntity.ok(body);
        when(restTemplate.getForEntity(anyString(), eq(JsonNode.class))).thenReturn(response);

        GeoapifyResponse result = geoapifyClient.searchLocation("Unknown");

        assertFalse(result.isSuccess());
    }

    @Test
    void searchLocation_shouldThrowException_whenRestClientFails() {
        when(restTemplate.getForEntity(anyString(), eq(JsonNode.class)))
                .thenThrow(new RestClientException("API error"));

        assertThrows(ExternalApiException.class, () -> geoapifyClient.searchLocation("fail"));
    }
}
