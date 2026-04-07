package de.casino.banking_service.user.TransactionClient;

import de.casino.banking_service.transaction.utility.Result;
import de.casino.banking_service.user.Response.TransactionResponse.DeleteAllTransactionsClient;
import de.casino.banking_service.user.Utility.ErrorWrapper;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
            String url = String.format("http://localhost:8080/casino/bank/api/transactions/delete/user/%d", userId);


            ResponseEntity<DeleteAllTransactionsClient> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.DELETE,
                            null,
                            DeleteAllTransactionsClient.class
                    );

            DeleteAllTransactionsClient result = response.getBody();

            if (!response.getStatusCode().is2xxSuccessful()) {
                return Result.failure(ErrorWrapper.EXTERNAL_SERVICE_ERROR);
            }if (result == null) {
                return Result.failure(ErrorWrapper.USER_MODEL_INVALID_AMOUNT);
            }
            return Result.success(result);
        } catch (Exception e) {
            return Result.failure(ErrorWrapper.EXTERNAL_SERVICE_ERROR);
        }
    }

}
