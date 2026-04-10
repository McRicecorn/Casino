package de.casino.banking_service.user.UserFactory;

import de.casino.banking_service.common.Result;
import de.casino.banking_service.user.Utility.ErrorWrapper;
import de.casino.banking_service.user.model.IUserEntity;
import de.casino.banking_service.user.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserFactory implements IUserFactory{


        @Override
        public Result<IUserEntity, ErrorWrapper> create(String firstName, String lastName) {
            return  UserEntity.create(firstName, lastName);
        }
}
