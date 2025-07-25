package com.example.travel.planner.service.impl;

import com.example.travel.planner.dto.user.UserDTO;
import com.example.travel.planner.dto.user.UserRegisterDTO;
import com.example.travel.planner.dto.user.UserUpdateDTO;
import com.example.travel.planner.entity.User;
import com.example.travel.planner.exception.ResourceAlreadyExistsException;
import com.example.travel.planner.exception.ResourceNotFoundException;
import com.example.travel.planner.mapper.UserMapper;
import com.example.travel.planner.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


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

    @BeforeEach
    void setUp(){
        testUser = new User();
        testUser.setId("test-id-123");
        testUser.setEmail("test@example.com");
        testUser.setFirstName("John");

        testUserDTO = new UserDTO();
        testUserDTO.setId("test-id-123");
        testUserDTO.setEmail("test@example.com");
        testUserDTO.setFirstName("John");

        testUserRegisterDTO = new UserRegisterDTO();
        testUserRegisterDTO.setEmail("new@example.com");
        testUserRegisterDTO.setFirstName("Jane");
        testUserRegisterDTO.setPassword("password123");

        testUserUpdateDTO = new UserUpdateDTO();
        testUserUpdateDTO.setFirstName("Updated");
    }

    @Test
    void findAll_shouldReturnListOfUsers(){
        User user1 = new User();
        user1.setId("1");
        User user2 = new User();
        user2.setId("2");
        List<User> users = List.of(user1, user2);

        UserDTO dto1 = new UserDTO();
        dto1.setId("1");
        UserDTO dto2 = new UserDTO();
        dto2.setId("2");

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(user1)).thenReturn(dto1);
        when(userMapper.toDto(user2)).thenReturn(dto2);

        List<UserDTO> result = userService.findAll();

        assertThat(result).extracting(UserDTO::getId).containsExactly("1", "2");
        verify(userRepository).findAll();
        verify(userMapper, times(2)).toDto(any(User.class));


    }

    @Test
    void findById_shouldReturnUserDto() {
        User user = new User();
        String id = "123";
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(new UserDTO());

        UserDTO result = userService.findById(id);

        assertNotNull(result);

        verify(userRepository).findById(id);
        verify(userMapper).toDto(user);
    }

    @Test
    void findById_shouldThrowResourceNotFoundException(){
        when(userRepository.findById("Test")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findById("Test"));

        verify(userRepository).findById("Test");
        verifyNoInteractions(userMapper);
    }

    @Test
    void createUser_shouldSaveNewUser(){

        User newUser = new User();
        newUser.setEmail("new@example.com");

        when(userRepository.existsByEmail(testUserRegisterDTO.getEmail())).thenReturn(false);
        when(userMapper.toEntity(testUserRegisterDTO)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(testUser);
        when(userMapper.toDto(testUser)).thenReturn(testUserDTO);

        UserDTO result = userService.createUser(testUserRegisterDTO);

        assertNotNull(result);
        assertEquals(result.getEmail(),testUserDTO.getEmail());

        verify(userRepository).existsByEmail(testUserRegisterDTO.getEmail());
        verify(userMapper).toEntity(testUserRegisterDTO);
        verify(userRepository).save(newUser);
        verify(userMapper).toDto(testUser);

    }

    @Test
    void createUser_shouldThrowEmailExistsException_whenEmailExists(){

        when(userRepository.existsByEmail(testUserRegisterDTO.getEmail())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> userService.createUser(testUserRegisterDTO));

        verify(userRepository).existsByEmail(testUserRegisterDTO.getEmail());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);

    }

    @Test
    void updateUser_shouldUpdateAndReturnDto_WhenUserExists(){
        String userId = "test-id-123";
        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setFirstName("Updated");

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        doNothing().when(userMapper).updateEntityFromDto(testUserUpdateDTO, testUser);
        when(userRepository.save(testUser)).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(testUserDTO);

        UserDTO result = userService.updateUser(userId, testUserUpdateDTO);

        assertNotNull((result));
        assertEquals(userId, result.getId());

        verify(userRepository).findById(userId);
        verify(userMapper).updateEntityFromDto(testUserUpdateDTO, testUser);
        verify(userRepository).save(testUser);
        verify(userMapper).toDto(updatedUser);

    }

    @Test
    void updateUser_shouldThrowIfNotFound(){

        String userId = "non-existent-id";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userId, testUserUpdateDTO));

        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);
    }

    @Test
    void deleteUser_shouldDeleteAndReturnMessage(){

        String userId = "123";

        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        String result = userService.deleteUser(userId);

        assertThat(result).contains(userId);

        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);

    }

    @Test
    void deleteUser_shouldThrowIfNotExists() {

        String userId = "404";

        when(userRepository.existsById("404")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(userId));

        verify(userRepository).existsById(userId);
        verify(userRepository, never()).deleteById(any());
    }
}



