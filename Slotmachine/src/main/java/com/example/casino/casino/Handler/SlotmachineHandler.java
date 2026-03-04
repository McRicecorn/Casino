package com.example.casino.casino.Handler;

import com.example.casino.casino.Factory.ISlotmachineFactory;
import com.example.casino.casino.Repository.ISlotmachineRepository;
import com.example.casino.casino.Request.ISlotmachineRequest;
import com.example.casino.casino.Response.ISlotmachineResponse;
import com.example.casino.casino.ResponseFactory.ISlotmachineResponseFactory;
import com.example.casino.casino.Utility.ErrorWrapper;
import com.example.casino.casino.Utility.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SlotmachineHandler implements ISlotmachineHandler {

    private final ISlotmachineRepository repository;
    private final ISlotmachineFactory modelFactory;
    private final ISlotmachineResponseFactory responseFactory;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${casino.banking.url}") // in application.properties
    private String bankingUrl;


    @Autowired
    public SlotmachineHandler(ISlotmachineRepository repository, ISlotmachineFactory modelFactory, ISlotmachineResponseFactory responseFactory) {#
        this.repository = repository;
        this.modelFactory = modelFactory;
        this.responseFactory = responseFactory;
    }

    @Override
    public Result<ISlotmachineResponse, ErrorWrapper> play(ISlotmachineRequest request) {
        
    }

    public void updateBalance(String userID, double amount){
        Map<String, Object> request = new HashMap<>();
        request.put("invoicing_party", "Slotmachine");
        request.put("amount", amount);

        restTemplate.postForObject(bankingUrl + userID, request, Object.class);
    }
}
