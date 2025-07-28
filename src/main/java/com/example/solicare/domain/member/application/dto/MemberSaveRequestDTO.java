package com.example.solicare.domain.member.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSaveRequestDTO {
    private String name;
    private String elderlyName;
    private String phoneNumber;
    private String password;
}
