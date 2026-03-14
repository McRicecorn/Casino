package de.casino.banking_service.transaction.controller;

import de.casino.banking_service.transaction.handler.ITransactionHandler;
import de.casino.banking_service.transaction.handler.TransactionHandler;
import de.casino.banking_service.transaction.model.TransactionEntity;
import de.casino.banking_service.transaction.request.TransactionRequest;
import de.casino.banking_service.transaction.response.TransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/casino/bank/api")
public class TransactionController {

    private final ITransactionHandler transactionHandler;

    @Autowired
    public TransactionController(TransactionHandler transactionHandler) {
        this.transactionHandler= transactionHandler;
    }

    @Operation (summary = "Retrieve all Transactions", description = "Fetches a list of all Transactions done")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponse.class)))
    })
    @GetMapping("/transactions")
    public ResponseEntity<?> getAllTransactions() {
        var result = transactionHandler.getAllTransactions();
        if (result.isSuccess()){
            return ResponseEntity.ok(result.getSuccessData().get());

        }
       else
           return ResponseEntity.status(result.getFailureData().get().getHttpStatus()).body(result.getFailureData().get().getMessage());

    }

   @Operation (summary = "Retrieves transactions of a user", description = "Fetches all Transactions done by a specific user")
   @ApiResponses({
     @ApiResponse(responseCode = "200", description = "Found users transactions.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponse.class))),
           @ApiResponse(responseCode = "404", description = "User not found.", content = @Content)
   })
   @GetMapping("/transactions/user/{id}")
   public ResponseEntity<?> getTransactionbyUserId(@PathVariable Long id){
        var result = transactionHandler.findByUserId(id);

        if (1==1)
        return ResponseEntity.ok(transactionHandler.findByUserId(id));
        else
            return null;
   }

   @Operation (summary ="Delete Transaction by ID", description = "Removes a transaction from the Database using its identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Deleted Transaction successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @DeleteMapping("/transaction/{transaction_id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id) {
        var result=transactionHandler.deleteTransactionEntity(id);
        if (result.isSuccess()){

            return ResponseEntity.ok(null);
        }
        else
            return ResponseEntity.status(
                    result.getFailureData().get().getHttpStatus())
                    .body(result.getFailureData().get().getMessage());

   }

   @Operation (summary = "Create new Transaction", description = "Creates a new Transaction based on given Informations")
   @ApiResponses({
   @ApiResponse(responseCode = "201", description = "Transaction has been successful"),
    @ApiResponse(responseCode = "400", description = "Requested Transaction was faulty or missing"),
    @ApiResponse(responseCode = "404", description = "User was not found")
   })
    @PostMapping("/transaction/user/{userId")
    public ResponseEntity<?> createTransactionEntity(@RequestBody TransactionRequest request, @PathVariable Long userId){
        var result = transactionHandler.createTransactionEntity(request);
        if (result.isSuccess()) {

            return ResponseEntity.ok(result.getSuccessData().get());
        }
        else
            return ResponseEntity.status(result.getFailureData().get().getHttpStatus()).body(result.getFailureData().get().getMessage());
   }

}



