package com.ohgiraffers.springsecurity.command.controller;

import com.ohgiraffers.springsecurity.command.dto.UserCreateRequest;
import com.ohgiraffers.springsecurity.command.service.UserCommandService;
import com.ohgiraffers.springsecurity.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserCommandController {

    private final UserCommandService userCommandService;

    /* 회원가입 */
    @PostMapping("/users")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody UserCreateRequest userCreateRequest) {
        // 서비스 호출
        this.userCommandService.registUser(userCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

    /* 관리자 계정 가입 */
    @PostMapping("/admin")
    public ResponseEntity<ApiResponse<Void>> admin(@RequestBody UserCreateRequest userCreateRequest) {
        // 서비스 호출
        this.userCommandService.registAdmin(userCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

}
