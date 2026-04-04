package de.casino.banking_service.user.UserFactory;

import de.casino.banking_service.user.Utility.ErrorWrapper;
import de.casino.banking_service.user.Utility.Result;
import de.casino.banking_service.user.model.IUserEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserFactoryTest {

    private UserFactory factory;

    private static Random rng;
    private static String emptyName;
    private static String nullName;

    private String randomFirstName;
    private String randomLastName;



    @BeforeAll
    public static void setUpAll() {
        rng = new Random();
        emptyName = "";
        nullName = null;
    }

    @BeforeEach
    public void setUp() {

        factory = spy(new UserFactory());

        randomFirstName = "User" + rng.nextInt(10000);
        randomLastName = "Last" + rng.nextInt(10000);

    }

    @Test
    public void create_WhenValidNames_ShouldReturnSuccess() {

        String[][] testCases = {
                {randomFirstName, randomLastName},
                {"Max", "Mustermann"},
                {"Anna", "Meier"}
        };

        for (String[] names : testCases) {

            Result<IUserEntity, ErrorWrapper> result =
                    factory.create(names[0], names[1]);

            assertTrue(result.isSuccess());
            assertTrue(result.getSuccessData().isPresent());

            IUserEntity user = result.getSuccessData().get();

            assertEquals(names[0], user.getFirstName());
            assertEquals(names[1], user.getLastName());

            verify(factory, times(1)).create(names[0], names[1]);
        }
    }

    @Test
    public void create_WhenInvalidFirstName_ShouldReturnFailure() {

        String[][] testCases = {
                {emptyName, "Mustermann"},
                {nullName, "Mustermann"}
        };

        for (String[] names : testCases) {

            Result<IUserEntity, ErrorWrapper> result =
                    factory.create(names[0], names[1]);

            assertTrue(result.isFailure());
            assertTrue(result.getFailureData().isPresent());

            verify(factory, times(1)).create(names[0], names[1]);
        }
    }

    @Test
    public void create_WhenInvalidLastName_ShouldReturnFailure() {

        String[][] testCases = {
                {"Max", emptyName},
                {"Max", nullName}
        };

        for (String[] names : testCases) {

            Result<IUserEntity, ErrorWrapper> result =
                    factory.create(names[0], names[1]);

            assertTrue(result.isFailure());
            assertTrue(result.getFailureData().isPresent());

            verify(factory, times(1)).create(names[0], names[1]);
        }
    }
}