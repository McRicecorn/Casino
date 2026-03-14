package de.casino.banking_service.transaction.handler;

import de.casino.banking_service.transaction.model.TransactionEntity;
import de.casino.banking_service.transaction.modelFactory.ITransactionEntityFactory;
import de.casino.banking_service.transaction.repository.TransactionRepository;
import de.casino.banking_service.transaction.request.ITransactionRequest;
import de.casino.banking_service.transaction.response.ITransactionResponse;
import de.casino.banking_service.transaction.responseFactory.ITransactionResponseFactory;
import de.casino.banking_service.transaction.utility.ErrorResult;
import de.casino.banking_service.transaction.utility.ErrorWrapper;
import de.casino.banking_service.transaction.utility.Result;

import java.util.ArrayList;
import java.util.List;


public class TransactionHandler implements ITransactionHandler {
    private final TransactionRepository transactionRepository;

    private ITransactionResponseFactory transactionResponseFactory;

    private ITransactionEntityFactory transactionEntityFactory;

    public TransactionHandler(TransactionRepository transactionRepository){
        this.transactionRepository=transactionRepository;
    }
    @Override
    public Result<Iterable<ITransactionResponse>, ErrorWrapper> getAllTransactions() {
        var result = transactionRepository.findAll();
        var finalResult = new ArrayList<ITransactionResponse>();
        for (TransactionEntity transactionEntity : result) {
            var response = transactionResponseFactory.create(transactionEntity);
            finalResult.add(response);
        }
        return Result.success(finalResult);

    }

    @Override
    public Result<ITransactionResponse, ErrorWrapper> createTransactionEntity(ITransactionRequest tranctionEntity){

        var transactionEntityModel = transactionEntityFactory.create(tranctionEntity.getAmount(), tranctionEntity.getInvoicingParty(), tranctionEntity.getUser());

        if (transactionEntityModel.isFailure()){
            return Result.failure(transactionEntityModel.getFailureData().get());
        }
        transactionRepository.save((TransactionEntity)
          transactionEntityModel.getSuccessData().get());

        var respnse = transactionResponseFactory.create(transactionEntityModel.getSuccessData().get());
        return Result.success(respnse);
    }

    @Override
    public Result<ITransactionResponse, ErrorWrapper> updateTransactionEntity(Long id, ITransactionRequest transactionentity){
        var target = transactionRepository.findById(id);
        if (target.isEmpty()){
            return Result.failure(ErrorWrapper.TRANSACTION_WAS_NOT_FOUND);
        }

        var result = target.get().setAmount(transactionentity.getAmount());
        if (result.isFailure()) {

            return Result.failure(result.getFailureData().get());
        }
        result=target.get().setUser(transactionentity.getUser());
        if (result.isFailure()) {

            return Result.failure(result.getFailureData().get());
        }
        result=target.get().setInvoicingParty(transactionentity.getInvoicingParty());
        if (result.isFailure()) {

            return Result.failure(result.getFailureData().get());
        }
        transactionRepository.save(target.get());
        var response = transactionResponseFactory.create(target.get());
        return Result.success(response);
    }

    @Override
    public ErrorResult<ErrorWrapper> deleteTransactionEntity(Long id){
        var target = transactionRepository.findById(id);
        if (target.isEmpty()){
            return ErrorResult.failure(ErrorWrapper.TRANSACTION_WAS_NOT_FOUND);
        }
        else
            transactionRepository.delete(target.get());
        return ErrorResult.success();
    }

    @Override
    public List<TransactionEntity> findByUserId(Long id){
        return transactionRepository.findByUserId(id);

    }





}
