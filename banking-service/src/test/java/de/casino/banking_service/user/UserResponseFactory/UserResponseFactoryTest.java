package de.casino.banking_service.user.UserResponseFactory;

import de.casino.banking_service.user.Response.IUserResponse;
import de.casino.banking_service.user.model.IUserEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserResponseFactoryTest {

    private UserResponseFactory factory;

    // Static Werte
    private static Long staticId;
    private static String staticFirstName;
    private static String staticLastName;
    private static BigDecimal staticBalance;

    // Random Werte
    private Long randomId;
    private String randomFirstName;
    private String randomLastName;
    private BigDecimal randomBalance;

    // Mock
    private IUserEntity mockUser;

    @BeforeAll
    public static void setUpAll() {
        staticId = 1L;
        staticFirstName = "John";
        staticLastName = "Doe";
        staticBalance = BigDecimal.valueOf(100.00);
    }

    @BeforeEach
    public void setUp() {
        factory = new UserResponseFactory();

        randomId = (long) (Math.random() * 1000);
        randomFirstName = "User" + (int)(Math.random() * 1000);
        randomLastName = "Test" + (int)(Math.random() * 1000);
        randomBalance = BigDecimal.valueOf(Math.random() * 1000);

        mockUser = mock(IUserEntity.class);
    }

    // =========================
    // GET RESPONSE TESTS
    // =========================

    @Test
    @DisplayName("createGet returns correct response with static values")
    public void createGet_WithStaticValues_ShouldReturnCorrectResponse() {
        when(mockUser.getFirstName()).thenReturn(staticFirstName);
        when(mockUser.getLastName()).thenReturn(staticLastName);
        when(mockUser.getBalance()).thenReturn(staticBalance);

        IUserResponse response = factory.createGet(mockUser);

        assertEquals(staticFirstName, response.firstName());
        assertEquals(staticLastName, response.lastName());
        assertEquals(staticBalance, response.balance());
    }

    @Test
    @DisplayName("createGet returns correct response with random values")
    public void createGet_WithRandomValues_ShouldReturnCorrectResponse() {
        when(mockUser.getFirstName()).thenReturn(randomFirstName);
        when(mockUser.getLastName()).thenReturn(randomLastName);
        when(mockUser.getBalance()).thenReturn(randomBalance);

        IUserResponse response = factory.createGet(mockUser);

        assertEquals(randomFirstName, response.firstName());
        assertEquals(randomLastName, response.lastName());
        assertEquals(randomBalance, response.balance());
    }

    @Test
    @DisplayName("createGet calls IUserEntity methods exactly once")
    public void createGet_ShouldCallMethodsOnce() {
        when(mockUser.getId()).thenReturn(randomId);
        when(mockUser.getFirstName()).thenReturn(randomFirstName);
        when(mockUser.getLastName()).thenReturn(randomLastName);
        when(mockUser.getBalance()).thenReturn(randomBalance);

        factory.createGet(mockUser);

        verify(mockUser, times(1)).getId();
        verify(mockUser, times(1)).getFirstName();
        verify(mockUser, times(1)).getLastName();
        verify(mockUser, times(1)).getBalance();
    }

    // =========================
    // DELETE RESPONSE TESTS
    // =========================

    @Test
    @DisplayName("createDelete returns correct response with static values")
    public void createDelete_WithStaticValues_ShouldReturnCorrectResponse() {
        when(mockUser.getFirstName()).thenReturn(staticFirstName);
        when(mockUser.getLastName()).thenReturn(staticLastName);
        when(mockUser.getBalance()).thenReturn(staticBalance);

        IUserResponse response = factory.createDelete(mockUser);

        assertEquals(staticFirstName, response.firstName());
        assertEquals(staticLastName, response.lastName());
        assertEquals(staticBalance, response.balance());
    }

    @Test
    @DisplayName("createDelete calls IUserEntity methods exactly once")
    public void createDelete_ShouldCallMethodsOnce() {
        when(mockUser.getFirstName()).thenReturn(randomFirstName);
        when(mockUser.getLastName()).thenReturn(randomLastName);
        when(mockUser.getBalance()).thenReturn(randomBalance);

        factory.createDelete(mockUser);

        verify(mockUser, times(1)).getFirstName();
        verify(mockUser, times(1)).getLastName();
        verify(mockUser, times(1)).getBalance();

        // wichtig: ID darf NICHT aufgerufen werden
        verify(mockUser, never()).getId();
    }
}