package com.example.casino.controller;

import com.example.casino.handler.ISlotmachineHandler;
import com.example.casino.request.SlotmachineRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
        var result = handler.getRules();

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getSuccessData().get());
        }else{
            return ResponseEntity.status(
                    result.getFailureData().get().getHttpStatus())
                    .body(result.getFailureData().get().getMessage());
        }
    }

    @GetMapping(value = "/info/chances", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> readChances() {
        var result = handler.calculateChances();

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getSuccessData().get());
        }else{
            return ResponseEntity.status(
                    result.getFailureData().get().getHttpStatus())
                    .body(result.getFailureData().get().getMessage());
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> readGlobalStats() {
        var result = handler.readGlobalStats();
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getSuccessData().get());
        }else{
            return ResponseEntity.status(
                    result.getFailureData().get().getHttpStatus())
                    .body(result.getFailureData().get().getMessage());
        }
    }

    @GetMapping("/stats/user/{user_id}")
    public ResponseEntity<?> readUserStats(@PathVariable("user_id") long userId) {
        var result = handler.readUserStats(userId);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getSuccessData().get());
        }else{
            return ResponseEntity.status(
                    result.getFailureData().get().getHttpStatus())
                    .body(result.getFailureData().get().getMessage());
        }
    }

    //TODO Geldbeträge auf BigDecimal umstellen!!!
    //TODO Tests schreiben !!!
    //TODO Fehlermeldungen nach Test run checken 

}
