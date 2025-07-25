package com.example.travel.planner.service.impl;

import com.example.travel.planner.client.GeoapifyClient;
import com.example.travel.planner.dto.geoapify.GeoapifyResponse;
import com.example.travel.planner.entity.embeded.Location;
import com.example.travel.planner.exception.LocationNotFoundException;
import com.example.travel.planner.mapper.LocationMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeoapifyServiceTest {

    @Mock
    private GeoapifyClient geoapifyClient;

    @Mock
    private LocationMapper locationMapper;

    @InjectMocks
    private GeoapifyService geoapifyService;

    private GeoapifyResponse testResponse;
    private Location testLocation;

    @BeforeEach
    void setUp() {

        Map<String, Object> testPropertiesMap = Map.of(
                "city", "Kyiv",
                "country", "Ukraine",
                "lat", 50.4501,
                "lon", 30.5234
        );

        ObjectMapper mapper = new ObjectMapper();
        JsonNode testProperties = mapper.convertValue(testPropertiesMap, ObjectNode.class);
        testResponse = GeoapifyResponse.success(testProperties);

        testLocation = new Location();
        testLocation.setCity("Kyiv");
        testLocation.setCountry("Ukraine");
        testLocation.setLatitude(50.4501);
        testLocation.setLongitude(30.5234);
    }

    @Test
    void searchLocation_shouldReturnLocation_whenLocationFound() {
        String query = "Kyiv Ukraine";
        String encodedQuery = "Kyiv+Ukraine";

        when(geoapifyClient.searchLocation(encodedQuery)).thenReturn(testResponse);
        when(locationMapper.fromGeoapify(any(JsonNode.class))).thenReturn(testLocation);

        Location result = geoapifyService.searchLocation(query);

        assertNotNull(result);
        assertEquals("Kyiv", result.getCity());
        assertEquals("Ukraine", result.getCountry());
        assertEquals(50.4501, result.getLatitude());
        assertEquals(30.5234, result.getLongitude());
        verify(geoapifyClient).searchLocation(encodedQuery);
        verify(locationMapper).fromGeoapify(any(JsonNode.class));
    }

    @Test
    void searchLocation_shouldThrowLocationNotFoundException_whenLocationNotFound() {
        String query = "NonexistentCity";
        String encodedQuery = "NonexistentCity";

        GeoapifyResponse failedResponse = GeoapifyResponse.empty();

        when(geoapifyClient.searchLocation(encodedQuery)).thenReturn(failedResponse);

        assertThrows(LocationNotFoundException.class, () -> geoapifyService.searchLocation(query));
        verify(geoapifyClient).searchLocation(encodedQuery);
        verifyNoInteractions(locationMapper);
    }

    @Test
    void searchLocation_shouldHandleQueryWithSpaces() {
        String query = "New York USA";
        String encodedQuery = "New+York+USA";

        when(geoapifyClient.searchLocation(encodedQuery)).thenReturn(testResponse);
        when(locationMapper.fromGeoapify(any(JsonNode.class))).thenReturn(testLocation);

        Location result = geoapifyService.searchLocation(query);

        assertNotNull(result);
        verify(geoapifyClient).searchLocation(encodedQuery);
        verify(locationMapper).fromGeoapify(any(JsonNode.class));
    }

    @Test
    void searchLocation_shouldTrimWhitespaceFromQuery() {
        String query = "  Kyiv Ukraine  ";
        String encodedQuery = "Kyiv+Ukraine";

        when(geoapifyClient.searchLocation(encodedQuery)).thenReturn(testResponse);
        when(locationMapper.fromGeoapify(any(JsonNode.class))).thenReturn(testLocation);

        Location result = geoapifyService.searchLocation(query);

        assertNotNull(result);
        verify(geoapifyClient).searchLocation(encodedQuery);
        verify(locationMapper).fromGeoapify(any(JsonNode.class));
    }

}