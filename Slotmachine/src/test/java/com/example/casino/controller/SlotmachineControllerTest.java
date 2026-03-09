package com.example.casino.controller;

import com.example.casino.handler.ISlotmachineHandler;
import com.example.casino.handler.SlotmachineHandler;
import com.example.casino.model.ISlotmachineGameEntity;
import com.example.casino.model.SlotmachineGameEntity;
import com.example.casino.repository.ISlotmachineRepository;
import com.example.casino.response.SlotmachineResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SlotmachineControllerTest {

    @Test
    @DisplayName("communication with banking service is successful")
    void play_Success(){
    }

    @Test
    void play_Fail(){
    }

    @Test
    void readGame() {
        fail();
    }

    @Test
    void readAllGames() {
    }

    @Test
    void readRules() {
    }

    @Test
    void readChances() {
    }

    @Test
    void readGlobalStats() {
    }

    @Test
    void readUserStats() {
    }
}