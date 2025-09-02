package com.solicare.app.backend.domain.service;

import com.solicare.app.backend.application.dto.request.MemberRequestDTO;
import com.solicare.app.backend.application.dto.request.SeniorRequestDTO;
import com.solicare.app.backend.application.dto.res.MemberResponseDTO;
import com.solicare.app.backend.application.dto.res.SeniorResponseDTO;
import com.solicare.app.backend.application.mapper.MemberMapper;
import com.solicare.app.backend.application.mapper.SeniorMapper;
import com.solicare.app.backend.domain.dto.output.care.CareLinkOutput;
import com.solicare.app.backend.domain.dto.output.care.CareQueryOutput;
import com.solicare.app.backend.domain.entity.Care;
import com.solicare.app.backend.domain.entity.Member;
import com.solicare.app.backend.domain.entity.Senior;
import com.solicare.app.backend.domain.repository.CareRelationRepository;
import com.solicare.app.backend.domain.repository.MemberRepository;
import com.solicare.app.backend.domain.repository.SeniorRepository;

import jakarta.transaction.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CareService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final SeniorRepository seniorRepository;
    private final CareRelationRepository careRelationRepository;
    private final MemberMapper memberMapper;
    private final SeniorMapper seniorMapper;

    public CareQueryOutput<SeniorResponseDTO.Profile> querySeniorByMember(String memberUuid) {
        try {
            Member member =
                    memberRepository
                            .findByUuid(memberUuid)
                            .orElseThrow(() -> new IllegalArgumentException("MEMBER_NOT_FOUND"));
            List<Senior> seniors =
                    careRelationRepository.findByMember(member).stream()
                            .map(Care::getSenior)
                            .toList();
            return CareQueryOutput.of(
                    CareQueryOutput.Status.SUCCESS,
                    seniors.stream().map(seniorMapper::toProfileDTO).toList(),
                    null);
        } catch (Exception e) {
            return CareQueryOutput.of(CareQueryOutput.Status.ERROR, null, e);
        }
    }

    public CareQueryOutput<MemberResponseDTO.Profile> queryMemberBySenior(String seniorUuid) {
        try {
            Senior senior =
                    seniorRepository
                            .findByUuid(seniorUuid)
                            .orElseThrow(() -> new IllegalArgumentException("SENIOR_NOT_FOUND"));
            List<Member> members =
                    careRelationRepository.findBySenior(senior).stream()
                            .map(Care::getMember)
                            .toList();
            return CareQueryOutput.of(
                    CareQueryOutput.Status.SUCCESS,
                    members.stream().map(memberMapper::toProfileDTO).toList(),
                    null);
        } catch (Exception e) {
            return CareQueryOutput.of(CareQueryOutput.Status.ERROR, null, e);
        }
    }

    public CareLinkOutput<MemberResponseDTO.Profile> linkSeniorToMember(
            String memberUuid, MemberRequestDTO.LinkSenior linkDto) {
        try {
            Member member =
                    memberRepository
                            .findByUuid(memberUuid)
                            .orElseThrow(() -> new IllegalArgumentException("MEMBER_NOT_FOUND"));
            Senior senior =
                    seniorRepository
                            .findByUserId(linkDto.userId())
                            .orElseThrow(() -> new IllegalArgumentException("SENIOR_NOT_FOUND"));

            if (careRelationRepository.existsByMemberAndSenior(member, senior)) {
                return CareLinkOutput.of(CareLinkOutput.Status.ALREADY_LINKED, null, null);
            }

            if (!passwordEncoder.matches(linkDto.password(), senior.getPassword())) {
                return CareLinkOutput.of(CareLinkOutput.Status.INVALID_SENIOR_PASSWORD, null, null);
            }
            careRelationRepository.save(Care.builder().member(member).senior(senior).build());
            return CareLinkOutput.of(
                    CareLinkOutput.Status.SUCCESS, memberMapper.toProfileDTO(member), null);
        } catch (Exception e) {
            return CareLinkOutput.of(CareLinkOutput.Status.ERROR, null, e);
        }
    }

    public CareLinkOutput<SeniorResponseDTO.Profile> linkMemberToSenior(
            String seniorUuid, SeniorRequestDTO.LinkMember linkDto) {
        try {
            Member member =
                    memberRepository
                            .findByEmail(linkDto.email())
                            .orElseThrow(() -> new IllegalArgumentException("MEMBER_NOT_FOUND"));
            Senior senior =
                    seniorRepository
                            .findByUuid(seniorUuid)
                            .orElseThrow(() -> new IllegalArgumentException("SENIOR_NOT_FOUND"));

            if (careRelationRepository.existsByMemberAndSenior(member, senior)) {
                return CareLinkOutput.of(CareLinkOutput.Status.ALREADY_LINKED, null, null);
            }

            if (!passwordEncoder.matches(linkDto.password(), member.getPassword())) {
                return CareLinkOutput.of(CareLinkOutput.Status.INVALID_MEMBER_PASSWORD, null, null);
            }

            careRelationRepository.save(Care.builder().member(member).senior(senior).build());
            return CareLinkOutput.of(
                    CareLinkOutput.Status.SUCCESS, seniorMapper.toProfileDTO(senior), null);
        } catch (Exception e) {
            return CareLinkOutput.of(CareLinkOutput.Status.ERROR, null, e);
        }
    }
    // TODO: refactor duplicate code above, use private methods to reduce redundancy
    // TODO: handle specific exceptions (e.g. EntityNotFound -> ApiException with 404 status)
    //  handled by controller advice(ExceptionHandler)
}
