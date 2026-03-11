package de.casino.banking_service.user.controller;


import de.casino.banking_service.user.handler.UserHandler;
import de.casino.banking_service.user.mapper.UserMapper;
import de.casino.banking_service.user.model.UserEntity;
import de.casino.banking_service.user.Request.CreateUserRequest;
import de.casino.banking_service.user.Response.DeleteUserResponse;
import de.casino.banking_service.user.Request.UpdateUserRequest;
import de.casino.banking_service.user.Response.GetUserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

//@AllArgsConstructor
@RestController
@RequestMapping("/casino/bank/api")
public class UserController {


    private final UserHandler userHandler;

    public UserController(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable Long id) {
            UserEntity user = userHandler.getUserById(id);
           return ResponseEntity.ok(UserMapper.toResponse(user));


    }

    @GetMapping("/users")
    public ResponseEntity<List<GetUserResponse>> getAllUsers() {

        return ResponseEntity.ok(UserMapper.toResponseList(userHandler.getAllUsers()));
    }

    @PostMapping("/user")
    public ResponseEntity<GetUserResponse> createUser(@Valid @RequestBody CreateUserRequest userRequest) {
            UserEntity user = userHandler.createUser(userRequest.firstName(), userRequest.lastName());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(UserMapper.toResponse(user)); // 201 Created

    }

    @PutMapping("/user/{id}")
    public ResponseEntity<GetUserResponse> renameUser(@PathVariable Long id,
                                                      @Valid  @RequestBody UpdateUserRequest userRequest) {

        UserEntity user = userHandler.rename(id, userRequest.firstName(), userRequest.lastName());
        return ResponseEntity.ok(UserMapper.toResponse(user)); // 200 OK

    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<DeleteUserResponse> deleteUser(@PathVariable Long id) {

            UserEntity user = userHandler.deleteUserByID(id);
            return ResponseEntity.ok(UserMapper.DeleteUserResponse(user));

    }

    @PostMapping("/user/{id}/deposit/{amount}/{decimals}")
    public ResponseEntity<GetUserResponse> deposit(
            @PathVariable Long id,
            @PathVariable String amount,
            @PathVariable String decimals) {


        BigDecimal value = parseAmount(amount, decimals);

        UserEntity updatedUser = userHandler.deposit(id, value);
            return ResponseEntity.ok(UserMapper.toResponse(updatedUser)); // 200 OK


}
    @PostMapping("/user/{id}/withdraw/{amount}/{decimals}")
    public ResponseEntity<GetUserResponse> withdraw(
            @PathVariable Long id,
            @PathVariable String amount,
            @PathVariable String decimals) {

        BigDecimal value = parseAmount(amount, decimals);

        UserEntity updatedUser = userHandler.withdraw(id, value);

        return ResponseEntity.ok(UserMapper.toResponse(updatedUser));
    }

    private BigDecimal parseAmount(String amount, String decimals) {
        return new BigDecimal(amount + "." + decimals);
    }
}
