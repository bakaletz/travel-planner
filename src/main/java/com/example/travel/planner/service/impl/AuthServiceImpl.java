package com.example.travel.planner.service.impl;

import com.example.travel.planner.constant.JWTConstants;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final Environment env;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JWTConstants jwtConstants;
    private static final long JWT_EXPIRATION_MILLIS = 30_000_000L;

    @Override
    public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        String jwt = "";
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getEmail(),
                loginRequest.getPassword());

        Authentication authenticationResponse = authenticationManager.authenticate(authentication);

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", loginRequest.getEmail()));

        if (null != authenticationResponse && authenticationResponse.isAuthenticated() && null != env) {
            String secret = env.getProperty(jwtConstants.getSecretKey(),
                    jwtConstants.getSecretDefaultValue());
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

            Date issuedAt = new Date();
            Date expiration = new Date(issuedAt.getTime() + JWT_EXPIRATION_MILLIS);

            jwt = Jwts.builder().issuer("Bakaletz").subject("JWT Token")
                    .claim("userId", user.getId())
                    .issuedAt(issuedAt)
                    .expiration(expiration)
                    .signWith(secretKey).compact();
        }
        jwt = jwtConstants.getBearer() + jwt;

        return new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(), jwt);
    }

    @Override
    public UserDTO registerUser(UserRegisterDTO userDTO) {

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ResourceAlreadyExistsException("User", "email", userDTO.getEmail());
        }

        User user = userMapper.toEntity(userDTO, passwordEncoder);

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}
