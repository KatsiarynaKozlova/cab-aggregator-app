package com.software.modsen.authservice.mapper;

import com.software.modsen.authservice.dto.request.UserRequest;
import com.software.modsen.authservice.dto.response.UserResponse;
import com.software.modsen.authservice.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUserModel(UserRequest userRequest);
    UserResponse toResponse(User user);
}
