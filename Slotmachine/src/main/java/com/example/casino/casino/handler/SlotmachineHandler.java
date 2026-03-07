package com.example.casino.casino.handler;

import com.example.casino.casino.dto.transaction.BankingTransactionRequest;
import com.example.casino.casino.dto.transaction.IBankingTransactionRequest;
import com.example.casino.casino.dto.user.BankingUserResponse;
import com.example.casino.casino.factory.ISlotmachineGameFactory;
import com.example.casino.casino.model.ISlotmachineGameEntity;
import com.example.casino.casino.model.SlotmachineGameEntity;
import com.example.casino.casino.repository.ISlotmachineRepository;
import com.example.casino.casino.request.ISlotmachineRequest;
import com.example.casino.casino.response.ISlotmachineResponse;
import com.example.casino.casino.responseFactory.ISlotmachineResponseFactory;
import com.example.casino.casino.utility.ErrorWrapper;
import com.example.casino.casino.utility.Result;
import com.example.casino.casino.utility.SlotSymbols;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Random;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SlotmachineHandler implements ISlotmachineHandler {

    private final ISlotmachineRepository repository;
    private final ISlotmachineGameFactory modelFactory;
    private final ISlotmachineResponseFactory responseFactory;
    private final RestTemplate restTemplate = new RestTemplate();


    @Value("${casino.banking.url}") // in application.properties
    private String bankingUrl;

    @Value("${spring.application.name}")
    private String serviceName;

    private static final Random RANDOM = new Random();


    @Autowired
    public SlotmachineHandler(ISlotmachineRepository repository, ISlotmachineGameFactory modelFactory, ISlotmachineResponseFactory responseFactory) {
        this.repository = repository;
        this.modelFactory = modelFactory;
        this.responseFactory = responseFactory;
    }

    @Override
    public Result<ISlotmachineResponse, ErrorWrapper> play(ISlotmachineRequest request) {

        double betAmount = request.getBetAmount();

        //check, if bet amount is less than 0
        if (betAmount <= 0) {
            return Result.failure(ErrorWrapper.INVALID_BET_AMOUNT);
        }

        //get user information from banking API
        String userURL = bankingUrl + "user/" + request.getUser();
        long userId = request.getUser();
        BankingUserResponse user;


        try{
             user = restTemplate.getForObject(userURL, BankingUserResponse.class);
        }catch(Exception e){
            return Result.failure(ErrorWrapper.UNEXPECTED_INTERNAL_ERROR);
        }

        //Check if user has bank account
        if (user == null) {
           return Result.failure(ErrorWrapper.USER_NOT_FOUND);
        }

        //check balance
        if (user.getBalance() < betAmount){
            return Result.failure(ErrorWrapper.INSUFFICIENT_BALANCE);
        }

        //game logic
        //spin reels (generate symbols)
        List<SlotSymbols> reelResult = new ArrayList<>();
        SlotSymbols[] values = SlotSymbols.values();
        for (int i = 0; i < 3; i++){
            SlotSymbols symbol = values[RANDOM.nextInt(values.length)];
            reelResult.add(symbol);
        }

        //check if all symbols are the same
        boolean isWinningBig = reelResult.get(0) == reelResult.get(1) && reelResult.get(1) == reelResult.get(2);
        boolean isWinningSmall = reelResult.get(0) == reelResult.get(1) || reelResult.get(1) == reelResult.get(2) || reelResult.get(0) == reelResult.get(2);

        double winAmount = 0;
        //calculate win amount
        if (isWinningBig){
            winAmount = request.getBetAmount() * 10;
        }else if (isWinningSmall){
            winAmount = request.getBetAmount() * 2;
        }else{
            winAmount = 0;
        }

        boolean isWinning = winAmount > 0;


        //convert symbols to strings
        String slotResultString = reelResult.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));

        //win amount minus bet amount for banking Service/balance
        double netAmount = winAmount - betAmount;


        //create request for banking service
        IBankingTransactionRequest transactionRequest = new BankingTransactionRequest(serviceName, netAmount);

        String transactionUrl = bankingUrl + "transaction/user/" + request.getUser();

        //send request
        try {
            restTemplate.postForObject(transactionUrl, transactionRequest, Object.class);
        } catch (Exception e) {
            return Result.failure(ErrorWrapper.UNEXPECTED_INTERNAL_ERROR);
        }

        //if successful, create game entity
        var entityResult = modelFactory.createSlotmachine(
                userId,
                betAmount,
                winAmount,
                isWinning,
                slotResultString
        );

        if (entityResult.isFailure()) {
            return Result.failure(entityResult.getFailureData().get());
        }

        //save in DB
        ISlotmachineGameEntity savedEntity = repository.save((SlotmachineGameEntity)entityResult.getSuccessData().get());

        //create response for user
        ISlotmachineResponse response = responseFactory.createSlotmachineResponse(savedEntity);

        //return as success
        return Result.success(response);
    }

    @Override
    public Result<Iterable<ISlotmachineResponse>, ErrorWrapper> readAllGames(){
        var result = repository.findAll();

        var finalResult = new ArrayList<ISlotmachineResponse>();

        for (SlotmachineGameEntity slotmachineGameEntity : result){
            var response = responseFactory.createSlotmachineResponse(slotmachineGameEntity);
            finalResult.add(response);
        }
        //return as success
        return Result.success(finalResult);
    }

    @Override
    public Result<ISlotmachineResponse, ErrorWrapper> readGame(long id){
        var target = repository.findById(id);
        if (target.isEmpty()){
            return Result.failure(ErrorWrapper.GAME_NOT_FOUND);
        }

        var response = responseFactory.createSlotmachineResponse(target.get());

        //return as success
        return Result.success(response);
    }

    @Override
    public String calculateChances(){
        //number of symbols
        int n = SlotSymbols.values().length;

        //calculate chances for probability of 3 matching symbols
        double bigWinChance = 1.0 / (n * n);

        //calculate chances for probability of 2 matching symbols
        double smallWinChance = (3.0 * (n - 1)) / (n * n);

        //calculate RTP (Return to Player) -> zu Deutsch: Auszahlungsquote
        double rtp = (bigWinChance * 10) + (smallWinChance * 2);

        return String.format(
                "With %d symbols, your chances are as follows:\n" +
                        "- Jackpot (3 matching): %.2f%%\n" +
                        "- Small Win (exactly 2 matching): %.2f%%\n" +
                        "- RTP (Theoretical Return to Player): %.2f%%",
                n, bigWinChance * 100, smallWinChance * 100, rtp * 100
        );
    }

    @Override
    public String getRules(){
        return "Slot Machine Rules:\n" +
                "First, select your bet amount and start the game.\n" +
                "Three matching symbols win the Jackpot of 10 x the bet amount!\n" +
                "Two matching symbols win a prize of 2 x the bet amount!\n" +
                "No matching symbols loose the bet amount.\n" +
                "The net result will be automatically updated in your banking account.";
    }

}
