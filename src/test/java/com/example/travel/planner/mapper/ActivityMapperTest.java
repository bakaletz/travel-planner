package com.example.travel.planner.mapper;

import com.example.travel.planner.dto.activity.ActivityCreateDTO;
import com.example.travel.planner.dto.activity.ActivityDTO;
import com.example.travel.planner.entity.embeded.Activity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class ActivityMapperTest {

    private final ActivityMapper mapper = Mappers.getMapper(ActivityMapper.class);

    @Test
    void toEntity_shouldMapAllFields() {
        ActivityCreateDTO dto = new ActivityCreateDTO();
        dto.setTitle("Hiking");
        dto.setDescription("Mountain hiking");

        Activity entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getTitle()).isEqualTo("Hiking");
        assertThat(entity.getDescription()).isEqualTo("Mountain hiking");
    }

    @Test
    void toDto_shouldMapAllFields() {
        Activity activity = new Activity();
        activity.setTitle("Skiing");
        activity.setDescription("Snow skiing");

        ActivityDTO dto = mapper.toDto(activity);

        assertThat(dto).isNotNull();
        assertThat(dto.getTitle()).isEqualTo("Skiing");
        assertThat(dto.getDescription()).isEqualTo("Snow skiing");
    }

    @Test
    void updateEntityFromDto_shouldUpdateEntityFields() {
        ActivityCreateDTO dto = new ActivityCreateDTO();
        dto.setTitle("Swimming");
        dto.setDescription("Pool swimming");

        Activity activity = new Activity();
        activity.setTitle("OldName");
        activity.setDescription("OldDescription");

        mapper.updateEntityFromDto(dto, activity);

        assertThat(activity.getTitle()).isEqualTo("Swimming");
        assertThat(activity.getDescription()).isEqualTo("Pool swimming");
    }
}