package com.ohgiraffers.springsecurity.command.service;

import com.ohgiraffers.springsecurity.command.dto.UserCreateRequest;
import com.ohgiraffers.springsecurity.command.entity.User;
import com.ohgiraffers.springsecurity.command.entity.UserRole;
import com.ohgiraffers.springsecurity.command.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    /* 신규 User 등록 */
    @Transactional
    public void registUser(UserCreateRequest userCreateRequest) {

        /* Request(DTO) to User Entity */
        User user = this.modelMapper.map(userCreateRequest, User.class);

        /* 비밀번호 암호화 */
        user.setEncodedPassword(this.passwordEncoder.encode(userCreateRequest.getPassword()));

        /* 저장 */
        this.userRepository.save(user);
    }

    /* 신규 ADMIN 등록 */
    @Transactional
    public void registAdmin(UserCreateRequest userCreateRequest) {
        /* Request(DTO) to User Entity */
        User user = this.modelMapper.map(userCreateRequest, User.class);

        /* 비밀번호 암호화 */
        user.setEncodedPassword(this.passwordEncoder.encode(userCreateRequest.getPassword()));

        /* 권한 변경 */
        user.modifyRole("ADMIN");

        /* 저장 */
        this.userRepository.save(user);

    }

}
