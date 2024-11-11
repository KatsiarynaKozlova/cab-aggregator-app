package com.software.modsen.authservice.mapper;

import com.software.modsen.authservice.dto.request.FeignUserRequest;
import com.software.modsen.authservice.dto.request.UserRequest;
import com.software.modsen.authservice.dto.response.UserResponse;
import com.software.modsen.authservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUserModel(UserRequest userRequest);
    @Mapping(source = "username", target = "name")
    UserResponse toResponse(User user);
    @Mapping(source = "username", target = "name")
    FeignUserRequest toPassengerRequest(UserRequest userRequest);
}
