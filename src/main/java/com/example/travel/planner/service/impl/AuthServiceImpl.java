package com.example.travel.planner.service.impl;

import com.example.travel.planner.constant.ApplicationConstants;
import com.example.travel.planner.dto.authentication.LoginRequestDTO;
import com.example.travel.planner.dto.authentication.LoginResponseDTO;
import com.example.travel.planner.dto.user.UserDTO;
import com.example.travel.planner.dto.user.UserRegisterDTO;
import com.example.travel.planner.entity.User;
import com.example.travel.planner.exception.ResourceAlreadyExistsException;
import com.example.travel.planner.exception.ResourceNotFoundException;
import com.example.travel.planner.mapper.UserMapper;
import com.example.travel.planner.repository.UserRepository;
import com.example.travel.planner.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final Environment env;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public LoginResponseDTO authenticateAndGenerateToken(LoginRequestDTO loginRequest) {

        String jwt = "";
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getEmail(),
                loginRequest.getPassword());

        Authentication authenticationResponse = authenticationManager.authenticate(authentication);

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User","email", loginRequest.getEmail()));

        if(null != authenticationResponse && authenticationResponse.isAuthenticated()) {
            if (null != env) {
                String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                        ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                jwt = Jwts.builder().issuer("Bakaletz").subject("JWT Token")
                        .claim("userId", user.getId())
                        .issuedAt(new java.util.Date())
                        .expiration(new java.util.Date((new java.util.Date()).getTime() + 30000000))
                        .signWith(secretKey).compact();
            }
        }
        jwt = "Bearer " + jwt;

        return new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(), jwt);
    }

    @Override
    public UserDTO registerUser(UserRegisterDTO userDTO) {

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ResourceAlreadyExistsException("User", "email", userDTO.getEmail());
        }

        User user = userMapper.toEntity(userDTO);

        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}
