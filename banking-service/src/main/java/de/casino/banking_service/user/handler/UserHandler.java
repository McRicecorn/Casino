package de.casino.banking_service.user.handler;

import de.casino.banking_service.user.Response.IUserResponse;
import de.casino.banking_service.user.UserFactory.IUserFactory;
import de.casino.banking_service.user.UserResponseFactory.IUserResponseFactory;
import de.casino.banking_service.user.Utility.ErrorWrapper;
import de.casino.banking_service.user.Utility.Result;
import de.casino.banking_service.user.exceptions.UserNotFoundException;
import de.casino.banking_service.user.model.IUserEntity;
import de.casino.banking_service.user.model.UserEntity;
import de.casino.banking_service.user.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserHandler implements IUserHandler {

    private  final IUserRepository userRepository;
    private  final IUserFactory userFactory;
    private  final IUserResponseFactory userResponseFactory;

    @Autowired
    public UserHandler(IUserRepository userRepository, IUserFactory userFactory, IUserResponseFactory userResponseFactory) {
        this.userRepository = userRepository;
        this.userFactory =  userFactory;
        this.userResponseFactory = userResponseFactory;

    }

    public UserEntity deleteUserByID(Long id) {
        UserEntity user = getUserById(id);
        userRepository.delete(user);
        return user;
    }

    public UserEntity createUser(String first_name, String last_name) {
        UserEntity user = new UserEntity(first_name, last_name);
        return userRepository.save(user);
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public UserEntity deposit(Long id, BigDecimal amount) {
        UserEntity user = getUserById(id);
        user.deposit(amount);
        return userRepository.save(user);
    }

    @Transactional
    public UserEntity withdraw(Long id, BigDecimal amount) {
        UserEntity user = getUserById(id);
        user.withdraw(amount);
        return userRepository.save(user);
    }

    public UserEntity rename(Long id, String first_name, String last_name) {
        UserEntity user = getUserById(id);
        user.rename(first_name, last_name);
        return userRepository.save(user);
    }

    /// ////////////////////////////
    public Result<IUserResponse, ErrorWrapper> getUserByIdResponse(Long id) {
        var result = userRepository.findById(id);
        if (result.isPresent()) {
            return Result.success(userResponseFactory.createGet(result.get()));
        }
        return Result.failure(ErrorWrapper.USER_NOT_FOUND);
    }

    public List<IUserResponse> getAllUsersResponse() {
        List<UserEntity> users = userRepository.findAll();
        List <IUserResponse> userResponses = new ArrayList<>();
        for (UserEntity user : users) {
            userResponses.add(userResponseFactory.createGet(user));
        }
        return userResponses    ;
    }



}