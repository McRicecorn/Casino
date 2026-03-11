package de.casino.banking_service.user.UserResponseFactory;

import de.casino.banking_service.user.Response.DeleteUserResponse;
import de.casino.banking_service.user.Response.GetUserResponse;
import de.casino.banking_service.user.Response.IUserResponse;
import de.casino.banking_service.user.model.IUserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserResponseFactory implements IUserResponseFactory{
        @Override
        public IUserResponse createGet(IUserEntity user) {
            return  new GetUserResponse(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getBalance());
        }
        @Override
        public IUserResponse createDelete(IUserEntity user) {
            return new DeleteUserResponse(

                    user.getFirstName(),
                    user.getLastName(),
                    user.getBalance()
            );
        }

}
