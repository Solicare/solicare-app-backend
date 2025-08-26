package com.solicare.app.backend.domain.dto;

import com.solicare.app.backend.domain.enums.PushDeviceType;

public record PushDeviceDTO(boolean enabled, PushDeviceType type, String token) {}
