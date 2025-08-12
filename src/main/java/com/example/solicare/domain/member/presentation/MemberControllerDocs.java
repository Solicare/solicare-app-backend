package com.example.solicare.domain.member.presentation;

import com.example.solicare.domain.member.application.dto.MemberLoginRequestDTO;
import com.example.solicare.domain.member.application.dto.MemberSaveRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member1")
public class MemberControllerDocs {

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "409", description = "중복 회원")
    })
    @PostMapping("/join")
    public void joinDoc(@RequestBody MemberSaveRequestDTO request) {
        // Swagger 문서용 - 실제 로직 없음
    }

    @Operation(summary = "로그인", description = "전화번호와 비밀번호로 로그인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "자격 증명 실패")
    })
    @PostMapping("/login")
    public void loginDoc(@RequestBody MemberLoginRequestDTO request) {
        // Swagger 문서용 - 실제 로직 없음
    }
}
