package com.example.solicare.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "fcm_device_token")
public class DeviceToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member")
    private Long memberId;              // 로그인 사용자 식별자(없으면 null)

    @Column(nullable = false, length = 2048)
    private String token;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    private LocalDateTime lastSeenAt;

    // ====== 도메인 메서드 ======
    public void touch() {
        this.lastSeenAt = LocalDateTime.now();
    }

    public void update(String newToken) {
        this.token = newToken;
        touch();
    }

    public void unregister() {
        this.token = null;
        touch();
    }
}
