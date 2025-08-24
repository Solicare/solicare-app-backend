package com.example.solicare.aichat.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatMessageDto(
        @NotBlank String role,   // "user" | "assistant" | "system"
        @NotBlank String content
) {
}
