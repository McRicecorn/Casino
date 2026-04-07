package de.casino.banking_service.user.UserFactory;

import de.casino.banking_service.user.Utility.ErrorWrapper;
import de.casino.banking_service.user.Utility.Result;
import de.casino.banking_service.user.model.IUserEntity;

public interface IUserFactory {

    Result<IUserEntity, ErrorWrapper> create(String first_name, String last_name);
}
