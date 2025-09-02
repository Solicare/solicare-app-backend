package com.solicare.app.backend.domain.repository;

import com.solicare.app.backend.domain.entity.CareRelation;
import com.solicare.app.backend.domain.entity.Member;
import com.solicare.app.backend.domain.entity.Senior;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CareRelationRepository extends JpaRepository<CareRelation, String> {
    List<CareRelation> findByMember(Member member);

    List<CareRelation> findBySenior(Senior senior);

    boolean existsByMemberAndSenior(Member member, Senior senior);
}
