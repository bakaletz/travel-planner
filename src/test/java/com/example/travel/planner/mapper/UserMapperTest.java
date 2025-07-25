package com.example.travel.planner.mapper;

import com.example.travel.planner.dto.user.UserDTO;
import com.example.travel.planner.dto.user.UserRegisterDTO;
import com.example.travel.planner.dto.user.UserUpdateDTO;
import com.example.travel.planner.entity.User;
import com.example.travel.planner.testdata.TestUserProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toDto_shouldMapFields() {
        User user = TestUserProvider.createDefaultUser();

        UserDTO dto = mapper.toDto(user);

        assertEquals(user.getId(), dto.getId());
    }

    @Test
    void toEntity_shouldMapFields() {
        UserRegisterDTO registerDTO = TestUserProvider.createUserRegisterDTO();

        when(passwordEncoder.encode(any())).thenReturn("encoded-password");

        User entity = mapper.toEntity(registerDTO, passwordEncoder);

        assertEquals(registerDTO.getFirstName(), entity.getFirstName());
    }

    @Test
    void updateEntityFromDto_shouldUpdateFields() {
        UserUpdateDTO updateDTO = TestUserProvider.createUserUpdateDTO();
        User user = TestUserProvider.createDefaultUser();

        mapper.updateEntityFromDto(updateDTO, user);

        assertEquals(updateDTO.getFirstName(), user.getFirstName());
    }
}
