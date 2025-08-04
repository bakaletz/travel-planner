package com.example.travel.planner.service.impl;

import com.example.travel.planner.constant.JWTConstants;
import com.example.travel.planner.dto.authentication.LoginRequestDTO;
import com.example.travel.planner.dto.authentication.LoginResponseDTO;
import com.example.travel.planner.dto.user.UserDTO;
import com.example.travel.planner.dto.user.UserRegisterDTO;
import com.example.travel.planner.entity.User;
import com.example.travel.planner.exception.ResourceAlreadyExistsException;
import com.example.travel.planner.mapper.UserMapper;
import com.example.travel.planner.repository.UserRepository;
import com.example.travel.planner.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JWTConstants jwtConstants;
    private final Environment env;

    @Override
    public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new Exception("Invalid Email"));

            String jwt = generateJwtToken(user);

            return new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(), jwtConstants.getBearer() + jwt);

        } catch (Exception e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @Override
    public UserDTO registerUser(UserRegisterDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            log.warn("Attempt to register user with existing email: {}", userDTO.getEmail());
            throw new ResourceAlreadyExistsException("User", "email", userDTO.getEmail());
        }

        User user = userMapper.toEntity(userDTO, passwordEncoder);
        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    private String generateJwtToken(User user) {
        String secret = env.getProperty(jwtConstants.getSecretKey(), jwtConstants.getSecretDefaultValue());
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        long jwtExpirationMillis = 86400000;

        Instant now = Instant.now();
        Instant expiration = now.plus(Duration.ofMillis(jwtExpirationMillis));

        return Jwts.builder()
                .issuer("Bakalets")
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }
}