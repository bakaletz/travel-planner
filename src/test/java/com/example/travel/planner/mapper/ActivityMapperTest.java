package com.example.travel.planner.mapper;

import com.example.travel.planner.dto.activity.ActivityCreateDTO;
import com.example.travel.planner.dto.activity.ActivityDTO;
import com.example.travel.planner.dto.activity.ActivityUpdateDTO;
import com.example.travel.planner.entity.embeded.Activity;
import com.example.travel.planner.entity.embeded.Location;
import com.example.travel.planner.testdata.TestActivityProvider;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ActivityMapperTest {

    private final ActivityMapper mapper = Mappers.getMapper(ActivityMapper.class);

    @Test
    void toEntity_shouldMapAllFields() {
        ActivityCreateDTO dto = TestActivityProvider.createActivityCreateDTO();
        Location location = Location.builder()
                .city("Test")
                .build();

        Activity entity = mapper.toEntity(dto, location);

        assertEquals(dto.getTitle(), entity.getTitle());
        assertThat(entity.getLocation()).isEqualTo(location);
    }

    @Test
    void toDto_shouldMapAllFields() {
        Activity activity = TestActivityProvider.createDefaultActivity();

        ActivityDTO dto = mapper.toDto(activity);

        assertThat(dto.getTitle()).isEqualTo(activity.getTitle());
    }

    @Test
    void updateEntityFromDto_shouldUpdateEntityFields() {
        ActivityUpdateDTO dto = TestActivityProvider.createActivityUpdateDTO();
        Activity activity = TestActivityProvider.createDefaultActivity();

        mapper.updateEntityFromDto(dto, activity);

        assertThat(activity.getTitle()).isEqualTo(dto.getTitle());
    }
}