package de.casino.banking_service.user.model;


import de.casino.banking_service.user.exceptions.InvalidAmountException;
import de.casino.banking_service.user.exceptions.InvalidUserDataException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {
    //normal test cases for UserEntity class
    @Test
    void createUserEntity() {
        UserEntity user = new UserEntity("John", "Doe");
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals(BigDecimal.ZERO.setScale(2), user.getBalance());
    }

    @Test
    void deposit_IncreaseBalance() {
        UserEntity user = new UserEntity("John", "Doe");

        user.deposit(new BigDecimal("100.00"));

        assertEquals(new BigDecimal("100.00"), user.getBalance());
    }

    @Test
    void withdraw_DecreaseBalance() {
        UserEntity user = new UserEntity("John", "Doe");
        user.deposit(new BigDecimal("100.00"));

        user.withdraw(new BigDecimal("30.00"));

        assertEquals(new BigDecimal("70.00"), user.getBalance());
    }

    @Test
    void rename_ChangeFirstAndLastName() {
        UserEntity user = new UserEntity("John", "Doe");

        user.rename("Jane", "Smith");

        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
    }
    //edge cases for UserEntity class - creation
    @Test
    void createUserEntity_nullFirstName() {
        assertThrows(InvalidUserDataException.class, () -> {
            new UserEntity(null, "Doe");
        });
    }
    @Test
    void createUserEntity_nullLastName() {
        assertThrows(InvalidUserDataException.class, () -> {
            new UserEntity("John", null);
        });
    }
    @Test
    void createUserEntity_blankFirstName() {
        assertThrows(InvalidUserDataException.class, () -> {
            new UserEntity("", "Doe");
        });
    }
    @Test
    void createUserEntity_blankLastName() {
        assertThrows(InvalidUserDataException.class, () -> {
            new UserEntity("John", "");
        });
    }
    @Test
    void createUserEntity_whitespaceFirstName() {
        assertThrows(InvalidUserDataException.class, () -> {
            new UserEntity("   ", "Doe");
        });
    }
    @Test
    void createUserEntity_whitespaceLastName() {
        assertThrows(InvalidUserDataException.class, () -> {
            new UserEntity("John", "   ");
        });
    }


    //edge cases for UserEntity class - withdrawal
    @Test
    void withdraw_InsufficientFunds() {
        UserEntity user = new UserEntity("John", "Doe");

        assertThrows(InvalidAmountException.class, () -> {
            user.withdraw(new BigDecimal("50.00"));
        });

    }
    @Test
    void withdraw_NegativeAmount() {
        UserEntity user = new UserEntity("John", "Doe");

        assertThrows(InvalidAmountException.class, () -> {
            user.withdraw(new BigDecimal("-10.00"));
        });

    }

    @Test
    void withdraw_NullAmount() {
        UserEntity user = new UserEntity("John", "Doe");

        assertThrows(InvalidAmountException.class, () -> {
            user.withdraw(null);
        });

    }
    @Test
    void withdraw_TooManyDecimalPlaces() {
        UserEntity user = new UserEntity("John", "Doe");

        assertThrows(InvalidAmountException.class, () -> {
            user.withdraw(new BigDecimal("10.123"));
        });
    }



    @Test
    void withdraw_ZeroAmount() {
        UserEntity user = new UserEntity("John", "Doe");

        assertThrows(InvalidAmountException.class, () -> {
            user.withdraw(BigDecimal.ZERO);
        });


    }

    @Test
    void withdraw_exactBalance() {
        UserEntity user = new UserEntity("John", "Doe");
        user.deposit(new BigDecimal("100.00"));

        user.withdraw(new BigDecimal("100.00"));

        assertEquals(BigDecimal.ZERO.setScale(2), user.getBalance());
    }
    //edge cases for UserEntity class - deposit

    @Test
    void deposit_NegativeAmount() {
        UserEntity user = new UserEntity("John", "Doe");

        assertThrows(InvalidAmountException.class, () -> {
            user.deposit(new BigDecimal("-10.00"));
        });

    }

    @Test
    void deposit_NullAmount() {
        UserEntity user = new UserEntity("John", "Doe");

        assertThrows(InvalidAmountException.class, () -> {
            user.deposit(null);
        });

    }

    @Test
    void deposit_TooManyDecimalPlaces() {
        UserEntity user = new UserEntity("John", "Doe");

        assertThrows(InvalidAmountException.class, () -> {
            user.deposit(new BigDecimal("10.123"));
        });

    }

    @Test
    void deposit_ZeroAmount() {
        UserEntity user = new UserEntity("John", "Doe");

        assertThrows(InvalidAmountException.class, () -> {
            user.deposit(BigDecimal.ZERO);
        });

    }

    @Test
    void deposit_integerAmount() {
        UserEntity user = new UserEntity("John", "Doe");

        user.deposit(new BigDecimal("50"));

        assertEquals(new BigDecimal("50.00"), user.getBalance());
    }

    @Test
    void deposit_extremeAmount() {
        UserEntity user = new UserEntity("John", "Doe");

        user.deposit(new BigDecimal("1000000000.00"));

        assertEquals(new BigDecimal("1000000000.00"), user.getBalance());
    }

    @Test
    void deposit_smallAmount() {
        UserEntity user = new UserEntity("John", "Doe");

        user.deposit(new BigDecimal("0.01"));

        assertEquals(new BigDecimal("0.01"), user.getBalance());
    }

    @Test
    void deposit_randomizedValues() {
        UserEntity user = new UserEntity("John", "Doe");

        for (int i = 0; i < 100; i++) {
            BigDecimal value = BigDecimal.valueOf(Math.random() * 1000)
                    .setScale(2, RoundingMode.HALF_UP);

            user.deposit(value);
        }

        assertTrue(user.getBalance().compareTo(BigDecimal.ZERO) > 0);
    }
     //edge cases for UserEntity class - rename


    @Test
    void rename_blankFirstName() {
        UserEntity user = new UserEntity("John", "Doe");

        assertThrows(InvalidUserDataException.class, () -> {
            user.rename("", "Smith");
        });

    }

    @Test
    void rename_blankLastName() {
        UserEntity user = new UserEntity("John", "Doe");

        assertThrows(InvalidUserDataException.class, () -> {
            user.rename("Jane", "");
        });

    }

    @Test
    void rename_SameName() {
        UserEntity user = new UserEntity("John", "Doe");

        assertThrows(InvalidUserDataException.class, () -> {
            user.rename("John", "Doe");
        });

    }

    @Test
    void rename_NullFirstName() {
        UserEntity user = new UserEntity("John", "Doe");

        assertThrows(InvalidUserDataException.class, () -> {
            user.rename(null, "Smith");
        });

    }
    @Test
    void rename_NullLastName() {
        UserEntity user = new UserEntity("John", "Doe");

        assertThrows(InvalidUserDataException.class, () -> {
            user.rename("Jane", null);
        });

    }









}