package de.casino.banking_service.user.UserResponseFactory;

import de.casino.banking_service.user.Response.IUserResponse;
import de.casino.banking_service.user.model.IUserEntity;



public interface IUserResponseFactory {
    IUserResponse createGet(IUserEntity user);
    IUserResponse createDelete(IUserEntity user);

}
