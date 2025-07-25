package com.example.travel.planner.service.impl;

import com.example.travel.planner.dto.user.UserDTO;
import com.example.travel.planner.dto.user.UserRegisterDTO;
import com.example.travel.planner.dto.user.UserUpdateDTO;
import com.example.travel.planner.entity.User;
import com.example.travel.planner.exception.ResourceAlreadyExistsException;
import com.example.travel.planner.exception.ResourceNotFoundException;
import com.example.travel.planner.mapper.UserMapper;
import com.example.travel.planner.repository.UserRepository;
import com.example.travel.planner.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDTO> findAll() {

        List<User> users = userRepository.findAll();

        return users.stream().map(userMapper::toDto).toList();
    }

    @Override
    public UserDTO findById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id));
        return userMapper.toDto(user);
    }

    @Override
    public UserDTO createUser(UserRegisterDTO userRegisterDTO) {

        if (userRepository.existsByEmail(userRegisterDTO.getEmail())){
            throw new ResourceAlreadyExistsException("User","email",userRegisterDTO.getEmail());
        }

        User savedUser = userRepository.save(userMapper.toEntity(userRegisterDTO));

        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDTO updateUser(String id, UserUpdateDTO userUpdateDTO) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        userMapper.updateEntityFromDto(userUpdateDTO, user);

        User updated = userRepository.save(user);

        return userMapper.toDto(updated);
    }

    @Override
    public String deleteUser(String id) {

        if (!userRepository.existsById(id)){
            throw new ResourceNotFoundException("User", id);
        }
        userRepository.deleteById(id);

        return "User with id " + id + " was deleted";
    }


}
