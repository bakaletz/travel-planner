package com.example.travel.planner.service;

import com.example.travel.planner.entity.embeded.Location;

public interface GeoService {
    Location searchLocation(String query);
}
