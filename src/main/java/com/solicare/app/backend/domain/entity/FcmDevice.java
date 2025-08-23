package com.solicare.app.backend.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FcmDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column(nullable = false, length = 2048)
    private String token;

    @Column(nullable = false, length = 20)
    private String localNumber;

    @Column
    private LocalDateTime lastSeenAt;

    @OneToOne(mappedBy = "fcmDevice")
    private Member member;

    public FcmDevice update(String newToken, String localNumber) {
        this.token = newToken;
        this.localNumber = localNumber;
        touch();
        return this;
    }

    // ====== 도메인 메서드 ======
    public void touch() {
        this.lastSeenAt = LocalDateTime.now();
    }

    public void unregister() {
        this.token = null;
        touch();
    }
}
