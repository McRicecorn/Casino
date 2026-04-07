package de.casino.banking_service.transaction.UserClient;

import de.casino.banking_service.transaction.response.userResponse.GetUserClientResponse;
import de.casino.banking_service.transaction.utility.ErrorWrapper;
import de.casino.banking_service.transaction.utility.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class UserClient implements IUserClient{

    private final RestTemplate restTemplate;

    public UserClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String  baseUrl = "http://localhost:8080/casino/bank/api/user/";

    public Result<GetUserClientResponse, ErrorWrapper> getUserById(Long userId) {
        try {
            ResponseEntity<GetUserClientResponse> response = restTemplate.getForEntity(baseUrl + userId, GetUserClientResponse.class);
            return Result.success(response.getBody());


        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Result.failure(ErrorWrapper.USER_WAS_NOT_FOUND);
            }
            return Result.failure(ErrorWrapper.EXTERNAL_SERVICE_ERROR);
        }
    }

    public Result<Void, ErrorWrapper> deposit(Long userId, BigDecimal amount) {
        try {
            String[] parts = amount.toPlainString().split("\\.");

            String integerPart = parts[0];
            String decimalPart = parts.length > 1 ? parts[1] : "00";

            String url = String.format(
                    "http://localhost:8080/casino/bank/api/user/%d/deposit/%s/%s",
                    userId, integerPart, decimalPart
            );

            restTemplate.postForEntity(url, null, Object.class);

            return Result.success(null);

        }
        catch (Exception e) {
            return Result.failure(ErrorWrapper.USER_SERVICE_BAD_RESPONSE);
        }
    }

    public Result<Void, ErrorWrapper> withdraw(Long userId, BigDecimal amount) {
        try {
            String[] parts = amount.toPlainString().split("\\.");

            String integerPart = parts[0];
            String decimalPart = parts.length > 1 ? parts[1] : "00";

            String url = String.format(
                    "http://localhost:8080/casino/bank/api/user/%d/withdraw/%s/%s",
                    userId, integerPart, decimalPart
            );

            restTemplate.postForEntity(url, null, Object.class);

            return Result.success(null);

        }

        catch (Exception e) {
            return Result.failure(ErrorWrapper.USER_SERVICE_BAD_RESPONSE);
        }
    }



}