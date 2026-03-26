package de.casino.banking_service.transaction.handler;

import de.casino.banking_service.transaction.model.ITransactionEntity;
import de.casino.banking_service.transaction.model.TransactionEntity;
import de.casino.banking_service.transaction.modelFactory.ITransactionEntityFactory;
import de.casino.banking_service.transaction.repository.ITransactionRepository;
import de.casino.banking_service.transaction.request.ITransactionRequest;
import de.casino.banking_service.transaction.request.PostTransactionRequest;
import de.casino.banking_service.transaction.request.PutTransactionRequest;
import de.casino.banking_service.transaction.response.ITransactionResponse;
import de.casino.banking_service.transaction.responseFactory.ITransactionResponseFactory;
import de.casino.banking_service.transaction.utility.ErrorResult;
import de.casino.banking_service.transaction.utility.ErrorWrapper;
import de.casino.banking_service.transaction.utility.Result;
import de.casino.banking_service.user.model.IUserEntity;
import de.casino.banking_service.user.model.UserEntity;
import de.casino.banking_service.user.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;

@Service
public class TransactionHandler implements ITransactionHandler {
    private final ITransactionRepository transactionRepository;

    private final ITransactionResponseFactory transactionResponseFactory;

    private final ITransactionEntityFactory transactionEntityFactory;

    private final IUserRepository userRepository;



    @Autowired
    public TransactionHandler(ITransactionRepository transactionRepository,
                              ITransactionResponseFactory responseFactory,
                              ITransactionEntityFactory transactionEntityFactory,
                              IUserRepository userRepository){
        this.transactionRepository =transactionRepository;
        this.transactionResponseFactory= responseFactory;
        this.transactionEntityFactory= transactionEntityFactory;
        this.userRepository= userRepository;

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
        var user = userRepository.findById(userId);
        if (user.isEmpty()){
            return Result.failure(ErrorWrapper.USER_WAS_NOT_FOUND);
        }

         var transactionEntity = transactionEntityFactory.create(transactionRequest.getAmount(), transactionRequest.getInvoicingParty(), user.get());

            if (transactionEntity.isFailure()){
                return Result.failure(transactionEntity.getFailureData().get());
            }

            var entity= (TransactionEntity) transactionEntity.getSuccessData().get();
            transactionRepository.save(entity);

            var response = transactionResponseFactory.createPost(transactionEntity.getSuccessData().get());
            return Result.success(response);

    }

    public Result<ITransactionResponse, ErrorWrapper> updateTransaction(Long id, PutTransactionRequest request){

        var target = findTransactionById(id);
        if (target.isFailure()){
            return Result.failure(target.getFailureData().get());
        }

        var user = userRepository.findById(request.getUserId());
        if (user.isEmpty()){
            return Result.failure(ErrorWrapper.USER_WAS_NOT_FOUND);
        }

        var transaction = target.getSuccessData().get();

        var updateResult = transaction.update(
                request.getAmount(),
                request.getInvoicingParty(),
                user.get()
        );

        if (updateResult.isFailure()){
            return Result.failure(updateResult.getFailureData().get());
        }

        transactionRepository.save(transaction);

        return Result.success(transactionResponseFactory.createPut(transaction));
    }

    public Result<ITransactionResponse, ErrorWrapper> deleteTransaction(Long id){
        var target = findTransactionById(id);
        if (target.isFailure()) {
            return Result.failure(target.getFailureData().get());
        }
        var transaction = target.getSuccessData().get();

        transactionRepository.delete(transaction);
        var response = transactionResponseFactory.createDelete(transaction);

        return Result.success(response);
    }


}
