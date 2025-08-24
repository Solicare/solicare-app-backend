package com.solicare.app.backend.domain.entity;

import com.solicare.app.backend.domain.enums.PushDeviceType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PushDevice {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String uuid;

  @Column private Boolean enabled = true;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PushDeviceType type;

  @Column(nullable = false, length = 2048)
  private String token;

  @Column private LocalDateTime lastSeenAt;

  //  @Column(length = 20)
  //  private String deviceId; // 기기 고유번호
  //
  //  @Column(length = 20)
  //  private String deviceType; // ANDROID, IOS 등
  //
  //  @Column(length = 50)
  //  private String deviceModel; // 기기 모델명
  //
  //  @Column(length = 20)
  //  private String appVersion; // 앱 버전
  //
  //  @Column(length = 20)
  //  private String osVersion; // OS 버전

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_uuid")
  private Member member;

  public PushDevice link(Member member) {
    this.member = member;
    this.touch();
    return this;
  }

  private void touch() {
    this.lastSeenAt = LocalDateTime.now();
  }

  public PushDevice unlink() {
    this.member = null;
    this.touch();
    return this;
  }

  public PushDevice setEnabled(boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  public PushDevice update(String token) {
    this.token = token;
    this.touch();
    return this;
  }
}
