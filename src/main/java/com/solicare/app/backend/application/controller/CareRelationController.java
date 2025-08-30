package com.solicare.app.backend.application.controller;

import com.solicare.app.backend.application.dto.request.SeniorRequestDTO;
import com.solicare.app.backend.domain.dto.output.care.CareLinkOutput;
import com.solicare.app.backend.domain.service.CareRelationService;
import com.solicare.app.backend.global.res.ApiResponse;
import com.solicare.app.backend.global.res.ApiResponseFactory;
import com.solicare.app.backend.global.res.ApiStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Care Relation", description = "돌봄 관계 API")
@RestController
@RequestMapping("/api/care")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CareRelationController {

    private final CareRelationService careRelationService;
    private final ApiResponseFactory apiResponseFactory;

    @Operation(summary = "멤버와 시니어 연결", description = "로그인된 멤버와 특정 시니어를 연결하여 돌봄 관계를 생성합니다.",
            security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping("/link")
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public ResponseEntity<ApiResponse<Object>> linkSenior(
            @NonNull Authentication authentication,
            @RequestBody @Valid SeniorRequestDTO.Link requestDto) {

        // 요청 DTO의 UUID와 현재 로그인한 사용자의 UUID가 일치하는지 확인
        if (!authentication.getName().equals(requestDto.monitorUserUuid())) {
            return apiResponseFactory.onFailure(ApiStatus._FORBIDDEN, "자신의 계정으로만 연결 요청을 할 수 있습니다.");
        }

        CareLinkOutput result = careRelationService.linkSeniorToMember(requestDto);

        if (!result.isSuccess()) {
            ApiStatus status = switch (result.getStatus()) {
                case MEMBER_NOT_FOUND, SENIOR_NOT_FOUND -> ApiStatus._NOT_FOUND;
                case INVALID_SENIOR_PASSWORD -> ApiStatus._UNAUTHORIZED;
                case RELATION_ALREADY_EXISTS -> ApiStatus._BAD_REQUEST;
                default -> ApiStatus._INTERNAL_SERVER_ERROR;
            };
            return apiResponseFactory.onFailure(status, result.getStatus().name());
        }

        return apiResponseFactory.onSuccess(result.getResponse());
    }
}