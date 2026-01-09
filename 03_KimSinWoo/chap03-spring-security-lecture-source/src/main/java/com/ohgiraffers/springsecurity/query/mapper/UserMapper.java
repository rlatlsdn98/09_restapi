package com.ohgiraffers.springsecurity.query.mapper;

import com.ohgiraffers.springsecurity.query.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    UserDTO findUserByUsername(String username);

    List<UserDTO> findAllUsers();
}
