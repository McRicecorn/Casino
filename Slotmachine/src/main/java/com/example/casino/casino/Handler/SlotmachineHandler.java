package com.example.casino.casino.Handler;

import com.example.casino.casino.Repository.ISlotmachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SlotmachineHandler implements ISlotmachineHandler {

    private ISlotmachineRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${casino.banking.url}")
    private String bankingURL;

    public void updateBalance(String userID, double amount){
        Map<String, Object> request = new HashMap<>();
        request.put("invoicing_party", "Slotmachine");
        request.put("amount", amount);

        restTemplate.postForObject(bankingURL + userID, request, Object.class);
    }
}
