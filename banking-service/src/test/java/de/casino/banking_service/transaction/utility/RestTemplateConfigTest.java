package de.casino.banking_service.transaction.utility;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RestTemplateConfig.class)
class RestTemplateConfigTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void restTemplateBean_shouldBeCreated() {
        assertNotNull(restTemplate);
        assertTrue(restTemplate instanceof RestTemplate);
    }
}