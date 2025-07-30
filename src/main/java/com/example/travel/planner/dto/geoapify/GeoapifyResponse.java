package com.example.travel.planner.dto.geoapify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeoapifyResponse {

    private String country;
    private String city;
    @JsonProperty("lon")
    private double longitude;
    @JsonProperty("lat")
    private double latitude;
    @JsonProperty("formatted")
    private String address;
    @JsonProperty("place_id")
    private String placeId;
}
