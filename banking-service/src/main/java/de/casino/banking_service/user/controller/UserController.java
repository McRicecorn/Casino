package de.casino.banking_service.user.controller;


import de.casino.banking_service.user.handler.UserHandler;
import de.casino.banking_service.user.mapper.UserMapper;
import de.casino.banking_service.user.model.UserEntity;
import de.casino.banking_service.user.view.CreateUserRequest;
import de.casino.banking_service.user.view.UpdateUserRequest;
import de.casino.banking_service.user.view.UserResponse;
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
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
            UserEntity user = userHandler.getUserById(id);
           return ResponseEntity.ok(UserMapper.toResponse(user));


    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(UserMapper.toResponseList(userHandler.getAllUsers()));
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest userRequest) {
            UserEntity user = userHandler.createUser(userRequest.firstName(), userRequest.lastName());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(UserMapper.toResponse(user)); // 201 Created

    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserResponse> renameUser(@PathVariable Long id,
                                               @Valid  @RequestBody UpdateUserRequest userRequest) {

        UserEntity user = userHandler.rename(id, userRequest.firstName(), userRequest.lastName());
        return ResponseEntity.ok(UserMapper.toResponse(user)); // 200 OK

    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id) {

            userHandler.deleteUserByID(id);
            return ResponseEntity.ok().build();

    }

    @PostMapping("/user/{id}/deposit/{amount}/{decimals}")
    public ResponseEntity<UserResponse> deposit(
            @PathVariable Long id,
            @PathVariable String amount,
            @PathVariable String decimals) {


            BigDecimal value = new BigDecimal(amount + "." + decimals);
            UserEntity updatedUser = userHandler.deposit(id, value);
            return ResponseEntity.ok(UserMapper.toResponse(updatedUser)); // 200 OK


}
    @PostMapping("/user/{id}/withdraw/{amount}/{decimals}")
    public ResponseEntity<UserResponse> withdraw(
            @PathVariable Long id,
            @PathVariable String amount,
            @PathVariable String decimals) {

        BigDecimal value = new BigDecimal(amount + "." + decimals);
        UserEntity updatedUser = userHandler.withdraw(id, value);

        return ResponseEntity.ok(UserMapper.toResponse(updatedUser));
    }
}
