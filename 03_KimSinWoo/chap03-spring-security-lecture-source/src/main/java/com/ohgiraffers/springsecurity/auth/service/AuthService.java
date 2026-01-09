package com.ohgiraffers.springsecurity.auth.service;

import com.ohgiraffers.springsecurity.auth.dto.LoginRequest;
import com.ohgiraffers.springsecurity.auth.dto.TokenResponse;
import com.ohgiraffers.springsecurity.auth.entity.RefreshToken;
import com.ohgiraffers.springsecurity.auth.repository.AuthRepository;
import com.ohgiraffers.springsecurity.command.entity.User;
import com.ohgiraffers.springsecurity.command.repository.UserRepository;
import com.ohgiraffers.springsecurity.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthRepository authRepository;

    public TokenResponse login(LoginRequest loginRequest) {
        // 1. ID(username)로 조회 -> id(username), pwd(암호화) 조회됨
        User user = this.userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다"));

        // 2. 비밀번호 매칭 확인
        if(!this.passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다");
        }

        // 3. 비밀번호가 일치 -> 로그인 성공 -> 토큰 생성 -> 발급
        String accessToken = this.jwtTokenProvider.createToken(user.getUsername(), user.getRole().name());
        String refreshToken = this.jwtTokenProvider.createRefreshToken(user.getUsername(), user.getRole().name());

        // 4. refresh token DB에 저장(보안 및 토큰 재발급 검증용)
        RefreshToken tokenEntity = RefreshToken.builder()
                .username(user.getUsername())
                .token(refreshToken)
                .expiryDate(new Date(System.currentTimeMillis() + jwtTokenProvider.getRefreshExpiration())).build();

        this.authRepository.save(tokenEntity);

        return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    /* DB refresh token 삭제 */
    public void logout(String refreshToken) {
        // refreshToken 검증 절차
        this.jwtTokenProvider.validateToken(refreshToken);

        String username = this.jwtTokenProvider.getUsernameFromJWT(refreshToken);

        this.authRepository.deleteById(username); // DB에서 username이 일치하는 행을 삭제
    }

    /* refresh token 검증 후 새 token 발급 서비스 */
    public TokenResponse refreshToken(String provideRefreshToken) {
        // refresh token 유효성 검사
        this.jwtTokenProvider.validateToken(provideRefreshToken);

        // 전달 받은 refresh token 에서 사용자 이름(username) 얻어오기
        String username = this.jwtTokenProvider.getUsernameFromJWT(provideRefreshToken);

        // DB에서 username이 일치하는 행의 refresh token을 조회
        RefreshToken storedToken  = this.authRepository.findById(username)
                .orElseThrow(()->new BadCredentialsException("해당 유저로 조회되는 refresh token 없음"));

        // 넘어온 요청 시 전달 받은 refresh token 과 DB에 저장된 refresh token이 일치하는지 확인
        if (!storedToken.getToken().equals(provideRefreshToken)) {
            throw new BadCredentialsException("refresh token이 일치하지 않음");
        }

        // DB에 저장된 token의 만료 기간이 현재 시간 보다 과거인지 확인
        // (만료 기간이 지났는지 확인)
        if (storedToken.getExpiryDate().before(new Date())) {
            throw new BadCredentialsException("refresh token 기간 만료");
        }

        // username이 일치하는 회원(user) 조회
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(()->new BadCredentialsException("해당 유저 없음"));

        // 새로운 token 발급
        String accessToken = this.jwtTokenProvider.createToken(user.getUsername(), user.getRole().name());
        String refreshToken = this.jwtTokenProvider.createRefreshToken(user.getUsername(), user.getRole().name());

        // refresh token entity 생성 (저장용)
        RefreshToken tokenEntity = RefreshToken.builder()
                .username(username)
                .token(refreshToken)
                .expiryDate(new Date(System.currentTimeMillis() + this.jwtTokenProvider.getRefreshExpiration()))
                .build();
        // DB 저장 (PK 중복 행이 이미 존재 -> UPDATE)
        this.authRepository.save(tokenEntity);

        // TokenResponse 반환
        return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

}
