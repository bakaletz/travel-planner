package com.example.travel.planner.service.impl;

import com.example.travel.planner.client.GeoapifyClient;
import com.example.travel.planner.dto.geoapify.GeoapifyResponse;
import com.example.travel.planner.entity.embeded.Location;
import com.example.travel.planner.exception.LocationNotFoundException;
import com.example.travel.planner.mapper.LocationMapper;
import com.example.travel.planner.service.GeoService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Getter @Setter
public class GeoapifyService implements GeoService {

    private final LocationMapper locationMapper;
    private final GeoapifyClient geoapifyClient;

    public Location searchLocation(String query) {

        String encodedQuery = prepareQuery(query);

        GeoapifyResponse response = geoapifyClient.searchLocation(encodedQuery);

        if (!response.isSuccess()) {
            throw new LocationNotFoundException("Location not found for query: " + query);
        }

        return locationMapper.fromGeoapify(response.getProperties());
    }


    private String prepareQuery(String query) {
        return URLEncoder.encode(query.trim(), StandardCharsets.UTF_8)
                .replace("%20", "+");
    }
}