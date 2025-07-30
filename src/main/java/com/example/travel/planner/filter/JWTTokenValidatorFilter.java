package com.example.travel.planner.filter;

import com.example.travel.planner.constant.JWTConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    private final JWTConstants jwtConstants;

    private static final int BEARER_PREFIX_LENGTH = 7;

    public JWTTokenValidatorFilter(JWTConstants jwtConstants) {
        this.jwtConstants = jwtConstants;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(jwtConstants.getHeader());

        if (authHeader != null && authHeader.startsWith(jwtConstants.getBearer())) {
            String jwt = authHeader.substring(BEARER_PREFIX_LENGTH);

            try {
                Environment env = getEnvironment();
                String secret = env.getProperty(jwtConstants.getSecretKey(),
                        jwtConstants.getSecretDefaultValue());
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

                Claims claims = Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload();

                String userId = String.valueOf(claims.get("userId"));

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, null);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                throw new BadCredentialsException("Invalid Token received!", e);
            }
        }

        filterChain.doFilter(request, response);
    }
}
