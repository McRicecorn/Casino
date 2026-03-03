package de.casino.banking_service.user.controller;

import de.casino.banking_service.user.exceptions.GlobalExceptionHandler;
import de.casino.banking_service.user.exceptions.InvalidAmountException;
import de.casino.banking_service.user.exceptions.InvalidUserDataException;
import de.casino.banking_service.user.exceptions.UserNotFoundException;
import de.casino.banking_service.user.handler.UserHandler;
import de.casino.banking_service.user.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;


@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserHandler userHandler;

    //getUserById Tests
    @Test
    void getUserById_shouldReturn200() throws Exception{
        UserEntity user = new UserEntity("John", "Doe");

        when(userHandler.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/casino/bank/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

    }

    @Test
    void getUserById_shouldReturn400_UserString() throws Exception{
        mockMvc.perform(get("/casino/bank/api/user/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserById_shouldReturn404_NonExistingUser() throws Exception{
        when(userHandler.getUserById(999L)).thenThrow(new UserNotFoundException(999L));

        mockMvc.perform(get("/casino/bank/api/user/999"))
                .andExpect(status().isNotFound());
    }


    //getAllUsers Tests
    @Test
    void getAllUsers_shouldReturn200() throws Exception {
        List<UserEntity> users = List.of(
                new UserEntity("Max", "Mustermann"),
                new UserEntity("Anna", "Meyer")
        );

        when(userHandler.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/casino/bank/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    //createUser Tests
    @Test
    void createUser_shouldReturn201() throws Exception {

        String userJson = """
                {
                    "first_name": "Alice",
                    "last_name": "Smith"
                }
                """;
        UserEntity createdUser = new UserEntity("Alice", "Smith");
        when(userHandler.createUser("Alice", "Smith")).thenReturn(createdUser);

        mockMvc.perform(post("/casino/bank/api/user")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    void createUser_shouldReturn400_MissingFirstName() throws Exception {
        when(userHandler.createUser("", "Mustermann"))
                .thenThrow(new InvalidUserDataException("invalid"));

        String json = """
        {
            "first_name": "",
            "last_name": "Mustermann"
        }
    """;

        mockMvc.perform(post("/casino/bank/api/user")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_shouldReturn400_MissingLastName() throws Exception {
        when(userHandler.createUser("Max", ""))
                .thenThrow(new InvalidUserDataException("invalid"));

        String json = """
        {
            "first_name": "Max",
            "last_name": ""
        }
    """;

        mockMvc.perform(post("/casino/bank/api/user")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_shouldReturn400_BlankFirstName() throws Exception {
        String userJson = """
                {
                    "first_name": "   ",
                    "last_name": "Smith"
                }
                """;

        when(userHandler.createUser("   ", "Smith"))
                .thenThrow(new InvalidUserDataException("invalid"));

        mockMvc.perform(post("/casino/bank/api/user")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_shouldReturn400_BlankLastName() throws Exception {
        String userJson = """
                {
                    "first_name": "Alice",
                    "last_name": "   "
                }
                """;

        when(userHandler.createUser("Alice", "   "))
                .thenThrow(new InvalidUserDataException("invalid"));

        mockMvc.perform(post("/casino/bank/api/user")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_shouldReturn400_InvalidJson() throws Exception {
        String userJson = """
                {
                    "first_name": "Alice",
                    "last_name": "Smith
                }
                """;

        mockMvc.perform(post("/casino/bank/api/user")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deposit_shouldReturn200_whenValid() throws Exception {

        UserEntity user = new UserEntity("Max", "Mustermann");

        when(userHandler.deposit(eq(1L), any()))
                .thenReturn(user);

        mockMvc.perform(post("/casino/bank/api/user/1/deposit/100/00"))
                .andExpect(status().isOk());
    }

    @Test
    void deposit_shouldReturn400_whenInvalidNumber() throws Exception {

        mockMvc.perform(post("/casino/bank/api/user/1/deposit/abc/xy"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void renameUser_shouldReturn200() throws Exception {

        UserEntity renamedUser = new UserEntity("New", "Name");

        when(userHandler.rename(1L, "New", "Name"))
                .thenReturn(renamedUser);

        String json = """
        {
            "first_name": "New",
            "last_name": "Name"
        }
    """;

        mockMvc.perform(put("/casino/bank/api/user/1")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("New"))
                .andExpect(jsonPath("$.lastName").value("Name"));
    }

    @Test
    void renameUser_shouldReturn404_whenUserNotFound() throws Exception {

        when(userHandler.rename(999L, "New", "Name"))
                .thenThrow(new UserNotFoundException(999L));

        String json = """
        {
            "first_name": "New",
            "last_name": "Name"
        }
    """;

        mockMvc.perform(put("/casino/bank/api/user/999")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void renameUser_shouldReturn400_whenInvalidData() throws Exception {

        when(userHandler.rename(1L, "", "Name"))
                .thenThrow(new InvalidUserDataException("invalid"));

        String json = """
        {
            "first_name": "",
            "last_name": "Name"
        }
    """;

        mockMvc.perform(put("/casino/bank/api/user/1")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_shouldReturn200() throws Exception {

        UserEntity deletedUser = new UserEntity("John", "Doe");

        when(userHandler.deleteUserByID(1L))
                .thenReturn(deletedUser);

        mockMvc.perform(delete("/casino/bank/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void deleteUser_shouldReturn404() throws Exception {

        when(userHandler.deleteUserByID(999L))
                .thenThrow(new UserNotFoundException(999L));

        mockMvc.perform(delete("/casino/bank/api/user/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deposit_shouldReturn404_whenUserNotFound() throws Exception {

        when(userHandler.deposit(eq(999L), any()))
                .thenThrow(new UserNotFoundException(999L));

        mockMvc.perform(post("/casino/bank/api/user/999/deposit/100/00"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deposit_shouldReturn400_whenInvalidAmount() throws Exception {

        when(userHandler.deposit(eq(1L), any()))
                .thenThrow(new InvalidUserDataException("invalid"));

        mockMvc.perform(post("/casino/bank/api/user/1/deposit/100/00"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void withdraw_shouldReturn200_whenValid() throws Exception {

        UserEntity user = new UserEntity("Max", "Mustermann");

        when(userHandler.withdraw(eq(1L), any()))
                .thenReturn(user);

        mockMvc.perform(post("/casino/bank/api/user/1/withdraw/50/00"))
                .andExpect(status().isOk());
    }

    @Test
    void withdraw_shouldReturn400_whenInvalidAmount() throws Exception {

        when(userHandler.withdraw(eq(1L), any()))
                .thenThrow(new InvalidAmountException("invalid"));

        mockMvc.perform(post("/casino/bank/api/user/1/withdraw/50/00"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void withdraw_shouldReturn404_whenUserNotFound() throws Exception {

        when(userHandler.withdraw(eq(999L), any()))
                .thenThrow(new UserNotFoundException(999L));

        mockMvc.perform(post("/casino/bank/api/user/999/withdraw/50/00"))
                .andExpect(status().isNotFound());
    }
}