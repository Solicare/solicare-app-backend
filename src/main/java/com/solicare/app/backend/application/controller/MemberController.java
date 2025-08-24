package com.solicare.app.backend.application.controller;

import com.solicare.app.backend.application.dto.request.MemberRequestDTO;
import com.solicare.app.backend.application.dto.res.MemberResponseDTO;
import com.solicare.app.backend.domain.dto.output.member.MemberJoinOutput;
import com.solicare.app.backend.domain.dto.output.member.MemberLoginOutput;
import com.solicare.app.backend.domain.service.MemberService;
import com.solicare.app.backend.global.apiPayload.ApiResponse;
import com.solicare.app.backend.global.apiPayload.response.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "회원 관련 API")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
  private final MemberService memberService;

  @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "회원가입 성공"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "잘못된 요청"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "409",
        description = "중복 회원")
  })
  @PostMapping("/join")
  public ResponseEntity<ApiResponse<MemberResponseDTO.Join>> join(
      @Schema(name = "MemberRequestJoin", description = "회원가입 요청 DTO") @RequestBody @Valid
          MemberRequestDTO.Join memberJoinRequestDTO) {
    MemberJoinOutput result = memberService.createAndIssueToken(memberJoinRequestDTO);
    // TODO: check result of creation and respond accordingly
    return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, null));
  }

  @Operation(summary = "로그인", description = "전화번호와 비밀번호로 로그인합니다.")
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "로그인 성공"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "401",
        description = "자격 증명 실패")
  })
  @PostMapping("/login")
  public ResponseEntity<ApiResponse<MemberResponseDTO.Login>> login(
      @RequestBody @Valid MemberRequestDTO.Login memberLoginRequestDTO) {
    MemberLoginOutput resp = memberService.loginAndIssueToken(memberLoginRequestDTO);
    // TODO: check result of creation and respond accordingly
    return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, null));
  }

  @GetMapping("profile?uuid={uuid}")
  public ResponseEntity<ApiResponse<MemberResponseDTO.Profile>> getProfile(
      @NonNull Authentication authentication, @PathVariable("uuid") String uuid) {
    // TODO: check if the authenticated user matches the requested uuid
    // TODO: fetch and return the profile information
    return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, null));
  }
}
