package com.solicare.app.backend.application.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CareRelationResponseDTO {

    @Schema(name = "CareRelationLinkResponse", description = "돌봄 관계 연결 응답 DTO")
    public record Link(
            @Schema(description = "멤버 이름") String memberName,
            @Schema(description = "시니어 이름") String seniorName,
            @Schema(description = "돌봄 관계 UUID") String relationUuid
    ) {}
}