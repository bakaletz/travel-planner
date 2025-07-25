package com.example.travel.planner.entity.embeded;

import lombok.*;

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
