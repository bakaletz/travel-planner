package com.example.travel.planner.service.impl;

import com.example.travel.planner.dto.user.UserDTO;
import com.example.travel.planner.dto.user.UserRegisterDTO;
import com.example.travel.planner.dto.user.UserUpdateDTO;
import com.example.travel.planner.entity.User;
import com.example.travel.planner.exception.ResourceAlreadyExistsException;
import com.example.travel.planner.exception.ResourceNotFoundException;
import com.example.travel.planner.mapper.UserMapper;
import com.example.travel.planner.repository.UserRepository;
import com.example.travel.planner.testdata.TestUserProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    UserServiceImpl userService;

    private User testUser;
    private UserDTO testUserDTO;
    private UserRegisterDTO testUserRegisterDTO;
    private UserUpdateDTO testUserUpdateDTO;
    private final String userId = TestUserProvider.USER_ID;

    @BeforeEach
    void setUp() {
        testUser = TestUserProvider.createDefaultUser();
        testUserDTO = TestUserProvider.createUserDTO();
        testUserRegisterDTO = TestUserProvider.createUserRegisterDTO();
        testUserUpdateDTO = TestUserProvider.createUserUpdateDTO();
    }

    @Test
    void findAllUsers_shouldReturnPageOfUsers() {
        User user1 = User.builder()
                .id("1")
                .build();
        User user2 = User.builder()
                .id("2")
                .build();
        List<User> users = List.of(user1, user2);

        Page<User> userPage = new PageImpl<>(users);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        userService.findAllUsers(0, 10);

        verify(userMapper, times(2)).toDto(any());
    }

    @Test
    void findById_shouldReturnUserDto() {

        String userId = testUser.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userMapper.toDto(testUser)).thenReturn(testUserDTO);

        userService.findById(userId);

        verify(userMapper).toDto(any());
    }

    @Test
    void findById_shouldThrowResourceNotFoundException() {

        when(userRepository.findById("Test")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findById("Test"));

        verifyNoInteractions(userMapper);
    }

    @Test
    void createUser_shouldSaveNewUser() {
        when(userRepository.existsByEmail(testUserRegisterDTO.getEmail())).thenReturn(false);

        userService.createUser(testUserRegisterDTO);

        verify(userMapper).toEntity(eq(testUserRegisterDTO), any());
        verify(userRepository).save(any());
        verify(userMapper).toDto(any());

    }

    @Test
    void createUser_shouldThrowEmailExistsException_whenEmailExists() {
        when(userRepository.existsByEmail(testUserRegisterDTO.getEmail())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> userService.createUser(testUserRegisterDTO));

        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);

    }

    @Test
    void updateUser_shouldUpdateAndReturnDto_WhenUserExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        userService.updateUser(userId, testUserUpdateDTO);

        verify(userMapper).updateEntityFromDto(eq(testUserUpdateDTO), any());
        verify(userRepository).save(testUser);
        verify(userMapper).toDto(any());
    }


    @Test
    void updateUser_shouldThrowIfNotFound() {

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userId, testUserUpdateDTO));

        verifyNoInteractions(userMapper);
    }

    @Test
    void deleteUser_shouldDeleteAndReturnMessage() {
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void deleteUser_shouldThrowIfNotExists() {
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(userId));

        verify(userRepository, never()).deleteById(any());
    }
}



