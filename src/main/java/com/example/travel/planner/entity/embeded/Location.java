package com.example.travel.planner.entity.embeded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Location {

    private String country;
    private String city;
    private double longitude;
    private double latitude;
    private String address;
    private String placeId;
}
