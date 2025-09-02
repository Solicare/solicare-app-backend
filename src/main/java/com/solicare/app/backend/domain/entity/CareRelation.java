package com.solicare.app.backend.domain.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CareRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_uuid", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_uuid", nullable = false)
    private Senior senior;

    @Column(nullable = false)
    private LocalDate careStartDate;

    @PrePersist
    protected void onCreate() {
        this.careStartDate = LocalDate.now();
    }
}
