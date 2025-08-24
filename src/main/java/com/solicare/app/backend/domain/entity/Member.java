package com.solicare.app.backend.domain.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString(exclude = "password") // 비밀번호 로그 노출 방지
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String uuid;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String phoneNumber;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = false)
  private List<PushDevice> pushDevices = new ArrayList<>();

  // TODO: add column 'seniors' (List of UUIDs) for seniors under care
}
