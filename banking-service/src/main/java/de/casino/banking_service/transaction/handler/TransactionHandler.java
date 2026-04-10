package de.casino.banking_service.transaction.handler;

import de.casino.banking_service.common.Games;
import de.casino.banking_service.common.Result;
import de.casino.banking_service.transaction.UserClient.IUserClient;
import de.casino.banking_service.transaction.model.ITransactionEntity;
import de.casino.banking_service.transaction.model.TransactionEntity;
import de.casino.banking_service.transaction.modelFactory.ITransactionEntityFactory;
import de.casino.banking_service.transaction.repository.ITransactionRepository;

import de.casino.banking_service.transaction.request.PostTransactionRequest;
import de.casino.banking_service.transaction.request.PutTransactionRequest;
import de.casino.banking_service.transaction.response.transactionResponse.ITransactionResponse;
import de.casino.banking_service.transaction.responseFactory.ITransactionResponseFactory;

import de.casino.banking_service.transaction.utility.ErrorWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionHandler implements ITransactionHandler {
    private final ITransactionRepository transactionRepository;

    private final ITransactionResponseFactory transactionResponseFactory;

    private final ITransactionEntityFactory transactionEntityFactory;

    private final IUserClient userClient;



    @Autowired
    public TransactionHandler(ITransactionRepository transactionRepository,
                              ITransactionResponseFactory responseFactory,
                              ITransactionEntityFactory transactionEntityFactory,
                              IUserClient userClient){
        this.transactionRepository =transactionRepository;
        this.transactionResponseFactory= responseFactory;
        this.transactionEntityFactory= transactionEntityFactory;
        this.userClient = userClient;
    }

    private Result<TransactionEntity, ErrorWrapper> findTransactionById(Long id){
        var result = transactionRepository.findById(id);
        if (result.isPresent()){
            return Result.success(result.get());
        }
        return Result.failure(ErrorWrapper.TRANSACTION_WAS_NOT_FOUND);
    }

    public Result<Iterable<ITransactionResponse>, ErrorWrapper> getAllTransactions() {
        var result = transactionRepository.findAll();
        var finalResult = new ArrayList<ITransactionResponse>();
        for (TransactionEntity transactionEntity : result) {
            var response = transactionResponseFactory.createGetAll(transactionEntity);
            finalResult.add(response);
        }
        return Result.success(finalResult);

    }

    public Result<Iterable<ITransactionResponse>, ErrorWrapper> getTransactionsByUserId(Long id){
        var transactions = transactionRepository.findAllByUserId(id);
        var userTransactions = new ArrayList<ITransactionResponse>();

        for (ITransactionEntity transactionEntity : transactions){
            var response = transactionResponseFactory.createGetByUser(transactionEntity);
            userTransactions.add(response);
        }
        return Result.success(userTransactions);
    }


    public Result<ITransactionResponse, ErrorWrapper> createTransaction(PostTransactionRequest transactionRequest, long userId){
        var userResult = userClient.getUserById(userId);
        if (userResult.isFailure()) {
            return Result.failure(userResult.getFailureData().get());
        }

        var userDto = userResult.getSuccessData().get();

        var transactionEntity = transactionEntityFactory.create(
                transactionRequest.getAmount(),
                transactionRequest.getInvoicingParty(),
                userDto.getId());

        if (transactionEntity.isFailure()){
            return Result.failure(transactionEntity.getFailureData().get());
        }

        var amount = transactionRequest.getAmount();
        Result<Void, ErrorWrapper> moneyResult;

        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            moneyResult = userClient.deposit(userId, amount);
        } else {
            moneyResult = userClient.withdraw(userId, amount.abs());
        }

        if (moneyResult.isFailure()) {
            return Result.failure(moneyResult.getFailureData().get());
        }


        var entity= (TransactionEntity) transactionEntity.getSuccessData().get();
        transactionRepository.save(entity);

        var response = transactionResponseFactory.createPost(entity);
        return Result.success(response);

    }

    public Result<ITransactionResponse, ErrorWrapper> updateTransaction(Long id, PutTransactionRequest request){

        var target = findTransactionById(id);
        if (target.isFailure()){
            return Result.failure(target.getFailureData().get());
        }

        var transaction = target.getSuccessData().get();

        var userResult = userClient.getUserById(request.getUserId());
        if (userResult.isFailure()) {
            return Result.failure(userResult.getFailureData().get());
        }

        var oldAmount = transaction.getAmount();
        var newAmount = request.getAmount();
        var difference = newAmount.subtract(oldAmount).abs();


        Result<Void, ErrorWrapper> moneyResult;


        if (oldAmount.compareTo(newAmount) > 0) {
            moneyResult = userClient.withdraw(request.getUserId(), difference);
        } else if (oldAmount.compareTo(newAmount) < 0) {
            moneyResult = userClient.deposit(request.getUserId(), difference);
        } else {
            moneyResult = Result.success(null);
        }

        if (moneyResult.isFailure()) {
            return Result.failure(moneyResult.getFailureData().get());
        }

        var updateResult = transaction.update(
                newAmount,
                request.getInvoicingParty()
        );

        if (updateResult.isFailure()){
            return Result.failure(updateResult.getFailureData().get());
        }

        transactionRepository.save(transaction);

        return Result.success(transactionResponseFactory.createPut(transaction));
    }

    //todo: write test for money transfer
    //possbily split the money transfer logic into a separate method to make it easier to test and reuse in both create and update methods
    public Result<ITransactionResponse, ErrorWrapper> deleteTransaction(Long id){
        var target = findTransactionById(id);
        if (target.isFailure()) {
            return Result.failure(target.getFailureData().get());
        }
        var transaction = target.getSuccessData().get();

        var userResult = userClient.getUserById(transaction.getUserId());
        if (userResult.isFailure()) {
            return Result.failure(userResult.getFailureData().get());
        }

            var amount = transaction.getAmount();
            var amountPositive = amount.abs();
            Result<Void, ErrorWrapper> moneyResult;

            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                moneyResult = userClient.withdraw(transaction.getUserId(), amountPositive);
            } else {
                moneyResult = userClient.deposit(transaction.getUserId(), amountPositive);
            }

            if (moneyResult.isFailure()) {
                return Result.failure(moneyResult.getFailureData().get());
            }



        transactionRepository.delete(transaction);
        var response = transactionResponseFactory.createDelete(transaction);

        return Result.success(response);
    }

    public Result<ITransactionResponse, ErrorWrapper> deleteTransactionsByUserId(Long userId) {

        int numberDeleted = 0;
        List<Games> games = new ArrayList<>();
        BigDecimal totalAmountGained = BigDecimal.ZERO;
        BigDecimal totalAmountLost = BigDecimal.ZERO;

        var transactions = transactionRepository.findAllByUserId(userId);

        for (ITransactionEntity transaction : transactions) {
            var amount = transaction.getAmount();
            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                totalAmountGained = totalAmountGained.add(amount);
            } else {
                totalAmountLost = totalAmountLost.add(amount.abs());
            }
            games.add(transaction.getInvoicingParty());
            transactionRepository.delete((TransactionEntity) transaction);
            numberDeleted++;
        }

        var response = transactionResponseFactory.createDeleteAll(numberDeleted, totalAmountLost, totalAmountGained, games);

        return Result.success(response);


    }
}
