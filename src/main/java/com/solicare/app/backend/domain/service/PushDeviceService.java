package com.solicare.app.backend.domain.service;

import com.solicare.app.backend.application.mapper.PushDeviceMapper;
import com.solicare.app.backend.domain.dto.PushDeviceDTO;
import com.solicare.app.backend.domain.entity.Member;
import com.solicare.app.backend.domain.entity.PushDevice;
import com.solicare.app.backend.domain.enums.PushDeviceType;
import com.solicare.app.backend.domain.repository.PushDeviceRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PushDeviceService {
  private final PushDeviceMapper pushDeviceMapper;
  private final PushDeviceRepository repository;

  public List<PushDeviceDTO> getPushDevicesByMember(Member member) {
    List<PushDeviceDTO> dtoList = new ArrayList<>();
    List<PushDevice> pushDevices = repository.findByMember(member);
    pushDevices.stream().map(pushDeviceMapper::from).forEach(dtoList::add);
    return dtoList;
  }

  public PushDevice create(PushDeviceType type, String token) {
    return repository.save(PushDevice.builder().type(type).token(token).enabled(true).build());
  }

  public boolean linkMember(String pushDeviceUuid, Member member) {
    return repository.findById(pushDeviceUuid).map(device -> device.link(member)).isPresent();
  }

  public boolean unlinkMember(String pushDeviceUuid) {
    return repository.findById(pushDeviceUuid).map(PushDevice::unlink).isPresent();
  }

  public boolean delete(String pushDeviceUuid) {
    return repository
        .findById(pushDeviceUuid)
        .map(
            device -> {
              repository.delete(device);
              return true;
            })
        .isPresent();
  }
}
