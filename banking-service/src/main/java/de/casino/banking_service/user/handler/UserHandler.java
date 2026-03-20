package de.casino.banking_service.user.handler;

import de.casino.banking_service.user.Request.IUserRequest;
import de.casino.banking_service.user.Response.IUserResponse;
import de.casino.banking_service.user.UserFactory.IUserFactory;
import de.casino.banking_service.user.UserResponseFactory.IUserResponseFactory;
import de.casino.banking_service.user.Utility.ErrorWrapper;
import de.casino.banking_service.user.Utility.Result;

import de.casino.banking_service.user.model.UserEntity;
import de.casino.banking_service.user.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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



    private Result<UserEntity, ErrorWrapper> findUserById(Long id) {
        var result = userRepository.findById(id);
        if (result.isPresent()) {
            return Result.success(result.get());
        }
        return Result.failure(ErrorWrapper.USER_NOT_FOUND);
    }
/*
    private Result<IUserResponse, ErrorWrapper> getUserByName(String firstName, String lastName) {
       var existingUser = userRepository.findByFirstNameAndLastName(firstName, lastName);

        if (existingUser.isPresent()) {
            return Result.success(userResponseFactory.createGet(existingUser.get()));
        }
        return Result.failure(ErrorWrapper.USER_NOT_FOUND);

    }
 */

    public Result<IUserResponse, ErrorWrapper> getUserByIdResponse(long id) {
        var userResult = findUserById(id);

        if(userResult.isFailure()) {
            return Result.failure(userResult.getFailureData().get());
        }

        return Result.success(
                userResponseFactory.createGet(userResult.getSuccessData().get())
        );
    }



    public Result<IUserResponse, ErrorWrapper> createUser(IUserRequest request) {

        /*
        if (getUserByName(request.firstName(), request.lastName() ).isSuccess()) {
            return Result.failure(ErrorWrapper.USER_MODEL_IDENTICAL_NAME);
        }


         */
        var user = userFactory.create(request.firstName(), request.lastName());

        if (user.isFailure()) {
            return Result.failure(user.getFailureData().get());

        }

        userRepository.save((UserEntity) user.getSuccessData().get());

        var response = userResponseFactory.createGet( user.getSuccessData().get());
        return Result.success(response);
    }

    public Result<IUserResponse, ErrorWrapper> updateUserName(long id, IUserRequest request) {
        var findUser = findUserById(id);
        if (findUser.isFailure()) {
            return Result.failure(findUser.getFailureData().get());
        }
        var user = findUser.getSuccessData().get();

        var updateResult =  user.rename(request.firstName(), request.lastName());

        if (updateResult.isFailure()) {
            return Result.failure(updateResult.getFailureData().get());
        }

        userRepository.save(user);
        return Result.success(userResponseFactory.createGet( user));

    }


     public Result<IUserResponse, ErrorWrapper> deleteUser(long id) {
         var findUser = findUserById(id);
         if (findUser.isFailure()) {
             return Result.failure(findUser.getFailureData().get());
         }
         var user = findUser.getSuccessData().get();
         userRepository.delete( user);
         var response = userResponseFactory.createDelete(user);

         return Result.success(response);
     }

     public Result<Iterable<IUserResponse>, ErrorWrapper> getAllUsers (){
            var users = userRepository.findAll();
            var result  = new ArrayList<IUserResponse>();
            for (UserEntity user : users) {
                var response = userResponseFactory.createGet(user);
                result.add(response);
            }
            return Result.success(result);
     }

     public Result<IUserResponse, ErrorWrapper> deposit(long id, BigDecimal amount) {
        var findUser = findUserById(id);
        if (findUser.isFailure()) {
            return Result.failure(findUser.getFailureData().get());
        }
        var user = findUser.getSuccessData().get();

        var depositResult = user.deposit(amount);
        if (depositResult.isFailure()) {
            return Result.failure(depositResult.getFailureData().get());
        }
        userRepository.save(user);
        return Result.success(userResponseFactory.createGet( user));
    }

     public Result<IUserResponse, ErrorWrapper> withdraw(long id, BigDecimal amount) {
         var findUser = findUserById(id);
         if (findUser.isFailure()) {
             return Result.failure(findUser.getFailureData().get());
         }
         var user = findUser.getSuccessData().get();

        var withdrawResult = user.withdraw(amount);
        if (withdrawResult.isFailure()) {
            return Result.failure(withdrawResult.getFailureData().get());
        }
        userRepository.save( user);

        return Result.success(userResponseFactory.createGet( user));
    }



}