package de.casino.banking_service.stat.StatHandler;

import de.casino.banking_service.stat.Clients.ITransactionClientStats;

import de.casino.banking_service.stat.Responses.statResponses.GetGameStatsResponse;
import de.casino.banking_service.stat.Responses.statResponses.GetGlobalStatResponse;
import de.casino.banking_service.stat.Utility.ErrorWrapper;
import de.casino.banking_service.common.Games;
import de.casino.banking_service.common.Result;
import de.casino.banking_service.stat.responseFactory.responseFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StatHandler implements IStathandler{

    private final ITransactionClientStats tClient;
    private final responseFactory responseFactory;

    public StatHandler(
                       ITransactionClientStats tClient,
                       responseFactory responseFactory) {
        this.tClient = tClient;
        this.responseFactory = responseFactory;
    }

    public Result<GetGlobalStatResponse, ErrorWrapper> getGlobalStatByUserId(Long userId){

        BigDecimal totalGained = BigDecimal.ZERO;
        BigDecimal totalLost = BigDecimal.ZERO;
        int totalTransactions = 0;

        var transactionResult = tClient.getAllTransactionsById(userId);

        if (transactionResult.isFailure()) {
            return Result.failure(transactionResult.getFailureData().get());
        }

        var transactions = transactionResult.getSuccessData().get();

        for (var transaction : transactions) {
            BigDecimal amount = transaction.getAmount();

            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                totalGained = totalGained.add(amount);
            } else {
                totalLost = totalLost.add(amount.abs());
            }

            totalTransactions++;
        }

        BigDecimal net = totalGained.subtract(totalLost);

        var response = responseFactory.createGetGlobalStatResponse(
                totalGained,
                totalLost,
                totalTransactions,
                net
        );

        return Result.success(response);
    }

    public Result<GetGlobalStatResponse, ErrorWrapper> getGlobalStat() {

        BigDecimal totalGained = BigDecimal.ZERO;
        BigDecimal totalLost = BigDecimal.ZERO;
        int totalTransactions = 0;

        var transactionResult = tClient.getAllTransactions();

        if (transactionResult.isFailure()) {
            return Result.failure(transactionResult.getFailureData().get());
        }

        var transactions = transactionResult.getSuccessData().get();

        for (var transaction : transactions) {
            BigDecimal amount = transaction.getAmount();

            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                totalGained = totalGained.add(amount);
            } else {
                totalLost = totalLost.add(amount.abs());
            }

            totalTransactions++;
        }

        BigDecimal net = totalGained.subtract(totalLost);

        var response = responseFactory.createGetGlobalStatResponse(
                totalGained,
                totalLost,
                totalTransactions,
                net
        );

        return Result.success(response);
    }

    public Result<GetGameStatsResponse, ErrorWrapper> getGameStats(String gameName, Long userId) {

        int totalGamesPlayed = 0;
        int totalGamesWon = 0;
        int totalGamesLost = 0;
        BigDecimal totalGain = BigDecimal.ZERO;
        BigDecimal totalLoss = BigDecimal.ZERO;
        Games game;

        try {
            game = Games.valueOf(gameName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Result.failure(ErrorWrapper.INVOICING_PARTY_DOES_NOT_EXIST);
        }
        var transactionResult = tClient.getAllTransactionsById(userId);

        if (transactionResult.isFailure()) {
            return Result.failure(transactionResult.getFailureData().get());
        }

        var transactions = transactionResult.getSuccessData().get();

        for (var transaction : transactions) {
            if (transaction.getInvoicingParty().toString().equals(gameName)) {
                BigDecimal amount = transaction.getAmount();

                if (amount.compareTo(BigDecimal.ZERO) > 0) {
                    totalGain= totalGain.add(amount);
                    totalGamesWon++;
                } else {
                    totalLoss = totalLoss.add(amount.abs());
                    totalGamesLost++;
                }

                totalGamesPlayed++;
            }
        }



        var response = responseFactory.createGetGameStatsResponse(
                totalGamesPlayed,
                totalGamesWon,
                totalGamesLost,
                totalGain,
                totalLoss,
                game

        );

        return Result.success(response);




    }

    public Result<GetGameStatsResponse, ErrorWrapper> getGameStats(String gameName) {

        int totalGamesPlayed = 0;
        int totalGamesWon = 0;
        int totalGamesLost = 0;
        BigDecimal totalGain = BigDecimal.ZERO;
        BigDecimal totalLoss = BigDecimal.ZERO;
        Games game;

        try {
            game = Games.valueOf(gameName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Result.failure(ErrorWrapper.INVOICING_PARTY_DOES_NOT_EXIST);
        }
        var transactionResult = tClient.getAllTransactions();

        if (transactionResult.isFailure()) {
            return Result.failure(transactionResult.getFailureData().get());
        }

        var transactions = transactionResult.getSuccessData().get();

        for (var transaction : transactions) {
            if (transaction.getInvoicingParty().toString().equals(gameName)) {
                BigDecimal amount = transaction.getAmount();

                if (amount.compareTo(BigDecimal.ZERO) > 0) {
                    totalGain= totalGain.add(amount);
                    totalGamesWon++;
                } else {
                    totalLoss = totalLoss.add(amount.abs());
                    totalGamesLost++;
                }

                totalGamesPlayed++;
            }
        }



        var response = responseFactory.createGetGameStatsResponse(
                totalGamesPlayed,
                totalGamesWon,
                totalGamesLost,
                totalGain,
                totalLoss,
                game

        );

        return Result.success(response);




    }


}
