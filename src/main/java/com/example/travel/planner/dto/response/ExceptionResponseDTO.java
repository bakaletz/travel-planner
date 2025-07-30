package com.example.travel.planner.dto.response;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ExceptionResponseDTO(int statusCode, HttpStatus error, String message, LocalDateTime timeStamp) {
}
