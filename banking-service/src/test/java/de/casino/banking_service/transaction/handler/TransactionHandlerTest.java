package de.casino.banking_service.transaction.handler;

import de.casino.banking_service.transaction.UserClient.IUserClient;
import de.casino.banking_service.transaction.model.TransactionEntity;
import de.casino.banking_service.transaction.modelFactory.ITransactionEntityFactory;
import de.casino.banking_service.transaction.repository.ITransactionRepository;
import de.casino.banking_service.transaction.request.ITransactionRequest;
import de.casino.banking_service.transaction.request.PostTransactionRequest;
import de.casino.banking_service.transaction.request.PutTransactionRequest;
import de.casino.banking_service.transaction.response.transactionResponse.ITransactionResponse;
import de.casino.banking_service.transaction.response.userResponse.GetUserClientResponse;
import de.casino.banking_service.transaction.responseFactory.ITransactionResponseFactory;
import de.casino.banking_service.transaction.utility.ErrorResult;
import de.casino.banking_service.transaction.utility.ErrorWrapper;
import de.casino.banking_service.transaction.utility.Games;
import de.casino.banking_service.transaction.utility.Result;
import de.casino.banking_service.user.model.UserEntity;
import de.casino.banking_service.user.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionHandlerTest {

    @Mock
    private ITransactionRepository transactionRepository;

    @Mock
    private ITransactionEntityFactory transactionEntityFactory;

    @Mock
    private IUserClient userClient;

    @Mock
    private ITransactionResponseFactory transactionResponseFactory;

    @Mock
    private ITransactionRequest transactionRequest;

    @Mock
    private ITransactionResponse transactionResponse;

    @InjectMocks
    private TransactionHandler transactionHandler;

    private GetUserClientResponse mockUser(Long id) {
        GetUserClientResponse user = mock(GetUserClientResponse.class);
        when(user.getId()).thenReturn(id);
        return user;
    }



    @Nested
    class GetAllTransactionsTests{

        @Test
        public void getAllTransactions_ExistingUser_shouldSucceed(){
            TransactionEntity transactionEntity1 = mock(TransactionEntity.class);
            TransactionEntity transactionEntity2 = mock(TransactionEntity.class);
            when(transactionRepository.findAll()).thenReturn(List.of(transactionEntity1, transactionEntity2));
            ITransactionResponse transactionResponse1 = mock(ITransactionResponse.class);
            ITransactionResponse transactionResponse2 = mock(ITransactionResponse.class);
            when(transactionResponseFactory.createGetAll(transactionEntity1)).thenReturn(transactionResponse1);
            when(transactionResponseFactory.createGetAll(transactionEntity2)).thenReturn(transactionResponse2);

            var result = transactionHandler.getAllTransactions();

            List<ITransactionResponse> responses = new ArrayList<>();
            result.getSuccessData().get().forEach(responses::add);

            assertTrue(result.isSuccess());
            assertEquals(2, responses.size());
            assertTrue(responses.contains(transactionResponse1));
            assertTrue(responses.contains(transactionResponse2));
            verify(transactionRepository, times(1)).findAll();
            verify(transactionResponseFactory, times(1)).createGetAll(transactionEntity1);
            verify(transactionResponseFactory, times(1)).createGetAll(transactionEntity2);

        }

        @Test
        public void getAllTransactions_NoTransactions_shouldSucceed(){
            when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

            var result = transactionHandler.getAllTransactions();

            assertTrue(result.isSuccess());
            assertTrue(result.getSuccessData().get().iterator().hasNext() == false);
            verify(transactionRepository, times(1)).findAll();

        }

        @Test
        public void getAllTransactions_NonExistingUser_shouldFail(){

        }


    }

    @Nested
    class GetAllTransactionsFromUserTests{

        @Test
        void getTransactionsByUserId_existingTransactions_shouldSucceed() {
            long userId = 1L;

            TransactionEntity entity1 = mock(TransactionEntity.class);
            TransactionEntity entity2 = mock(TransactionEntity.class);

            ITransactionResponse response1 = mock(ITransactionResponse.class);
            ITransactionResponse response2 = mock(ITransactionResponse.class);

            when(transactionRepository.findAllByUserId(userId))
                    .thenReturn(List.of(entity1, entity2));

            when(transactionResponseFactory.createGetByUser(entity1))
                    .thenReturn(response1);
            when(transactionResponseFactory.createGetByUser(entity2))
                    .thenReturn(response2);

            var result = transactionHandler.getTransactionsByUserId(userId);

            List<ITransactionResponse> responses = new ArrayList<>();
            result.getSuccessData().get().forEach(responses::add);

            assertTrue(result.isSuccess());
            assertEquals(2, responses.size());
            assertTrue(responses.contains(response1));
            assertTrue(responses.contains(response2));

            verify(transactionRepository).findAllByUserId(userId);
        }

        @Test
        void getTransactionsByUserId_noTransactions_shouldReturnEmptyList() {
            long userId = 1L;

            when(transactionRepository.findAllByUserId(userId))
                    .thenReturn(Collections.emptyList());

            var result = transactionHandler.getTransactionsByUserId(userId);

            assertTrue(result.isSuccess());
            assertFalse(result.getSuccessData().get().iterator().hasNext());
        }

    }

    @Nested
    class createTransactionTests{

        private PostTransactionRequest request;
        private GetUserClientResponse userDto;
        private TransactionEntity entity;
        private ITransactionResponse response;

        private final long userId = 1L;
        private final BigDecimal amount = new BigDecimal("10.00");



        @Test
        void createTransaction_valid_shouldSucceed() {
            request = mock(PostTransactionRequest.class);
            userDto = mockUser(userId);
            entity = mock(TransactionEntity.class);
            response = mock(ITransactionResponse.class);

            when(request.getAmount()).thenReturn(amount);
            when(request.getInvoicingParty()).thenReturn(Games.ROULETTE);
            when(userClient.getUserById(userId))
                    .thenReturn(Result.success(userDto));

            when(userClient.deposit(userId, amount))
                    .thenReturn(Result.success(null));

            when(transactionEntityFactory.create(amount, Games.ROULETTE, userId))
                    .thenReturn(Result.success(entity));

            when(transactionResponseFactory.createPost(entity))
                    .thenReturn(response);

            var result = transactionHandler.createTransaction(request, userId);

            assertTrue(result.isSuccess());
            assertEquals(response, result.getSuccessData().get());

            verify(transactionRepository).save(entity);
        }


        @Test
        void createTransaction_userNotFound_shouldFail() {

            when(userClient.getUserById(userId))
                    .thenReturn(Result.failure(ErrorWrapper.USER_WAS_NOT_FOUND));

            var result = transactionHandler.createTransaction(request, userId);

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_WAS_NOT_FOUND,
                    result.getFailureData().get());

            verify(transactionRepository, never()).save(any());
        }

        @Test
        void createTransaction_factoryFails_shouldReturnFailure() {
            long userId = 1L;

            PostTransactionRequest request = mock(PostTransactionRequest.class);

            GetUserClientResponse userDto = mock(GetUserClientResponse.class);

            when(userDto.getId()).thenReturn(userId);

            when(userClient.getUserById(userId))
                    .thenReturn(Result.success(userDto));

            when(request.getAmount()).thenReturn(new BigDecimal("10.00"));
            when(request.getInvoicingParty()).thenReturn(Games.ROULETTE);

            when(transactionEntityFactory.create(any(BigDecimal.class), any(Games.class), anyLong()))
                    .thenReturn(Result.failure(ErrorWrapper.AMOUNT_WAS_NULL));

            var result = transactionHandler.createTransaction(request, userId);

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.AMOUNT_WAS_NULL, result.getFailureData().get());

            verify(transactionRepository, never()).save(any());
        }


    }

    @Nested
    class updateTransactionTests{





        @Test
        void updateTransaction_valid_shouldSucceed() {
            long transactionId = 1L;
            long userId = 99L;
            BigDecimal oldAmount = new BigDecimal("10.00");
            BigDecimal newAmount = new BigDecimal("20.00");
            BigDecimal difference = newAmount.subtract(oldAmount); // 10.00

            GetUserClientResponse userDto = mock(GetUserClientResponse.class);
            PutTransactionRequest request = mock(PutTransactionRequest.class);
            TransactionEntity entity = mock(TransactionEntity.class);

            when(transactionRepository.findById(transactionId))
                    .thenReturn(Optional.of(entity));

            when(request.getUserId()).thenReturn(userId);
            when(request.getAmount()).thenReturn(newAmount);
            when(request.getInvoicingParty()).thenReturn(Games.ROULETTE);

            when(entity.getAmount()).thenReturn(oldAmount);

            when(userClient.getUserById(userId))
                    .thenReturn(Result.success(userDto));
            when(userClient.withdraw(userId, difference))
                    .thenReturn(Result.success(null));

            doReturn(ErrorResult.success()).when(entity).update(newAmount, Games.ROULETTE);

            ITransactionResponse response = mock(ITransactionResponse.class);
            when(transactionResponseFactory.createPut(entity)).thenReturn(response);

            var result = transactionHandler.updateTransaction(transactionId, request);

            assertTrue(result.isSuccess());
            assertEquals(response, result.getSuccessData().get());

            verify(transactionRepository).save(entity);
            verify(userClient).withdraw(userId, difference);
            verify(entity).update(newAmount, Games.ROULETTE);
        }
        @Test
        void updateTransaction_transactionNotFound_shouldFail() {
            long transactionId = 1L;
            PutTransactionRequest request = mock(PutTransactionRequest.class);

            when(transactionRepository.findById(transactionId))
                    .thenReturn(Optional.empty());

            var result = transactionHandler.updateTransaction(transactionId, request);

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.TRANSACTION_WAS_NOT_FOUND,
                    result.getFailureData().get());

            verify(transactionRepository, never()).save(any());
        }

        @Test
        void updateTransaction_userNotFound_shouldFail() {

            long transactionId = 1L;
            long userId = 99L;
            PutTransactionRequest request = mock(PutTransactionRequest.class);
            TransactionEntity entity = mock(TransactionEntity.class);

            when(transactionRepository.findById(transactionId))
                    .thenReturn(Optional.of(entity));

            when(request.getUserId()).thenReturn(userId);

            when(userClient.getUserById(userId))
                    .thenReturn(Result.failure(ErrorWrapper.USER_WAS_NOT_FOUND));

            var result = transactionHandler.updateTransaction(transactionId, request);

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.USER_WAS_NOT_FOUND,
                    result.getFailureData().get());

            verify(transactionRepository, never()).save(any());
        }



        @Test
        void updateTransaction_validationFails_shouldFail() {

            long transactionId = 1L;
            long userId = 99L;

            PutTransactionRequest request = mock(PutTransactionRequest.class);
            TransactionEntity entity = mock(TransactionEntity.class);
            GetUserClientResponse userDto = mock(GetUserClientResponse.class);

            when(transactionRepository.findById(transactionId))
                    .thenReturn(Optional.of(entity));

            when(request.getUserId()).thenReturn(userId);
            when(request.getAmount()).thenReturn(new BigDecimal("0"));
            when(request.getInvoicingParty()).thenReturn(Games.ROULETTE);

            when(entity.getAmount()).thenReturn(new BigDecimal("0"));

            when(userClient.getUserById(userId))
                    .thenReturn(Result.success(userDto));

            when(entity.update(eq(new BigDecimal("0")), eq(Games.ROULETTE)))
                    .thenReturn(ErrorResult.failure(ErrorWrapper.AMOUNT_WAS_ZERO));

            var result = transactionHandler.updateTransaction(transactionId, request);

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.AMOUNT_WAS_ZERO,
                    result.getFailureData().get());

            verify(transactionRepository, never()).save(any());
        }

    }


    @Nested
    class deleteTransactionTests{




        @Test
        void deleteTransaction_valid_shouldSucceed() {
            long transactionId = 1L;

            TransactionEntity entity = mock(TransactionEntity.class);
            ITransactionResponse response = mock(ITransactionResponse.class);

            when(transactionRepository.findById(transactionId))
                    .thenReturn(Optional.of(entity));

            when(transactionResponseFactory.createDelete(entity))
                    .thenReturn(response);

            var result = transactionHandler.deleteTransaction(transactionId);

            assertTrue(result.isSuccess());
            assertEquals(response, result.getSuccessData().get());

            verify(transactionRepository).delete(entity);
        }

        @Test
        void deleteTransaction_transactionNotFound_shouldFail() {
            long transactionId = 1L;

            when(transactionRepository.findById(transactionId))
                    .thenReturn(Optional.empty());

            var result = transactionHandler.deleteTransaction(transactionId);

            assertTrue(result.isFailure());
            assertEquals(ErrorWrapper.TRANSACTION_WAS_NOT_FOUND,
                    result.getFailureData().get());

            verify(transactionRepository, never()).delete(any());
        }




    }



}