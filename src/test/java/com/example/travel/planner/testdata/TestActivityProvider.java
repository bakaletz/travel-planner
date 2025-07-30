package com.example.travel.planner.testdata;

import com.example.travel.planner.dto.activity.ActivityCreateDTO;
import com.example.travel.planner.dto.activity.ActivityDTO;
import com.example.travel.planner.dto.activity.ActivityUpdateDTO;
import com.example.travel.planner.entity.embeded.Activity;

public class TestActivityProvider {

    public static final String ACTIVITY_ID = "activity-123";

    public static Activity createDefaultActivity() {
        return Activity.builder()
                .id(ACTIVITY_ID)
                .title("Test Activity")
                .build();
    }

    public static ActivityDTO createActivityDTO() {
        return ActivityDTO.builder()
                .id(ACTIVITY_ID)
                .title("Test Activity")
                .build();
    }

    public static ActivityCreateDTO createActivityCreateDTO() {
        return ActivityCreateDTO.builder()
                .title("Test Activity")
                .locationQuery("Kyiv Ukraine")
                .build();
    }

    public static ActivityUpdateDTO createActivityUpdateDTO() {
        return ActivityUpdateDTO.builder()
                .title("Updated Activity")
                .build();
    }

}
