package de.casino.banking_service.stat.StatController;


import de.casino.banking_service.stat.StatHandler.StatHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/casino/bank/api")
public class StatController {

    private final StatHandler statHandler;

    public StatController(StatHandler statHandler) {
        this.statHandler = statHandler;
    }

    @GetMapping("/stats/user/{userId}")
    public ResponseEntity<?> getUserStats(@PathVariable Long userId) {

        var result = statHandler.getGlobalStatByUserId(userId);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getSuccessData().get());
        } else {
            return ResponseEntity
                    .status(result.getFailureData().get().getHttpStatus())
                    .body(result.getFailureData().get().getMessage());
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getGlobalStats() {

        var result = statHandler.getGlobalStat();

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getSuccessData().get());
        } else {
            return ResponseEntity
                    .status(result.getFailureData().get().getHttpStatus())
                    .body(result.getFailureData().get().getMessage());
        }
    }

    @GetMapping("/{game}/stats/user/{userId}")
    public ResponseEntity<?>  getGameStatsByUserId(@PathVariable String game, @PathVariable Long userId) {

        var result = statHandler.getGameStats(game, userId);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getSuccessData().get());
        } else {
            return ResponseEntity
                    .status(result.getFailureData().get().getHttpStatus())
                    .body(result.getFailureData().get().getMessage());
        }

    }

    @GetMapping("/{game}/stats")
    public ResponseEntity<?>  getGameStats(@PathVariable String game) {

        var result = statHandler.getGameStats(game);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getSuccessData().get());
        } else {
            return ResponseEntity
                    .status(result.getFailureData().get().getHttpStatus())
                    .body(result.getFailureData().get().getMessage());
        }
    }
}