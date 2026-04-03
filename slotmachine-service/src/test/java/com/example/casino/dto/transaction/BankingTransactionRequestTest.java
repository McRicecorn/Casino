package com.example.casino.dto.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BankingTransactionRequestTest {

    private String expectedParty = "Slotmachine-Service";
    private BigDecimal expectedAmount = new BigDecimal("20.00");


    @Test
    void getInvoicingParty() {
        //test
        BankingTransactionRequest request = new BankingTransactionRequest(expectedParty, expectedAmount);

        //assert
        assertEquals(expectedParty, request.getInvoicingParty());
    }

    @Test
    void setInvoicingParty() {
        //setup
        BankingTransactionRequest request = new BankingTransactionRequest(expectedParty, expectedAmount);
        String injectedParty = "pupsi-party";

        //test
        request.setInvoicingParty(injectedParty);

        //assert
        assertEquals(injectedParty, request.getInvoicingParty());
    }

    @Test
    void getAmount() {
        BankingTransactionRequest request = new BankingTransactionRequest(expectedParty, expectedAmount);

        //assert
        assertEquals(expectedAmount, request.getAmount());
    }

    @Test
    void setAmount() {
        //setup
        BankingTransactionRequest request = new BankingTransactionRequest(expectedParty, expectedAmount);
        BigDecimal injectedAmount = BigDecimal.valueOf(200);
        //test
        request.setAmount(injectedAmount);

        //assert
        assertEquals(injectedAmount, request.getAmount());
    }

    @Test
    @DisplayName("Test default constructor")
    void testDefaultConstructor(){
        //test
        BankingTransactionRequest request = new BankingTransactionRequest();
        //assert
        assertNotNull(request);
        assertEquals(null, request.getAmount());
    }
}