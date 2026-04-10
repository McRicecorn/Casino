package de.casino.banking_service.stat.Clients;

import de.casino.banking_service.stat.Responses.transactionResponses.GetAllTransactionsTClientResponse;

import de.casino.banking_service.stat.Utility.ErrorWrapper;
import de.casino.banking_service.common.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;


public class TClient implements ITransactionClientStats {

    private final RestTemplate restTemplate;

    public TClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final String baseUrl = "http://localhost:8080/casino/bank/api/transactions/user/";

    @Override
    public Result<Iterable<GetAllTransactionsTClientResponse>, ErrorWrapper> getAllTransactionsById(long userId) {
        try {
            ResponseEntity<GetAllTransactionsTClientResponse[]> response =
                    restTemplate.getForEntity(
                            baseUrl + userId,
                            GetAllTransactionsTClientResponse[].class
                    );

            return Result.success(List.of(response.getBody()));

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Result.failure(ErrorWrapper.USER_WAS_NOT_FOUND);
            }
            return Result.failure(ErrorWrapper.EXTERNAL_SERVICE_ERROR);
        }
    }

    @Override
    public Result<Iterable<GetAllTransactionsTClientResponse>, ErrorWrapper> getAllTransactions() {
        try {
            ResponseEntity<GetAllTransactionsTClientResponse[]> response =
                    restTemplate.getForEntity(
                            "http://localhost:8080/casino/bank/api/transactions",
                            GetAllTransactionsTClientResponse[].class
                    );

            return Result.success(List.of(response.getBody()));

        } catch (Exception e) {
            return Result.failure(ErrorWrapper.EXTERNAL_SERVICE_ERROR);
        }
    }
}




