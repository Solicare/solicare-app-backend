package com.example.solicare.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(staticName = "of")
@Table(name = "member")
@ToString(exclude = "password") // 비밀번호 로그 노출 방지
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;
    @NonNull
    @Column(nullable = false)
    private String name;
    @NonNull
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    @NonNull
    private String password;
    @NonNull
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    @OneToOne(fetch = FetchType.LAZY) // getFcmDevice() 시에만 조회
    @JoinColumn(referencedColumnName = "uuid", unique = true)
    private FcmDevice fcmDevice;

    // TODO: add column 'seniors' (List of UUIDs) for seniors under care
}
