package de.casino.banking_service.transaction.handler;

import de.casino.banking_service.transaction.model.TransactionEntity;
import de.casino.banking_service.transaction.modelFactory.ITransactionEntityFactory;
import de.casino.banking_service.transaction.repository.ITransactionRepository;
import de.casino.banking_service.transaction.request.ITransactionRequest;
import de.casino.banking_service.transaction.response.ITransactionResponse;
import de.casino.banking_service.transaction.responseFactory.ITransactionResponseFactory;
import de.casino.banking_service.transaction.utility.ErrorResult;
import de.casino.banking_service.transaction.utility.ErrorWrapper;
import de.casino.banking_service.transaction.utility.Result;
import de.casino.banking_service.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;


public class TransactionHandler implements ITransactionHandler {
    private final ITransactionRepository transactionRepository;

    private ITransactionResponseFactory transactionResponseFactory;

    private ITransactionEntityFactory transactionEntityFactory;

    private UserRepository userRepository;



    @Autowired
    public TransactionHandler(ITransactionRepository transactionRepository,
                              ITransactionResponseFactory responseFactory,
                              ITransactionEntityFactory transactionEntityFactory,
                              UserRepository userRepository){
        this.transactionRepository =transactionRepository;
        this.transactionResponseFactory= responseFactory;
        this.transactionEntityFactory= transactionEntityFactory;
        this.userRepository= userRepository;

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
    public Result<ITransactionResponse, ErrorWrapper> createTransactionEntity(ITransactionRequest tranctionEntity, Long userId){

        var userexists = userRepository.findById(userId);

        if (userexists.isEmpty()){
            return Result.failure(ErrorWrapper.USER_WAS_NOT_FOUND);
        }

        var transactionEntityModel = transactionEntityFactory.create(tranctionEntity.getAmount(), tranctionEntity.getInvoicingParty(), tranctionEntity.getUserId());

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
        result=target.get().setUserId(transactionentity.getUserId());
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
    public Result<Iterable<ITransactionResponse>, ErrorWrapper> findByUserId(Long id){
        var userexists= userRepository.findById(id);
        if (userexists.isEmpty()){
            return Result.failure(ErrorWrapper.USER_WAS_NOT_FOUND);
        }
        var result = transactionRepository.findByUserId(id);
        var finalResult = new ArrayList<ITransactionResponse>();
        for (TransactionEntity transactionEntity : result){
            var responese = transactionResponseFactory.create(transactionEntity);
            finalResult.add(responese);
        }

        return Result.success(finalResult);

    }





}
