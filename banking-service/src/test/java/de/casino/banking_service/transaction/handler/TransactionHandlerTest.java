package de.casino.banking_service.transaction.handler;

import de.casino.banking_service.transaction.model.TransactionEntity;
import de.casino.banking_service.transaction.modelFactory.ITransactionEntityFactory;
import de.casino.banking_service.transaction.repository.ITransactionRepository;
import de.casino.banking_service.transaction.request.ITransactionRequest;
import de.casino.banking_service.transaction.response.ITransactionResponse;
import de.casino.banking_service.transaction.responseFactory.ITransactionResponseFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TransactionHandlerTest {

    @Mock
    private ITransactionRepository transactionRepository;

    @Mock
    private ITransactionEntityFactory transactionEntityFactory;

    @Mock
    private ITransactionResponseFactory transactionResponseFactory;

    @Mock
    private ITransactionRequest transactionRequest;

    @Mock
    private ITransactionResponse transactionResponse;

    @InjectMocks
    private TransactionHandler transactionHandler;



    @Nested
    class GetAllTransactionsTests{

        @Test
        public void


    }

    @Nested
    class GetAllTransactionsFromUserTests{
        
    }

    @Nested
    class createTransactionTests{

    }

    @Nested
    class updateTransactionTests{

    }

    @Nested
    class deleteTransactionTests{

    }



}