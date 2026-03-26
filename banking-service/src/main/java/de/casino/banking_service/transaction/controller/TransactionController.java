package de.casino.banking_service.transaction.controller;

import de.casino.banking_service.transaction.handler.ITransactionHandler;
import de.casino.banking_service.transaction.request.PostTransactionRequest;
import de.casino.banking_service.transaction.request.PutTransactionRequest;
import de.casino.banking_service.transaction.request.TransactionRequest;
import de.casino.banking_service.transaction.response.PostTransactionResponse;
import de.casino.banking_service.user.handler.UserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/casino/bank/api")
//@Tag(name = "Circle Management", description = "APIs for managing Trasnaction of users")
public class TransactionController {

    private final ITransactionHandler transactionHandler;
    @Autowired

    public TransactionController(ITransactionHandler transactionHandler) {
        this.transactionHandler= transactionHandler;
    }


    @GetMapping("/transactions")
    public ResponseEntity<?> getAllTransactions() {
        var result = transactionHandler.getAllTransactions();

        return ResponseEntity.ok(result.getSuccessData().get());
    }

    @GetMapping("/transactions/user/{id}")
    public ResponseEntity<?> getAllTransactionsByUserId(@PathVariable Long id) {
        var result = transactionHandler.getTransactionsByUserId(id);

        return ResponseEntity.ok(result.getSuccessData().get());
    }

    @PostMapping("/transaction/user/{user_id}")
    public ResponseEntity<?> createTransaction(@PathVariable Long user_id,
                                               @RequestBody @Valid PostTransactionRequest request) {
        var result = transactionHandler.createTransaction(request, user_id);

        if (result.isSuccess()){
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(result.getSuccessData().get());
        }
        return ResponseEntity.status(
                result.getFailureData().get().getHttpStatus())
                .body(result.getFailureData().get().getMessage());

    }

    @PutMapping("/transaction/{transaction_id}")
    public ResponseEntity<?> updateTransaction(
            @PathVariable Long transaction_id,
            @RequestBody @Valid PutTransactionRequest request){

        var result = transactionHandler.updateTransaction(transaction_id, request);
        if (result.isSuccess()){
            return ResponseEntity.ok(result.getSuccessData().get());
        }
        return ResponseEntity.status(
                result.getFailureData().get().getHttpStatus())
                .body(result.getFailureData().get().getMessage());

    }

    @DeleteMapping("/transaction/{transaction_id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long transaction_id) {

        var result = transactionHandler.deleteTransaction(transaction_id);

        if (result.isSuccess()){
            return ResponseEntity.ok(result.getSuccessData().get());
        }
        return ResponseEntity.status(
                result.getFailureData().get().getHttpStatus())
                .body(result.getFailureData().get().getMessage());

    }


}



