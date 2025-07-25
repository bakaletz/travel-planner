package com.example.travel.planner.mapper;

import com.example.travel.planner.dto.user.UserDTO;
import com.example.travel.planner.dto.user.UserRegisterDTO;
import com.example.travel.planner.dto.user.UserUpdateDTO;
import com.example.travel.planner.entity.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toDto_shouldMapFields() {
        User user = new User();
        user.setId("user1");
        user.setEmail("email@example.com");
        user.setFirstName("John");

        UserDTO dto = mapper.toDto(user);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo("user1");
        assertThat(dto.getEmail()).isEqualTo("email@example.com");
        assertThat(dto.getFirstName()).isEqualTo("John");
    }

    @Test
    void toEntity_shouldMapFields() {
        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setEmail("newuser@example.com");
        registerDTO.setFirstName("New");
        registerDTO.setPassword("password123");

        User entity = mapper.toEntity(registerDTO);

        assertThat(entity).isNotNull();
        assertThat(entity.getEmail()).isEqualTo("newuser@example.com");
        assertThat(entity.getFirstName()).isEqualTo("New");
        assertThat(entity.getPassword()).isEqualTo("password123");
    }

    @Test
    void updateEntityFromDto_shouldUpdateFields() {
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setFirstName("Updated");
        updateDTO.setEmail("updated@example.com");

        User user = new User();
        user.setFirstName("Old");
        user.setEmail("old@example.com");

        mapper.updateEntityFromDto(updateDTO, user);

        assertThat(user.getFirstName()).isEqualTo("Updated");
        assertThat(user.getEmail()).isEqualTo("updated@example.com");
    }
}
