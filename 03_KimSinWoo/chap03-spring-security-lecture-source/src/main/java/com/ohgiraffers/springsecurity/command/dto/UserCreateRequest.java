package com.ohgiraffers.springsecurity.command.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserCreateRequest {

    private final String username;
    private final String password;

    // 가입 시 필요한 추가 필드 영역 ......
}
