package com.example.travel.planner.service.impl;

import com.example.travel.planner.client.GeoapifyClient;
import com.example.travel.planner.mapper.LocationMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GeoapifyServiceTest {

    @Mock
    private GeoapifyClient geoapifyClient;

    @Mock
    private LocationMapper locationMapper;

    @InjectMocks
    private GeoapifyService geoapifyService;

    @Test
    void searchLocation() {
        String query = "Encoded query";
        String encodedQuery = "Encoded+query";

        geoapifyService.searchLocation(query);

        verify(geoapifyClient).searchLocation(encodedQuery);
        verify(locationMapper).fromGeoapify(any());
    }
}