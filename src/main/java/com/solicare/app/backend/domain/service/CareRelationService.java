package com.solicare.app.backend.domain.service;

import com.solicare.app.backend.application.dto.request.SeniorRequestDTO;
import com.solicare.app.backend.application.dto.res.CareRelationResponseDTO;
import com.solicare.app.backend.domain.dto.output.care.CareLinkOutput;
import com.solicare.app.backend.domain.entity.CareRelation;
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

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CareRelationService {

    private final MemberRepository memberRepository;
    private final SeniorRepository seniorRepository;
    private final CareRelationRepository careRelationRepository;
    private final PasswordEncoder passwordEncoder;

    public CareLinkOutput linkSeniorToMember(SeniorRequestDTO.Link linkDto) {
        // 1. 멤버 조회
        Member member = memberRepository.findByUuid(linkDto.monitorUserUuid()).orElse(null);
        if (member == null) {
            return CareLinkOutput.of(CareLinkOutput.Status.MEMBER_NOT_FOUND, null, null);
        }

        // 2. 시니어 조회
        Senior senior = seniorRepository.findByUserId(linkDto.userId()).orElse(null);
        if (senior == null) {
            return CareLinkOutput.of(CareLinkOutput.Status.SENIOR_NOT_FOUND, null, null);
        }

        // 3. 시니어 비밀번호 검증
        if (!passwordEncoder.matches(linkDto.password(), senior.getPassword())) {
            return CareLinkOutput.of(CareLinkOutput.Status.INVALID_SENIOR_PASSWORD, null, null);
        }

        // 4. 이미 존재하는 관계인지 확인
        if (careRelationRepository.existsByMemberAndSenior(member, senior)) {
            return CareLinkOutput.of(CareLinkOutput.Status.RELATION_ALREADY_EXISTS, null, null);
        }

        // 5. CareRelation 엔티티 생성 및 저장
        CareRelation newRelation = CareRelation.builder()
                .member(member)
                .senior(senior)
                .build();
        careRelationRepository.save(newRelation);

        // 6. 성공 응답 생성
        CareRelationResponseDTO.Link response = new CareRelationResponseDTO.Link(
                member.getName(),
                senior.getName(),
                newRelation.getUuid()
        );

        return CareLinkOutput.of(CareLinkOutput.Status.SUCCESS, response, null);
    }
}