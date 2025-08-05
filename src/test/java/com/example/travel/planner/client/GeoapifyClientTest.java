package com.example.travel.planner.client;

import com.example.travel.planner.exception.ExternalApiException;
import com.example.travel.planner.exception.LocationNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeoapifyClientTest {

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private GeoapifyClient geoapifyClient;
    @Mock
    private ObjectMapper mapper;

    private String url;

    @BeforeEach
    void setUp() {
        url = "test.com";
        ReflectionTestUtils.setField(geoapifyClient, "apiUrl", url);
        ReflectionTestUtils.setField(geoapifyClient, "apiKey", "dummy-key");
        mapper = new ObjectMapper();
    }

    @Test
    void searchLocation_shouldMapToGeoapifyResponse_ifDataIsValid() {
        ObjectNode rootNode = mapper.createObjectNode();
        ArrayNode featuresArray = mapper.createArrayNode();
        featuresArray.add(mapper.createObjectNode());
        rootNode.set("features", featuresArray);
        ResponseEntity<JsonNode> response = new ResponseEntity<>(rootNode, HttpStatus.OK);

        when(restTemplate.getForEntity(url, JsonNode.class)).thenReturn(response);

        geoapifyClient.searchLocation("Test");

        verify(restTemplate).getForEntity(url, JsonNode.class);
    }

    @Test
    void searchLocation_shouldThrowRestClientException_ifItsProblemWithExternalAPI(){
        when(restTemplate.getForEntity(url, JsonNode.class)).thenThrow(new RestClientException(""));

        assertThrows(ExternalApiException.class, () -> geoapifyClient.searchLocation("Test"));
    }

    @Test
    void searchLocation_shouldThrowLocationNotFoundException_ifBodyIsNull() {
        ResponseEntity<JsonNode> response = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.getForEntity(url, JsonNode.class)).thenReturn(response);

        assertThrows(LocationNotFoundException.class, () -> geoapifyClient.searchLocation("Test"));
    }

    @Test
    void searchLocation_shouldThrowLocationNotFoundException_ifHasNotFeatures(){
        ObjectNode rootNode = mapper.createObjectNode();
        ResponseEntity<JsonNode> response = new ResponseEntity<>(rootNode, HttpStatus.OK);

        when(restTemplate.getForEntity(url, JsonNode.class)).thenReturn(response);

        assertThrows(LocationNotFoundException.class, () -> geoapifyClient.searchLocation("Test"));
    }

    @Test
    void searchLocation_shouldThrowLocationNotFoundException_ifFeaturesIsEmpty() {
        ObjectNode rootNode = mapper.createObjectNode();
        ArrayNode featuresArray = mapper.createArrayNode();
        rootNode.set("features", featuresArray);
        ResponseEntity<JsonNode> response = new ResponseEntity<>(rootNode, HttpStatus.OK);

        when(restTemplate.getForEntity(url, JsonNode.class)).thenReturn(response);

        assertThrows(LocationNotFoundException.class, () -> geoapifyClient.searchLocation("Test"));
    }
}
