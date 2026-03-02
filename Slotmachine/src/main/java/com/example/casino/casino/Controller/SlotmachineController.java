package com.example.casino.casino.Controller;

import com.example.casino.casino.Handler.ISlotmachineHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/casino/slotmachine/api")
public class SlotmachineController {

    private ISlotmachineHandler handler;

    @Autowired
    public SlotmachineController(ISlotmachineHandler handler) {
        this.handler = handler;
    }

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping("/start")
    public String hello(){
        return "Slotmachine ist bereit. App-Name = " + appName;
    }

    @GetMapping("/info/rules")
    public String rules(){
        return "Drei gleiche Symbole gewinnen!";
    }
}
