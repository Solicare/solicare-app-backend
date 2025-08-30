package com.solicare.app.backend.application.controller;

import com.solicare.app.backend.application.dto.request.SeniorAuthRequestDTO;
import com.solicare.app.backend.domain.dto.output.senior.SeniorJoinOutput;
import com.solicare.app.backend.domain.dto.output.senior.SeniorLoginOutput;
import com.solicare.app.backend.domain.service.SeniorService;
import com.solicare.app.backend.global.res.ApiResponse;
import com.solicare.app.backend.global.res.ApiResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Senior", description = "모니터링 대상 관련 API")
@RestController
@RequestMapping("/api/senior")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SeniorController {

    private final SeniorService seniorService;
    private final ApiResponseFactory apiResponseFactory;

    @Operation(summary = "모니터링 대상 회원가입", description = "새로운 모니터링 대상을 등록합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 존재하는 사용자 ID")
    })
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<SeniorJoinOutput>> join(
            @RequestBody @Valid SeniorAuthRequestDTO.Join joinRequestDTO) {
        SeniorJoinOutput result = seniorService.createAndIssueToken(joinRequestDTO);
        return apiResponseFactory.onSuccess(result);
    }

    @Operation(summary = "모니터링 대상 로그인", description = "사용자 ID와 비밀번호로 로그인합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "자격 증명 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<SeniorLoginOutput>> login(
            @RequestBody @Valid SeniorAuthRequestDTO.Login loginRequestDTO) {
        SeniorLoginOutput result = seniorService.loginAndIssueToken(loginRequestDTO);
        return apiResponseFactory.onSuccess(result);
    }
}