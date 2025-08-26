package com.solicare.app.backend.application.mapper;

import com.solicare.app.backend.domain.dto.PushDeviceDTO;
import com.solicare.app.backend.domain.entity.PushDevice;

import org.springframework.stereotype.Service;

@Service
public class PushDeviceMapper {
    public PushDeviceDTO from(PushDevice pushDevice) {
        return new PushDeviceDTO(
                pushDevice.isEnabled(), pushDevice.getType(), pushDevice.getToken());
    }
}
