package com.example.travel.planner.constant;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "jwt")
@Component
@Getter
@Setter
public class JWTConstants {
    private String secretKey;
    private String secretDefaultValue;
    private String header;
    private String bearer;
}