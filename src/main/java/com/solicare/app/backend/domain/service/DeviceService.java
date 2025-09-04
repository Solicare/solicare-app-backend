package com.solicare.app.backend.domain.service;

import com.solicare.app.backend.application.dto.res.DeviceResponseDTO;
import com.solicare.app.backend.application.mapper.DeviceMapper;
import com.solicare.app.backend.domain.dto.device.DeviceManageResult;
import com.solicare.app.backend.domain.dto.device.DeviceQueryResult;
import com.solicare.app.backend.domain.entity.Device;
import com.solicare.app.backend.domain.entity.Member;
import com.solicare.app.backend.domain.entity.Senior;
import com.solicare.app.backend.domain.enums.Push;
import com.solicare.app.backend.domain.enums.Role;
import com.solicare.app.backend.domain.repository.DeviceRepository;
import com.solicare.app.backend.domain.repository.MemberRepository;
import com.solicare.app.backend.domain.repository.SeniorRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceMapper deviceMapper;
    private final DeviceRepository deviceRepository;
    private final MemberRepository memberRepository;
    private final SeniorRepository seniorRepository;

    public boolean isDeviceOwner(Role role, String uuid, Push pushType, String deviceToken) {
        return getDevices(role, uuid).getResponse().stream()
                .anyMatch(
                        device -> device.type() == pushType && device.token().equals(deviceToken));
    }

    public DeviceQueryResult getDevices(Role role, String uuid) {
        switch (role) {
            case MEMBER -> {
                return DeviceQueryResult.of(
                        DeviceQueryResult.Status.SUCCESS,
                        deviceRepository.findByMember_Uuid(uuid).stream()
                                .map(deviceMapper::from)
                                .collect(Collectors.toList()),
                        null);
            }
            case SENIOR -> {
                return DeviceQueryResult.of(
                        DeviceQueryResult.Status.SUCCESS,
                        deviceRepository.findBySenior_Uuid(uuid).stream()
                                .map(deviceMapper::from)
                                .collect(Collectors.toList()),
                        null);
            }
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    public DeviceManageResult create(Push type, String token) {
        try {
            Device device = deviceRepository.findByTypeAndToken(type, token).orElse(null);
            if (device != null) {
                if (!device.isEnabled()) {
                    deviceRepository.save(device.setEnabled(true));
                    return DeviceManageResult.of(
                            DeviceManageResult.Status.ENABLED, deviceMapper.from(device), null);
                }
                return DeviceManageResult.of(
                        DeviceManageResult.Status.ALREADY_EXISTS, deviceMapper.from(device), null);
            }

            DeviceResponseDTO.Info info =
                    deviceMapper.from(
                            deviceRepository.save(
                                    Device.builder()
                                            .type(type)
                                            .token(token)
                                            .enabled(true)
                                            .build()));
            return DeviceManageResult.of(DeviceManageResult.Status.CREATED, info, null);
        } catch (Exception e) {
            return DeviceManageResult.of(DeviceManageResult.Status.ERROR, null, e);
        }
    }

    public DeviceManageResult delete(Push type, String token) {
        try {
            if (!deviceRepository.existsByTypeAndToken(type, token)) {
                return DeviceManageResult.of(
                        DeviceManageResult.Status.DEVICE_NOT_FOUND, null, null);
            }
            deviceRepository.deleteByTypeAndToken(type, token);
            return DeviceManageResult.of(DeviceManageResult.Status.DELETED, null, null);
        } catch (Exception e) {
            return DeviceManageResult.of(DeviceManageResult.Status.ERROR, null, e);
        }
    }

    public DeviceManageResult link(String deviceUuid, Role role, String uuid) {
        try {
            Device device =
                    deviceRepository
                            .findByUuid(deviceUuid)
                            .orElseThrow(() -> new IllegalArgumentException("DEVICE_NOT_FOUND"));
            switch (role) {
                case MEMBER -> {
                    Member member =
                            memberRepository
                                    .findByUuid(uuid)
                                    .orElseThrow(
                                            () -> new IllegalArgumentException("MEMBER_NOT_FOUND"));
                    device.link(member);
                }
                case SENIOR -> {
                    Senior senior =
                            seniorRepository
                                    .findByUuid(uuid)
                                    .orElseThrow(
                                            () -> new IllegalArgumentException("SENIOR_NOT_FOUND"));
                    device.link(senior);
                }
                default -> throw new IllegalArgumentException("Invalid role: " + role);
            }
            return DeviceManageResult.of(
                    DeviceManageResult.Status.LINKED,
                    deviceMapper.from(deviceRepository.save(device).setEnabled(true)),
                    null);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                return DeviceManageResult.of(
                        DeviceManageResult.Status.valueOf(e.getMessage()), null, e);
            }
            return DeviceManageResult.of(DeviceManageResult.Status.ERROR, null, e);
        }
    }

    public DeviceManageResult unlink(String deviceUuid) {
        try {
            Device device =
                    deviceRepository
                            .findByUuid(deviceUuid)
                            .orElseThrow(() -> new IllegalArgumentException("DEVICE_NOT_FOUND"));
            deviceRepository.save(device.unlink());
            return DeviceManageResult.of(
                    DeviceManageResult.Status.UNLINKED, deviceMapper.from(device), null);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                return DeviceManageResult.of(
                        DeviceManageResult.Status.valueOf(e.getMessage()), null, e);
            }
            return DeviceManageResult.of(DeviceManageResult.Status.ERROR, null, e);
        }
    }

    // TODO: handle specific exceptions (e.g. EntityNotFound -> ApiException with 404 status)
    //  handled by controller advice(ExceptionHandler)
}
