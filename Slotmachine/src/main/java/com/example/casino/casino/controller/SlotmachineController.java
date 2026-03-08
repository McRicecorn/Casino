package com.example.casino.casino.controller;

import com.example.casino.casino.handler.ISlotmachineHandler;
import com.example.casino.casino.request.SlotmachineRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/casino/slots/api")
@Tag(name = "Slot Machine Management", description = "APIs for managing Slot Games")
public class SlotmachineController {

    private ISlotmachineHandler handler;

    @Autowired
    public SlotmachineController(ISlotmachineHandler handler) {
        this.handler = handler;
    }

//    @Value("${spring.application.name}")
//    private String appName;
//
//    @GetMapping("/start")
//    public String hello(){
//        return "Slotmachine ist bereit. App-Name = " + appName;
//    }

    @Operation(summary = "Play a round", description = "Deduct bet from banking and calculate slot result")
    @PostMapping("/play")
    public ResponseEntity<?> play(@RequestBody SlotmachineRequest request) {
        var result = handler.play(request);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getSuccessData().get());
        } else {
            return ResponseEntity.status(
                    result.getFailureData().get().getHttpStatus())
                    .body(result.getFailureData().get().getMessage());
        }
    }

    @GetMapping("/stat/{game_id}")
    public ResponseEntity<?> readGame(@PathVariable("game_id") long gameId) {
        var result = handler.readGame(gameId);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getSuccessData().get());
        }else{
            return ResponseEntity.status(
                    result.getFailureData().get().getHttpStatus())
                    .body(result.getFailureData().get().getMessage());
        }
    }

    @GetMapping("/stat/games")
    public ResponseEntity<?> readAllGames() {
        var result = handler.readAllGames();
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getSuccessData().get());
        }else{
            return ResponseEntity.status(
                    result.getFailureData().get().getHttpStatus())
                    .body(result.getFailureData().get().getMessage());
        }
    }

    @GetMapping(value = "/info/rules", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> readRules() {
        return ResponseEntity.ok(handler.getRules());
    }

    @GetMapping(value = "/info/chances", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> readChances() {
        return ResponseEntity.ok(handler.calculateChances());
    }

    //TODO GET http://localhost:8081/casino/slots/api/stats/user/{user_id} --> Stats eines bestimmten Users zurückgeben (S. 12 PDF)
    //TODO GET http:\\localhost:8081\casino\slots\api\stats --> Stats aller User gesammelt anzeigen (S. 12 PDF)
}
