package com.stupor.auth.dto.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDto {
    private String message;
    private String reason;
    private String timestamp;
    private String status;
}
