package com.example.travel.planner.dto.geoapify;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeoapifyResponse {
    private final boolean success;
    private final JsonNode properties;

    public static GeoapifyResponse success(JsonNode properties) {
        return new GeoapifyResponse(true, properties);
    }

    public static GeoapifyResponse empty() {
        return new GeoapifyResponse(false, null);
    }
}
