package de.casino.banking_service.user.handler;

import de.casino.banking_service.user.exceptions.UserNotFoundException;
import de.casino.banking_service.user.model.UserEntity;
import de.casino.banking_service.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserHandler {

    private final UserRepository userRepository;

    public UserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public UserEntity deposit(Long id, BigDecimal amount) {
        UserEntity user = getUserById(id);
        user.deposit(amount);
        return userRepository.save(user);
    }

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

}