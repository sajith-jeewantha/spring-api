package com.springapi.store.mappers;

import com.springapi.store.dtos.RegisterUserRequest;
import com.springapi.store.dtos.UpdateUserRequest;
import com.springapi.store.dtos.UserDto;
import com.springapi.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    UserDto toDto(User user);

    User toEntity(RegisterUserRequest registerUserRequest);

    void update(UpdateUserRequest updateUserRequest, @MappingTarget User user);
}
