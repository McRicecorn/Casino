package de.casino.banking_service.transaction.response.userResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class GetUserClientResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void jsonMapping_validInput_shouldMapCorrectly() throws Exception {

        // given
        String json = """
        {
          "id": 1,
          "first_name": "Max",
          "last_name": "Mustermann",
          "balance": 100.50
        }
        """;

        // when
        GetUserClientResponse response =
                objectMapper.readValue(json, GetUserClientResponse.class);

        // then
        assertNotNull(response);

        assertEquals(1L, response.getId());
        assertEquals("Max", response.getFirstName());
        assertEquals("Mustermann", response.getLastName());
        assertEquals(new BigDecimal("100.50"), response.getBalance());
    }

    @Test
    void jsonMapping_missingFields_shouldAllowNulls() throws Exception {

        // given
        String json = """
        {
          "id": 2
        }
        """;

        // when
        GetUserClientResponse response =
                objectMapper.readValue(json, GetUserClientResponse.class);

        // then
        assertNotNull(response);

        assertEquals(2L, response.getId());
        assertNull(response.getFirstName());
        assertNull(response.getLastName());
        assertNull(response.getBalance());
    }
}