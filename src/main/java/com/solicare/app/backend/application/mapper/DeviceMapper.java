package com.solicare.app.backend.application.mapper;

import com.solicare.app.backend.application.dto.res.DeviceResponseDTO;
import com.solicare.app.backend.domain.entity.Device;

import org.springframework.stereotype.Service;

@Service
public class DeviceMapper {
    public DeviceResponseDTO.Info from(Device device) {
        return new DeviceResponseDTO.Info(
                device.getUuid(), device.isEnabled(), device.getType(), device.getToken());
    }
}
