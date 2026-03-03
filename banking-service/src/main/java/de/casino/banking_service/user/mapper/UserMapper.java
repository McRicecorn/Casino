package de.casino.banking_service.user.mapper;

import de.casino.banking_service.user.model.UserEntity;
import de.casino.banking_service.user.view.UserResponse;

import java.util.List;

public class UserMapper {

    private UserMapper() {
        // Private constructor to prevent instantiation
    }
    public static UserResponse toResponse(UserEntity user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBalance()
        );
    }
    public static List<UserResponse> toResponseList(List<UserEntity> users) {
        return users.stream()
                .map(UserMapper::toResponse)
                .toList();
    }
}