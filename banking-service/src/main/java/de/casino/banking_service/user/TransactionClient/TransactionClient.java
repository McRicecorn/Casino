package de.casino.banking_service.user.TransactionClient;

import de.casino.banking_service.transaction.utility.Result;
import de.casino.banking_service.user.Response.TransactionResponse.DeleteAllTransactionsClient;
import de.casino.banking_service.user.Utility.ErrorWrapper;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class TransactionClient implements ITransactionClient{

    private final RestTemplate restTemplate;

    public TransactionClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }



    public Result<DeleteAllTransactionsClient, ErrorWrapper> deleteTransactionsByUserId(Long userId) {
        try {
            String url = String.format("http://localhost:8080/casino/bank/api/transaction/user/%d", userId);
            DeleteAllTransactionsClient response = restTemplate.postForObject(url, null, DeleteAllTransactionsClient.class);
            return Result.success(response);
        } catch (Exception e) {
            return Result.failure(ErrorWrapper.EXTERNAL_SERVICE_ERROR);
        }
    }

}
