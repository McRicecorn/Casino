package de.casino.banking_service.user.mapper;

import de.casino.banking_service.user.model.UserEntity;
import de.casino.banking_service.user.Response.DeleteUserResponse;
import de.casino.banking_service.user.Response.GetUserResponse;

import java.util.List;

public class UserMapper {

    private UserMapper() {
        // Private constructor to prevent instantiation
    }
    public static GetUserResponse toResponse(UserEntity user) {
        return new GetUserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBalance()
        );
    }
    public static List<GetUserResponse> toResponseList(List<UserEntity> users) {
        return users.stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public static DeleteUserResponse DeleteUserResponse(UserEntity user) {
        return new DeleteUserResponse(
                user.getFirstName(),
                user.getLastName(),
                user.getBalance()
        );
    }
}