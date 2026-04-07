package de.casino.banking_service.user.controller;


import de.casino.banking_service.user.handler.IUserHandler;
import de.casino.banking_service.user.Request.CreateUserRequest;
import de.casino.banking_service.user.Request.UpdateUserRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

//@AllArgsConstructor
@RestController
@RequestMapping("/casino/bank/api")
public class UserController {


    private final IUserHandler userHandler;

    @Autowired
    public UserController(IUserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {

         var result = userHandler.getUserByIdResponse(id);

        if (result.isSuccess()) {

            return ResponseEntity.ok(result.getSuccessData().get());
        } else {
            return ResponseEntity.status(
                            result.getFailureData().get().getHttpStatus())
                    .body(result.getFailureData().get().getMessage());
        }


    }

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest userRequest) {
        var user = userHandler.createUser(userRequest);

        if (user.isSuccess()) {

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(user.getSuccessData().get());
        } else {
            return ResponseEntity.status(
                            user.getFailureData().get().getHttpStatus())
                    .body(user.getFailureData().get().getMessage());
        }

    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> renameUser(@PathVariable Long id,
                                                      @Valid  @RequestBody UpdateUserRequest userRequest) {

        var result = userHandler.updateUserName(id, userRequest);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getSuccessData().get());
        } else {
            return ResponseEntity.status(
                            result.getFailureData().get().getHttpStatus())
                    .body(result.getFailureData().get().getMessage());
        }


    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        var result = userHandler.deleteUser(id);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getSuccessData().get());
        } else {
            return ResponseEntity.status(
                            result.getFailureData().get().getHttpStatus())
                    .body(result.getFailureData().get().getMessage());
        }

    }
    @GetMapping("/users")
    public ResponseEntity<?> getAllUser() {

        var result = userHandler.getAllUsers();
        return ResponseEntity.ok(result.getSuccessData().get());

    }
    @PostMapping("/user/{id}/deposit/{amount}/{decimals}")
    public ResponseEntity<?> deposit(
            @PathVariable Long id,
            @PathVariable String amount,
            @PathVariable String decimals) {

        BigDecimal value = parseAmount(amount, decimals);
       var result = userHandler.deposit(id, value);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getSuccessData().get());
        } else {
            return ResponseEntity.status(
                            result.getFailureData().get().getHttpStatus())
                    .body(result.getFailureData().get().getMessage());
        }

    }
    @PostMapping("/user/{id}/withdraw/{amount}/{decimals}")
    public ResponseEntity<?> withdraw(
            @PathVariable Long id,
            @PathVariable String amount,
            @PathVariable String decimals) {

        BigDecimal value = parseAmount(amount, decimals);

        var result = userHandler.withdraw(id, value);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getSuccessData().get());
        } else {
            return ResponseEntity.status(
                            result.getFailureData().get().getHttpStatus())
                    .body(result.getFailureData().get().getMessage());
        }
    }


    private BigDecimal parseAmount(String amount, String decimals) {
        return new BigDecimal(amount + "." + decimals);
    }
}