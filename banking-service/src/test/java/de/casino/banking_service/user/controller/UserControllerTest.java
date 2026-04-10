package de.casino.banking_service.user.controller;

import de.casino.banking_service.common.Result;
import de.casino.banking_service.user.Request.CreateUserRequest;

import de.casino.banking_service.user.Request.UpdateUserRequest;
import de.casino.banking_service.user.Response.DeleteUserResponse;
import de.casino.banking_service.user.Response.GetUserResponse;
import de.casino.banking_service.user.Response.IUserResponse;
import de.casino.banking_service.user.Utility.ErrorWrapper;

import de.casino.banking_service.user.handler.IUserHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


import static org.mockito.Mockito.*;



public class UserControllerTest {

    private UserController controller;
    private IUserHandler mockHandler;

    @BeforeEach
    public void setUp() {
        mockHandler = mock(IUserHandler.class);
        controller = new UserController(mockHandler);
    }


    @Test
    void getUserById_WhenUserExists_ShouldReturnResponse() {

        Long id = 1L;
        IUserResponse response = mock(IUserResponse.class);

        when(mockHandler.getUserByIdResponse(id))
                .thenReturn(Result.success(response));

        ResponseEntity<?> result = controller.getUserById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());

        verify(mockHandler, times(1)).getUserByIdResponse(id);
    }
    @Test
    void getUserById_WhenUserDoesNotExist_ShouldReturnNotFound() {

        Long id = 1L;
        ErrorWrapper error = mock(ErrorWrapper.class);

        when(mockHandler.getUserByIdResponse(id))
                .thenReturn(Result.failure(error));

        when(error.getHttpStatus()).thenReturn(HttpStatus.NOT_FOUND);
        when(error.getMessage()).thenReturn("User not found");

        ResponseEntity<?> result = controller.getUserById(id);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("User not found", result.getBody());
        verify(mockHandler, times(1)).getUserByIdResponse(id);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {

        List<IUserResponse> users = List.of(mock(IUserResponse.class), mock(IUserResponse.class));

        when(mockHandler.getAllUsers())
                .thenReturn(Result.success(users));

        ResponseEntity<?> result = controller.getAllUser();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(users, result.getBody());

        verify(mockHandler, times(1)).getAllUsers();
    }

    @Test
    void createUser_WhenValidRequest_ShouldReturnUser() {

        CreateUserRequest request = mock(CreateUserRequest.class);
        IUserResponse response = mock(IUserResponse.class);

        when(mockHandler.createUser(request))
                .thenReturn(Result.success(response));

        ResponseEntity<?> result = controller.createUser(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());

        verify(mockHandler, times(1)).createUser(request);
    }

    @Test
    void createUser_WhenInvalidRequest_ShouldReturnBadRequest() {

        CreateUserRequest request = mock(CreateUserRequest.class);
        ErrorWrapper error = mock(ErrorWrapper.class);

        when(mockHandler.createUser(request))
                .thenReturn(Result.failure(error));

        when(error.getHttpStatus()).thenReturn(HttpStatus.BAD_REQUEST);
        when(error.getMessage()).thenReturn("Invalid user data");

        ResponseEntity<?> result = controller.createUser(request);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid user data", result.getBody());

        verify(mockHandler, times(1)).createUser(request);
    }

    @Test
    void updateUserName_WhenValidRequest_ShouldReturnUser() {
        Long id = 1L;
        UpdateUserRequest request = mock(UpdateUserRequest.class);
        IUserResponse response = mock(IUserResponse.class);

        when(mockHandler.updateUserName(id, request))
                .thenReturn(Result.success(response));

        ResponseEntity<?> result = controller.renameUser(id, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());

        verify(mockHandler, times(1)).updateUserName(id, request);
    }

    @Test
    void updateUserName_WhenInvalidRequest_ShouldReturnBadRequest() {
        Long id = 1L;
        UpdateUserRequest request = mock(UpdateUserRequest.class);
        ErrorWrapper error = mock(ErrorWrapper.class);

        when(mockHandler.updateUserName(id, request))
                .thenReturn(Result.failure(error));

        when(error.getHttpStatus()).thenReturn(HttpStatus.BAD_REQUEST);
        when(error.getMessage()).thenReturn("Invalid user data");

        ResponseEntity<?> result = controller.renameUser(id, request);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid user data", result.getBody());

        verify(mockHandler, times(1)).updateUserName(id, request);
    }

    @Test
    void deleteUser_WhenUserExists_ShouldReturnSuccess() {
        Long id = 1L;
        DeleteUserResponse response = mock(DeleteUserResponse.class);

        when(mockHandler.deleteUser(id))
                .thenReturn(Result.success(response));

        ResponseEntity<?> result = controller.deleteUser(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());

        verify(mockHandler, times(1)).deleteUser(id);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldReturnNotFound() {
        Long id = 1L;
        ErrorWrapper error = mock(ErrorWrapper.class);

        when(mockHandler.deleteUser(id))
                .thenReturn(Result.failure(error));

        when(error.getHttpStatus()).thenReturn(HttpStatus.NOT_FOUND);
        when(error.getMessage()).thenReturn("User not found");

        ResponseEntity<?> result = controller.deleteUser(id);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("User not found", result.getBody());

        verify(mockHandler, times(1)).deleteUser(id);
    }


    @Test
    void deposit_WhenValidRequest_ShouldReturnUser() {

        Long id = 1L;
        BigDecimal amount = new BigDecimal("100.00");
        GetUserResponse response = mock(GetUserResponse.class);

        when(mockHandler.deposit(id, amount))
                .thenReturn(Result.success(response));

        ResponseEntity<?> result = controller.deposit(id, "100", "00");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(mockHandler, times(1)).deposit(id, amount);

    }

    @Test
    void deposit_WhenInvalidRequest_ShouldReturnBadRequest() {
        Long id = 1L;
        BigDecimal amount = new BigDecimal("-100.00");
        ErrorWrapper error = mock(ErrorWrapper.class);

        when(mockHandler.deposit(id, amount))
                .thenReturn(Result.failure(error));

        when(error.getHttpStatus()).thenReturn(HttpStatus.BAD_REQUEST);
        when(error.getMessage()).thenReturn("Invalid amount");

        ResponseEntity<?> result = controller.deposit(id, "-100", "00");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid amount", result.getBody());

        verify(mockHandler, times(1)).deposit(id, amount);
    }

    @Test
    void withdraw_WhenValidRequest_ShouldReturnUser() {

        Long id = 1L;
        BigDecimal amount = new BigDecimal("50.00");
        GetUserResponse response = mock(GetUserResponse.class);

        when(mockHandler.withdraw(id, amount))
                .thenReturn(Result.success(response));

        ResponseEntity<?> result = controller.withdraw(id, "50", "00");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(mockHandler, times(1)).withdraw(id, amount);

    }

    @Test
    void withdraw_WhenInvalidRequest_ShouldReturnBadRequest() {
        Long id = 1L;
        BigDecimal amount = new BigDecimal("-50.00");
        ErrorWrapper error = mock(ErrorWrapper.class);

        when(mockHandler.withdraw(id, amount))
                .thenReturn(Result.failure(error));

        when(error.getHttpStatus()).thenReturn(HttpStatus.BAD_REQUEST);
        when(error.getMessage()).thenReturn("Invalid amount");

        ResponseEntity<?> result = controller.withdraw(id, "-50", "00");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid amount", result.getBody());

        verify(mockHandler, times(1)).withdraw(id, amount);
    }
}