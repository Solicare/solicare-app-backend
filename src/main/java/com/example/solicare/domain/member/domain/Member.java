package com.example.solicare.domain.member.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String elderlyName; //
    private String password;
    @Column(nullable = false, unique = true)
    private String phoneNumber;


    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;
}
