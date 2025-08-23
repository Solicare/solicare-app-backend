package com.solicare.app.backend.domain.entity;

import com.solicare.app.backend.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@RequiredArgsConstructor(staticName = "of")
@Table(name = "senior")
@ToString(exclude = "password")
public class Senior {
    @Id
    @Column(nullable = false, length = 20, unique = true)
    @NonNull
    private String userId;
    @Column(nullable = false, length = 4)
    @NonNull
    private String password;

    @Column(nullable = false)
    @NonNull
    private String name;

    @Column(nullable = false)
    @NonNull
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NonNull
    private Gender gender;

    @Column(nullable = false)
    private String phoneNumber;
    @NonNull
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String note;
}
