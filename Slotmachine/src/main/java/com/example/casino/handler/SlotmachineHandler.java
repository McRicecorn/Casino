package com.example.casino.handler;

import com.example.casino.dto.stats.IGlobalStatsResponse;
import com.example.casino.dto.stats.IUserStatsResponse;
import com.example.casino.dto.transaction.BankingTransactionRequest;
import com.example.casino.dto.transaction.IBankingTransactionRequest;
import com.example.casino.dto.user.BankingUserResponse;
import com.example.casino.factory.ISlotmachineGameFactory;
import com.example.casino.model.ISlotmachineGameEntity;
import com.example.casino.model.SlotmachineGameEntity;
import com.example.casino.repository.ISlotmachineRepository;
import com.example.casino.request.ISlotmachineRequest;
import com.example.casino.response.ISlotmachineResponse;
import com.example.casino.responseFactory.ISlotmachineResponseFactory;
import com.example.casino.utility.ErrorWrapper;
import com.example.casino.utility.Result;
import com.example.casino.utility.SlotSymbols;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Random;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SlotmachineHandler implements ISlotmachineHandler {

    private final ISlotmachineRepository repository;
    private final ISlotmachineGameFactory modelFactory;
    private final ISlotmachineResponseFactory responseFactory;
    private final RestTemplate restTemplate;


    @Value("${casino.banking.url}") // in application.properties
    private String bankingUrl;

    @Value("${spring.application.name}")
    private String serviceName;

    //for random slot symbol access
    private final Random RANDOM;


    @Autowired
    public SlotmachineHandler(ISlotmachineRepository repository, ISlotmachineGameFactory modelFactory, ISlotmachineResponseFactory responseFactory, RestTemplate restTemplate, Random random) {
        this.repository = repository;
        this.modelFactory = modelFactory;
        this.responseFactory = responseFactory;
        this.restTemplate = restTemplate;
        this.RANDOM = random;
    }

    @Override
    public Result<ISlotmachineResponse, ErrorWrapper> play(ISlotmachineRequest request) {

        BigDecimal betAmount = request.getBetAmount();

        //check, if bet amount is less than 0
        if (betAmount.compareTo(BigDecimal.ZERO) <= 0) {
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

        //for testing without banking service
        //user = new BankingUserResponse(1, "horst", "schlemmer", 1000);

        //Check if user has bank account
        if (user == null) {
           return Result.failure(ErrorWrapper.USER_NOT_FOUND);
        }

        //check balance
        if (user.getBalance().compareTo(betAmount) < 0){
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

        BigDecimal winAmount;
        //calculate win amount
        if (isWinningBig){
            winAmount = request.getBetAmount().multiply(BigDecimal.TEN);
        }else if (isWinningSmall){
            winAmount = request.getBetAmount().multiply(BigDecimal.valueOf(2));
        }else{
            winAmount = BigDecimal.ZERO;
        }

        boolean isWinning = winAmount.compareTo(BigDecimal.ZERO) > 0;


        //convert symbols to strings
        String slotResultString = reelResult.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));

        //win amount minus bet amount for banking Service/balance
        BigDecimal netAmount = winAmount.subtract(betAmount);


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
        List<ISlotmachineResponse> finalResult = repository.findAll().stream()
                .map(responseFactory::createSlotmachineResponse)
                .toList();

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
    public Result<String, ErrorWrapper> calculateChances(){
        //number of symbols
        int n = SlotSymbols.values().length;

        //calculate chances for probability of 3 matching symbols
        double bigWinChance = 1.0 / (n * n);

        //calculate chances for probability of 2 matching symbols
        double smallWinChance = (3.0 * (n - 1)) / (n * n);

        //calculate RTP (Return to Player) -> zu Deutsch: Auszahlungsquote
        double rtp = (bigWinChance * 10) + (smallWinChance * 2);

        String message = String.format(
                "With %d symbols, your chances are as follows:\n" +
                        "- Jackpot (3 matching): %.2f%%\n" +
                        "- Small Win (exactly 2 matching): %.2f%%\n" +
                        "- RTP (Theoretical Return to Player): %.2f%%",
                n, bigWinChance * 100, smallWinChance * 100, rtp * 100
        );

        return Result.success(message);
    }

    @Override
    public Result<String, ErrorWrapper> getRules(){
        String rules = "Slot Machine Rules:\n" +
                "First, select your bet amount and start the game.\n" +
                "Three matching symbols win the Jackpot of 10 x the bet amount!\n" +
                "Two matching symbols win a prize of 2 x the bet amount!\n" +
                "No matching symbols loose the bet amount.\n" +
                "The net result will be automatically updated in your banking account.";
        return Result.success(rules);
    }

    @Override
    public Result<IUserStatsResponse, ErrorWrapper> readUserStats(long userId){

        //find all games with specific userId
        List<ISlotmachineGameEntity> userGames = repository.findByUserId(userId);
        if (userGames.isEmpty()){
            return Result.failure(ErrorWrapper.NO_GAMES_FOUND);
        }
        //send to the response Factory
        IUserStatsResponse result = responseFactory.createUserStatsResponse(userGames);
        return Result.success(result);
    }

    @Override
    public Result<IGlobalStatsResponse, ErrorWrapper> readGlobalStats(){
        //save all games in a list
        List<SlotmachineGameEntity> allGames = repository.findAll();
        if (allGames.isEmpty()) {
            return Result.failure(ErrorWrapper.NO_GAMES_FOUND);
        }
        //send to the response Factory
        IGlobalStatsResponse result = responseFactory.createGlobalStatsResponse(allGames);
        return Result.success(result);

    }

}
