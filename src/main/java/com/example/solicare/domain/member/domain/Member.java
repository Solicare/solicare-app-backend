package com.example.solicare.domain.member.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "password") // 비밀번호 로그 노출 방지
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String elderlyName;

    private String password;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender = Gender.OTHER;           // ★ 추가

    @Column(nullable = false, length = 255)
    private String address;          // ★ 추가

    @Column(nullable = false)
    private Integer age;             // ★ 추가

    @Column(name = "special_note", length = 500)
    private String specialNote;      // ★ 추가 (nullable 허용)

    private int guardianHeartRate;   // 심박수 (bpm)
    private double guardianTemperature;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;
}
