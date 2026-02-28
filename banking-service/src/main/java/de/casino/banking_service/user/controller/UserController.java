package de.casino.banking_service.user.controller;


import de.casino.banking_service.user.handler.UserHandler;
import de.casino.banking_service.user.model.UserEntity;
import de.casino.banking_service.user.view.CreateUserRequest;
import de.casino.banking_service.user.view.UpdateUserRequest;
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
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
       try {
           UserEntity user = userHandler.getUserById(id);
           return ResponseEntity.ok(user);
         } catch (IllegalArgumentException e) {
              return ResponseEntity.notFound().build();
       }

    }

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userHandler.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/user")
    public ResponseEntity<UserEntity> createUser(@RequestBody CreateUserRequest userRequest) {
        try {
            UserEntity user = userHandler.createUser(userRequest.first_name, userRequest.last_name);
            return ResponseEntity.status(HttpStatus.CREATED).body(user); // 201 Created
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserEntity> renameUser(@PathVariable Long id,
                                                 @RequestBody UpdateUserRequest userRequest) {
        try {
            UserEntity user = userHandler.rename(id, userRequest.first_name, userRequest.last_name);
            return ResponseEntity.ok(user); // 200 OK
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
            }
            return ResponseEntity.badRequest().build(); // 400 bei Validierung
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<UserEntity> deleteUser(@PathVariable Long id) {
        try {
            UserEntity user = userHandler.deleteUserByID(id);
            return ResponseEntity.ok(user); // 200 OK, ohne ID sichtbar
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    @PostMapping("/user/{id}/deposit/{amount}/{decimals}")
    public ResponseEntity<UserEntity> deposit(
            @PathVariable Long id,
            @PathVariable String amount,
            @PathVariable String decimals) {

        try {
            BigDecimal value = new BigDecimal(amount + "." + decimals);
            UserEntity updatedUser = userHandler.deposit(id, value);
            return ResponseEntity.ok(updatedUser); // 200 OK

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build(); // 400 wenn keine Zahl

        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
            }
            return ResponseEntity.badRequest().build(); // 400 bei negativer Zahl etc.
        }
}
/*
    @PostMapping("/user/{id}/withdraw/{amount}/{decimals}")
    public void withdraw(@PathVariable Long id, @PathVariable BigDecimal amount) {

    }
*/
}
