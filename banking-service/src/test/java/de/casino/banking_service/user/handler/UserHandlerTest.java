package de.casino.banking_service.user.handler;

import de.casino.banking_service.user.Request.IUserRequest;
import de.casino.banking_service.user.Response.IUserResponse;
import de.casino.banking_service.user.UserFactory.IUserFactory;
import de.casino.banking_service.user.UserResponseFactory.IUserResponseFactory;
import de.casino.banking_service.user.Utility.ErrorResult;
import de.casino.banking_service.user.Utility.ErrorWrapper;
import de.casino.banking_service.user.Utility.Result;

import de.casino.banking_service.user.model.UserEntity;
import de.casino.banking_service.user.repository.IUserRepository;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserHandlerTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IUserFactory userFactory;

    @Mock
    private IUserResponseFactory userResponseFactory;

    @Mock
    private IUserRequest userRequest;

    @Mock
    private IUserResponse userResponse;

    @InjectMocks
    private UserHandler userHandler;

    @Nested
    class GetUserByIdTests {
        @Test
        void getUserById_ExistingUser_ReturnsUserResponse() {

            UserEntity userEntity = mock(UserEntity.class);

            when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
            when(userResponseFactory.createGet(userEntity)).thenReturn(userResponse);


            // Act
            var result = userHandler.getUserByIdResponse(1L);

            // Assert
            assertTrue(result.isSuccess());
            assertEquals(userResponse, result.getSuccessData().get());
            verify(userRepository, times(1)).findById(1L);
            verify(userResponseFactory, times(1)).createGet(userEntity);
        }

        @Test
        void getUserById_NonExistingUser_ReturnsUserNotFoundError() {
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            // Act
            Result<IUserResponse, ErrorWrapper> result = userHandler.getUserByIdResponse(1L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_NOT_FOUND, result.getFailureData().get());
            verify(userRepository, times(1)).findById(1L);
                verifyNoInteractions(userResponseFactory);
        }
    }

    @Nested
    class GetAllUsersTests {
        @Test
        void getAllUsers_ReturnsListOfUserResponses() {
            UserEntity userEntity1 = mock(UserEntity.class);
            UserEntity userEntity2 = mock(UserEntity.class);

            when(userRepository.findAll()).thenReturn(List.of(userEntity1, userEntity2));
            IUserResponse userResponse1 = mock(IUserResponse.class);
            IUserResponse userResponse2 = mock(IUserResponse.class);
            when(userResponseFactory.createGet(userEntity1)).thenReturn(userResponse1);
            when(userResponseFactory.createGet(userEntity2)).thenReturn(userResponse2);

            // Act
            var result = userHandler.getAllUsers();

            List<IUserResponse> responses = new ArrayList<>();
            result.getSuccessData().get().forEach(responses::add);

            // Assert
            assertTrue(result.isSuccess());
            assertEquals(2, responses.size());
            assertTrue(responses.contains(userResponse1));
            assertTrue(responses.contains(userResponse2));
            verify(userRepository, times(1)).findAll();
            verify(userResponseFactory, times(1)).createGet(userEntity1);
            verify(userResponseFactory, times(1)).createGet(userEntity2);
        }

        @Test
        void getAllUsers_NoUsers_ReturnsEmptyList() {
            when(userRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            var result = userHandler.getAllUsers();

            // Assert
            assertTrue(result.isSuccess());
            assertTrue(result.getSuccessData().get().iterator().hasNext() == false);
            verify(userRepository, times(1)).findAll();}
    }

    @Nested
    class createUserTests {

        @Test
        void createUser_ValidRequest_CreatesUser() {
            when(userRequest.firstName()).thenReturn("John");
            when(userRequest.lastName()).thenReturn("Doe");
            UserEntity userEntity = mock(UserEntity.class);
            when(userFactory.create("John", "Doe")).thenReturn(Result.success(userEntity));

            when(userResponseFactory.createGet(userEntity)).thenReturn(userResponse);

            // Act
            Result<IUserResponse, ErrorWrapper> result  = userHandler.createUser(userRequest);

            // Assert
            assertTrue(result.isSuccess());
            assertEquals(userResponse, result.getSuccessData().get());
            verify(userRepository, times(1)).save(userEntity);
            verify(userFactory, times(1)).create("John", "Doe");
            verify(userResponseFactory, times(1)).createGet(userEntity);
        }

        @Test
        void createUser_WhenInvalidRequest_ShouldReturnError() {
            when(userRequest.firstName()).thenReturn("John");
            when(userRequest.lastName()).thenReturn(null);
            when(userFactory.create("John", null)).thenReturn(Result.failure(ErrorWrapper.USER_MODEL_INVALID_NAME_Blank_OR_NULL));

            // Act
            Result<IUserResponse, ErrorWrapper> result = userHandler.createUser(userRequest);

            // Assert
            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_MODEL_INVALID_NAME_Blank_OR_NULL, result.getFailureData().get());
            verify(userFactory, times(1)).create("John", null);
            verifyNoInteractions(userRepository, userResponseFactory);
        }
    }

    @Nested
    class updateUserNameTests {
        @Test
        void updateUserName_ValidRequest_ShouldReturnUpdate() {
            UserEntity existingUser = mock(UserEntity.class);

            when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(userRequest.firstName()).thenReturn("John");
            when(userRequest.lastName()).thenReturn("Doe");

            when(existingUser.rename("John", "Doe")).thenReturn(ErrorResult.success());
            when(userResponseFactory.createGet(existingUser)).thenReturn(userResponse);

            when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(userResponseFactory.createGet(existingUser)).thenReturn(userResponse);

            // Act
            Result<IUserResponse, ErrorWrapper> result = userHandler.updateUserName(1L, userRequest);

            // Assert
            assertTrue(result.isSuccess());
            assertEquals(userResponse, result.getSuccessData().get());
            verify(userRepository, times(1)).findById(1L);
            verify(existingUser, times(1)).rename("John", "Doe");
            verify(userResponseFactory, times(1)).createGet(existingUser);
            verify(userRepository, times(1)).save(existingUser);


        }

        @Test
        void updateUserName_NonExistingUser_ShouldReturnUserNotFoundError() {
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            // Act
            Result<IUserResponse, ErrorWrapper> result = userHandler.updateUserName(1L, userRequest);

            // Assert
            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_NOT_FOUND, result.getFailureData().get());
            verify(userRepository, times(1)).findById(1L);
            verifyNoInteractions(userResponseFactory);
        }

        @Test
        void updateUserName_InvalidRename_IdenticalName_ShouldReturnError() {
            UserEntity existingUser = mock(UserEntity.class);
            when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(userRequest.firstName()).thenReturn("John");
            when(userRequest.lastName()).thenReturn("Doe");

            when(existingUser.rename("John", "Doe")).thenReturn(ErrorResult.failure(ErrorWrapper.USER_MODEL_IDENTICAL_NAME));

            // Act
            Result<IUserResponse, ErrorWrapper> result = userHandler.updateUserName(1L, userRequest);

            // Assert
            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_MODEL_IDENTICAL_NAME, result.getFailureData().get());
            verify(userRepository, times(1)).findById(1L);
            verify(existingUser, times(1)).rename("John", "Doe");
            verifyNoInteractions(userResponseFactory);
        }
    }

    @Nested
    class deleteUserTests {
        @Test
        void deleteUser_ExistingUser_ShouldDeleteUser() {
            UserEntity existingUser = mock(UserEntity.class);
            when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

            // Act
            Result<IUserResponse, ErrorWrapper> result = userHandler.deleteUser(1L);

            // Assert
            assertTrue(result.isSuccess());
            verify(userRepository, times(1)).findById(1L);
            verify(userRepository, times(1)).delete(existingUser);
        }

        @Test
        void deleteUser_NonExistingUser_ShouldReturnUserNotFoundError() {
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            // Act
            Result<IUserResponse, ErrorWrapper> result = userHandler.deleteUser(1L);

            // Assert
            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_NOT_FOUND, result.getFailureData().get());
            verify(userRepository, times(1)).findById(1L);
            verify(userRepository, times(0)).delete(any(UserEntity.class));
        }
    }

    @Nested
    class depositTests {
        @Test
        void deposit_ValidAmount_ShouldDeposit() {
            UserEntity existingUser = mock(UserEntity.class);
            when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(existingUser.deposit(BigDecimal.valueOf(100))).thenReturn(ErrorResult.success());

            // Act
            Result<IUserResponse, ErrorWrapper> result = userHandler.deposit(1L, BigDecimal.valueOf(100));

            // Assert
            assertTrue(result.isSuccess());
            verify(userRepository, times(1)).findById(1L);
            verify(existingUser, times(1)).deposit(BigDecimal.valueOf(100));
            verify(userRepository, times(1)).save(existingUser);
        }

         @Test
        void deposit_NonExistingUser_ShouldReturnUserNotFoundError() {
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            // Act
            Result<IUserResponse, ErrorWrapper> result = userHandler.deposit(1L, BigDecimal.valueOf(100));

            // Assert
            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_NOT_FOUND, result.getFailureData().get());
            verify(userRepository, times(1)).findById(1L);
            verifyNoInteractions(userResponseFactory);
        }

         @Test
        void deposit_InvalidAmount_ShouldReturnError() {
            UserEntity existingUser = mock(UserEntity.class);
            when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(existingUser.deposit(BigDecimal.valueOf(-100))).thenReturn(ErrorResult.failure(ErrorWrapper.USER_MODEL_INVALID_AMOUNT_NEGATIVE));

            // Act
            Result<IUserResponse, ErrorWrapper> result = userHandler.deposit(1L, BigDecimal.valueOf(-100));

            // Assert
            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_MODEL_INVALID_AMOUNT_NEGATIVE, result.getFailureData().get());
            verify(userRepository, times(1)).findById(1L);
            verify(existingUser, times(1)).deposit(BigDecimal.valueOf(-100));
            verifyNoInteractions(userResponseFactory);

         }
    }

    @Nested
    class withdrawTests {

        @Test
        void withdraw_ValidAmount_ShouldWithdraw() {
            UserEntity existingUser = mock(UserEntity.class);
            when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(existingUser.withdraw(BigDecimal.valueOf(100))).thenReturn(ErrorResult.success());

            // Act
            Result<IUserResponse, ErrorWrapper> result = userHandler.withdraw(1L, BigDecimal.valueOf(100));

            // Assert
            assertTrue(result.isSuccess());
            verify(userRepository, times(1)).findById(1L);
            verify(existingUser, times(1)).withdraw(BigDecimal.valueOf(100));
            verify(userRepository, times(1)).save(existingUser);
        }

         @Test
        void withdraw_NonExistingUser_ShouldReturnUserNotFoundError() {
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            // Act
            Result<IUserResponse, ErrorWrapper> result = userHandler.withdraw(1L, BigDecimal.valueOf(100));

            // Assert
            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_NOT_FOUND, result.getFailureData().get());
            verify(userRepository, times(1)).findById(1L);
            verifyNoInteractions(userResponseFactory);
        }

         @Test
        void withdraw_InvalidAmount_ShouldReturnError() {
            UserEntity existingUser = mock(UserEntity.class);
            when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(existingUser.withdraw(BigDecimal.valueOf(-100))).thenReturn(ErrorResult.failure(ErrorWrapper.USER_MODEL_INVALID_AMOUNT_NEGATIVE));

            // Act
            Result<IUserResponse, ErrorWrapper> result = userHandler.withdraw(1L, BigDecimal.valueOf(-100));

            // Assert
            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_MODEL_INVALID_AMOUNT_NEGATIVE, result.getFailureData().get());
            verify(userRepository, times(1)).findById(1L);
            verify(existingUser, times(1)).withdraw(BigDecimal.valueOf(-100));
            verifyNoInteractions(userResponseFactory);

         }
    }

}

