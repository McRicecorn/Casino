package de.casino.banking_service.user.handler;

import de.casino.banking_service.user.model.UserEntity;
import de.casino.banking_service.user.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserHandlerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserHandler userHandler;


    //2 tests, klappt und klappt nicht
    //Given / When / Then
    //Happy Path
    @Test
    void createUser_shouldSaveCorrectUser() {
        //Given
        when(userRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        //When
        userHandler.createUser("Max", "Mustermann");

        //Then
        ArgumentCaptor<UserEntity> captor =
                ArgumentCaptor.forClass(UserEntity.class);

        verify(userRepository).save(captor.capture());

        UserEntity savedUser = captor.getValue();

        assertEquals("Max", savedUser.getFirstName());
        assertEquals("Mustermann", savedUser.getLastName());
    }
    @Test
    void createUser_shouldThrowException_whenFirstNameIsInvalid() {

        // WHEN + THEN
        assertThrows(IllegalArgumentException.class, () ->
                userHandler.createUser("", "Mustermann")
        );

        // optional: prüfen, dass save nie aufgerufen wurde
        verify(userRepository, never()).save(any());
    }
    @Test
    void createUser_shouldThrowException_whenRepositoryFails() {

        // GIVEN
        when(userRepository.save(any()))
                .thenThrow(new RuntimeException("DB error"));

        // WHEN + THEN
        assertThrows(RuntimeException.class, () -> userHandler.createUser("Max", "Mustermann"));
    }

    @Test
    void getUserById_shouldReturnUser_whenUserExists() {
        // Given
        UserEntity fakeUser = new UserEntity("Max", "Mustermann");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(fakeUser));

        // When
        UserEntity result = userHandler.getUserById(1L);

        // Then
        assertEquals("Max", result.getFirstName());
        assertEquals("Mustermann", result.getLastName());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_shouldThrowException_whenUserDoesNotExist() {
        // Given
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        // When + Then
        assertThrows(IllegalArgumentException.class, () -> userHandler.getUserById(1L));
        verify(userRepository).findById(1L);
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        // Given
        UserEntity user1 = new UserEntity("Max", "Mustermann");
        UserEntity user2 = new UserEntity("Erika", "Musterfrau");

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        // When
        List<UserEntity> result = userHandler.getAllUsers();

        // Then
        assertEquals(2, result.size());
        assertEquals("Max", result.get(0).getFirstName());
        assertEquals("Erika", result.get(1).getFirstName());
            verify(userRepository).findAll();
    }

    @Test
    void getAllUsers_shouldReturnEmptyList_whenNoUsersExist() {
        // Given
        when(userRepository.findAll())
                .thenReturn(List.of());

        // When
        List<UserEntity> result = userHandler.getAllUsers();

        // Then
        assertTrue(result.isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    void deposit_shouldIncreaseBalance_whenAmountIsPositive_andSaveUser() {
        // Given
        UserEntity user = new UserEntity("Max", "Mustermann");
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        // When
        UserEntity result = userHandler.deposit(1L, new BigDecimal("100.00"));

        // Then
        assertEquals(new BigDecimal("100.00"), result.getBalance());
        verify(userRepository).save(user);
    }
    @Test
    void deposit_shouldThrowException_whenAmountIsNegative() {
        // Given
        UserEntity user = new UserEntity("Max", "Mustermann");
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        // When + Then
        assertThrows(IllegalArgumentException.class, () ->
                userHandler.deposit(1L, new BigDecimal("-50.00"))
        );
        verify(userRepository,never()).save(any());
    }

    @Test
    void deposit_shouldThrowException_whenUserDoesNotExist() {
        // Given
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        // When + Then
        assertThrows(IllegalArgumentException.class, () ->
                userHandler.deposit(1L, new BigDecimal("50.00"))
        );
        verify(userRepository,never()).save(any());
    }
    @Test
    void deposit_shouldThrowException_whenRepositoryFails() {
        // Given
        UserEntity user = new UserEntity("Max", "Mustermann");
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any()))
                .thenThrow(new RuntimeException("DB error"));

        // When + Then
        assertThrows(RuntimeException.class, () -> userHandler.deposit(1L, new BigDecimal("50.00"))
        );

    }

    @Test
    void withdraw_shouldDecreaseBalance_whenAmountIsPositive_andSaveUser() {
        // Given
        UserEntity user = new UserEntity("Max", "Mustermann");
        user.deposit(new BigDecimal("100.00"));
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        // When
        UserEntity result = userHandler.withdraw(1L, new BigDecimal("30.00"));

        // Then
        assertEquals(new BigDecimal("70.00"), result.getBalance());
        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
    }

    @Test
    void withdraw_shouldThrowException_whenAmountIsNegative() {
        // Given
        UserEntity user = new UserEntity("Max", "Mustermann");
        user.deposit(new BigDecimal("100.00"));
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        // When + Then
        assertThrows(IllegalArgumentException.class, () ->
                userHandler.withdraw(1L, new BigDecimal("-20.00"))
        );

        verify(userRepository,never()).save(any());
    }

    @Test
    void withdraw_shouldThrowException_whenAmountExceedsBalance() {
        // Given
        UserEntity user = new UserEntity("Max", "Mustermann");
        user.deposit(new BigDecimal("50.00"));
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        // When + Then
        assertThrows(IllegalArgumentException.class, () ->
                userHandler.withdraw(1L, new BigDecimal("60.00"))
        );
        verify(userRepository,never()).save(any());
    }

    @Test
    void withdraw_shouldThrowException_whenUserDoesNotExist() {
        // Given
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        // When + Then
        assertThrows(IllegalArgumentException.class, () ->
                userHandler.withdraw(1L, new BigDecimal("20.00"))
        );
        verify(userRepository,never()).save(any());
    }

    @Test
    void withdraw_shouldThrowException_whenRepositoryFails() {
        // Given
        UserEntity user = new UserEntity("Max", "Mustermann");
        user.deposit(new BigDecimal("100.00"));
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any()))
                .thenThrow(new RuntimeException("DB error"));

        // When + Then
        assertThrows(RuntimeException.class, () ->
                userHandler.withdraw(1L, new BigDecimal("20.00"))
        );

    }

    @Test
    void rename_shouldChangeUserName_andSaveUser() {
        // Given
        UserEntity user = new UserEntity("Max", "Mustermann");
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        // When
        UserEntity result = userHandler.rename(1L, "Erika", "Musterfrau");

        // Then
        assertEquals("Erika", result.getFirstName());
        assertEquals("Musterfrau", result.getLastName());
        verify(userRepository).save(user);
    }

    @Test
    void rename_shouldThrowException_whenFirstNameIsInvalid() {
        // Given
        UserEntity user = new UserEntity("Max", "Mustermann");
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        // When + Then
        assertThrows(IllegalArgumentException.class, () ->
                userHandler.rename(1L, "", "Musterfrau")
        );
        verify(userRepository,never()).save(any());
    }

    @Test
    void rename_shouldThrowException_whenUserDoesNotExist() {
        // Given
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        // When + Then
        assertThrows(IllegalArgumentException.class, () ->
                userHandler.rename(1L, "Erika", "Musterfrau")
        );
        verify(userRepository,never()).save(any());
    }

    @Test
    void rename_shouldThrowException_whenRepositoryFails() {
        // Given
        UserEntity user = new UserEntity("Max", "Mustermann");
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any()))
                .thenThrow(new RuntimeException("DB error"));

        // When + Then
        assertThrows(RuntimeException.class, () ->
                userHandler.rename(1L, "Erika", "Musterfrau")
        );
    }


}

