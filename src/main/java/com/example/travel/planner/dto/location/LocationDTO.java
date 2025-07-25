package com.example.travel.planner.dto.location;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDTO {

    private String country;
    private String city;
    private double longitude;
    private double latitude;
    private String address;
    private String placeId;
}
