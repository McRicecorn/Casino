package de.casino.banking_service.user.handler;

import de.casino.banking_service.common.Result;
import de.casino.banking_service.user.Request.IUserRequest;
import de.casino.banking_service.user.Response.IUserResponse;
import de.casino.banking_service.user.Utility.ErrorWrapper;

import java.math.BigDecimal;

public interface IUserHandler {
    Result<IUserResponse, ErrorWrapper> getUserByIdResponse(long id);
    Result<Iterable<IUserResponse>, ErrorWrapper> getAllUsers();
    Result<IUserResponse, ErrorWrapper> createUser(IUserRequest request);
    Result<IUserResponse, ErrorWrapper> updateUserName(long id, IUserRequest request);
    Result<IUserResponse, ErrorWrapper> deleteUser(long id);
    Result<IUserResponse, ErrorWrapper> deposit(long id, BigDecimal amount);
    Result<IUserResponse, ErrorWrapper> withdraw(long id, BigDecimal amount);




}
