package com.software.modsen.authservice.mapper;

import com.software.modsen.authservice.dto.request.UserLoginRequest;
import com.software.modsen.authservice.model.UserLogin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserLoginMapper {
    UserLogin toUserLoginModel(UserLoginRequest userLoginRequest);
}
