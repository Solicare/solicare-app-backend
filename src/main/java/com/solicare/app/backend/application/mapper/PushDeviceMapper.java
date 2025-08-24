package com.solicare.app.backend.application.mapper;

import com.solicare.app.backend.domain.dto.PushDeviceDTO;
import com.solicare.app.backend.domain.entity.PushDevice;
import org.springframework.stereotype.Component;

@Component
public class PushDeviceMapper {
  public PushDeviceDTO from(PushDevice pushDevice) {
    return new PushDeviceDTO(pushDevice.getEnabled(), pushDevice.getType(), pushDevice.getToken());
  }
}
