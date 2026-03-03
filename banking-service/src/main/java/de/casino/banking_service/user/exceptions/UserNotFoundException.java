package de.casino.banking_service.user.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }
}
