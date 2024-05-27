package coopworld.game.Tools.ElementsGeneration;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.Random;

import coopworld.game.CooperativeGame;
import coopworld.game.CrossPlatform.GeneralUtils;
import coopworld.game.Logs.Enums;
import coopworld.game.Logs.GenerationDelays;
import coopworld.game.Screens.GameScreens.CombinedInstructions;
import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Tools.AiTools.DataStructures.Node;
import coopworld.game.Tools.AiTools.MatrixBuilder;
import coopworld.game.Tools.AiTools.MatrixCell;
import coopworld.game.Tools.PersistentData;
import coopworld.game.Tools.VisualUtils;

public class ElementsGenerationManager {
    private GameScreen gameScreen;
    private long elapsedVirtualGenerationTime;
    private long[] lastRealGenerationTime;
    private long[] lastVirtualGenerationTime;
    private long lastIceGenerationTime;
    private long lastIceRemovingTime;
    private long lastNatureRemovingTime;

    private long lastNatureGenerationTime;
    private long elapsedRealGenerationTime;
    private long GENERATION_DELAY, COINS_GENERATION_DELAY, VIRTUAL_COINS_GENERATION_DELAY;
    private long GENERATION_DELAY_NATURE_OBS;
    private long GENERATION_DELAY_ICE_OBS;
    private Random r = new Random();
    private GeneralUtils utils;

    private long intervalSeconds;
    private long lastUpdateTime;
    private long currentTime;
    // starts from 1
    private int requestNum;
    private int rounds;

    MatrixBuilder.cellType currAskerCellType;
    MatrixBuilder.cellType currResponderCellType;

    Enums.Player currAsker;
    private ArrayList<Enums.Player> askersOrder;

    private boolean isLastRound;

    int low = 1;
    int high = 3;

    long[] currCounterCoin, currCounterSpecialCoins, currCounterIceCubes;

    ArrayList<Integer> range = new ArrayList<Integer>();
    int randInt;
    int scoreBefore;

    int helpCostForHuman;

    boolean potentialHelpTime;

    public ElementsGenerationManager(GameScreen gameScreen, Enums.Player askerFirst,
                                     int roundsNum,
                                     GeneralUtils utils) {
        this.gameScreen = gameScreen;
        this.utils = utils;
        isLastRound = false;
        elapsedVirtualGenerationTime = 0;
        lastRealGenerationTime = new long[]{0};
        lastVirtualGenerationTime = new long[]{0};
        lastIceGenerationTime = 0;
        lastNatureGenerationTime = 0;
        elapsedRealGenerationTime = 0;
        GenerationDelays gd = CooperativeGame.gameData.getGame_params().getGeneration_delays();
        COINS_GENERATION_DELAY = (long)gd.getCoins_delay() * 1000000000L;
        VIRTUAL_COINS_GENERATION_DELAY = (long)gd.getVirtual_coins_delay() * 1000000000L;

        GENERATION_DELAY_NATURE_OBS = 5000000000L;
        GENERATION_DELAY_ICE_OBS = 5000000000L;
        lastIceRemovingTime = GENERATION_DELAY_ICE_OBS;
        lastNatureRemovingTime = GENERATION_DELAY_NATURE_OBS;

        r = new Random();

        intervalSeconds = 0;
        lastUpdateTime = 0;
        currentTime = 0;
        requestNum = 0;
        rounds = roundsNum;

        askersOrder = new ArrayList<Enums.Player>();

        Enums.Player askerSecond;
        if (askerFirst == Enums.Player.Human) {
            askerSecond = Enums.Player.Virtual;
        }
        else {
            askerSecond = Enums.Player.Human;
        }
        askersOrder.add(askerFirst);
        askersOrder.add(askerSecond);

        currCounterCoin = new long[]{0};
        currCounterSpecialCoins = new long[]{0};
        currCounterIceCubes = new long[]{0};

        currCounterCoin[0] = 0;
        currCounterSpecialCoins[0] = 0;
        currCounterIceCubes[0] = 0;

        currAsker = askerFirst;

        range.add(1);
        range.add(2);
        range.add(3);

        helpCostForHuman = CooperativeGame.gameData.
                getGame_params().getReciprocityValues().getHelp_providing_cost_human();

        potentialHelpTime = false;
    }

    public void setIntervalSeconds(float intervalSeconds) {
        this.intervalSeconds = 1000000000L * (long)intervalSeconds;
    }

    public void init(){
        requestNum += 1;
        if(((float)requestNum / 2f) > rounds){
            isLastRound = true;
            //gameScreen.setReciprocityState(GameScreen.ReciprocityState.End);
        }

        initAsker();
        if (currAsker == Enums.Player.Human){
            isLastRound = false; // TODO CHECK THIS - force last asker to be virtual
        }
        initTimeMeasurement();

    }

    public void initAsker(){
        // init asker.
        currAsker = askersOrder.get((requestNum - 1) % askersOrder.size());
        // init elements (coins/virtual coins)
        if(currAsker == Enums.Player.Human){
            currAskerCellType = MatrixBuilder.cellType.coins;
            currResponderCellType = MatrixBuilder.cellType.virtualcoins;
        }
        else if(currAsker == Enums.Player.Virtual){
            currAskerCellType = MatrixBuilder.cellType.virtualcoins;
            currResponderCellType = MatrixBuilder.cellType.coins;
        }
    }

    public boolean addObstacleForAsker(){
        if(currAsker == Enums.Player.Human){
            return addIceObstacle();
        }
        else if(currAsker == Enums.Player.Virtual){
            return addNatureObstacle();
        }
        return false;
    }

    public void initTimeMeasurement(){
        gameScreen.getCountdownTimer().resetHelpRequestTimeCounting();

//        lastUpdateTime = ((CooperativeGame)Gdx.app.getApplicationListener()).
//                crossPlatformObjects.getUtils().getNanoTime();
    }

    public boolean isTimePassed(){
        currentTime = utils.getNanoTime();
        if (currentTime - lastUpdateTime >= intervalSeconds) {
            return true;
        }
        return false;
    }

    public void controlledCreationInstructions(int level_num) {
        switch(level_num){
            case 3:
                addIceObstacleControlledInstructions();
                break;

            case 4:
                addNatureObstacleControlledInstructions();
                break;
        }
        addCollectibleElement(MatrixBuilder.cellType.coins);
        // check if non blocked virtual coin is available.
        if(gameScreen.getSearchableMatrix().getVirtualCoinsList().isEmpty()) {
            // NO FREE COINS. NO BLOCKED COINS.
            if (gameScreen.getSearchableMatrix().getBlockedVirtualCoinsList().isEmpty()) {
                addCollectibleElement(MatrixBuilder.cellType.virtualcoins);
            }
        }
    }

    // interval - in seconds!
    public void experimentalElementsCreation(){
        if(gameScreen.getCountdownTimer().getRemainingTime() <= 0) {
            gameScreen.setReciprocityState(GameScreen.ReciprocityState.End);
        }
        switch(gameScreen.getReciprocityState()){
            case Init:
                gameScreen.setReciprocityState(GameScreen.ReciprocityState.BeforeHelp);
                init();

                break;

            case BeforeHelp:
                addCollectibleElement(currAskerCellType);
                // generate elements for the responder, independently.
                addCollectibleElement(currResponderCellType);
                // todo - check watch! Sarit
                if(PersistentData.GAME_GROUP == Enums.Group.Experimental){
                    if(gameScreen.getCountdownTimer().isTimePassedToGenerateHelpRequest()){
                        if(gameScreen.getCountdownTimer().getRemainingTime() <= 0){ // TODO ??? WILL CONTINUE ANYWAY?
                            if(isLastRound){
                                gameScreen.setReciprocityState(GameScreen.ReciprocityState.End);
                                break;
                            }
                        }
                        else{
                            gameScreen.setReciprocityState(GameScreen.ReciprocityState.ObstacleCreation);
                            setPotentialHelpTime(true);
                        }
                    }
                }

                break;

            case ObstacleCreation:
                if (addObstacleForAsker() == true) {
                    gameScreen.getPauseButton().remove(); // TODO - should we check if the button is on?
                    gameScreen.setReciprocityState(GameScreen.ReciprocityState.DuringHelp);
                    break;
                }
                if(currAsker == Enums.Player.Virtual){
                    addCollectibleElement(currAskerCellType);
                }
                addCollectibleElement(currResponderCellType);

                break;

            case DuringHelp:
                // generate elements for the responder, independently.
                addCollectibleElement(currResponderCellType);
                break;

            case End:
                gameScreen.setState(GameScreen.State.LevelEnded);
                break;
        }
        // that's it! level is ended.
        //gameScreen.setState(GameScreen.State.LevelEnded);
    }

    // m.getEmptyVirtualCoinsPositions(), elapsedVirtualGenerationTime, lastVirtualGenerationTime
    public boolean addCollectibleElement(MatrixBuilder.cellType type)
    {
        ArrayList<MatrixCell> emptyPositions;
        long elapsedGenerationTime;
        long[] lastGenerationTime;
        ArrayList<Node> elements;

        switch(type){
            case coins:
                emptyPositions = gameScreen.getMatrixBuilder().getEmptyCoinsPositions();
                lastGenerationTime = lastRealGenerationTime;
                GENERATION_DELAY = COINS_GENERATION_DELAY;
                elements = gameScreen.searchableMatrix.getCoinsList();
                break;

            case virtualcoins:
                emptyPositions = gameScreen.getMatrixBuilder().getEmptyVirtualCoinsPositions();
                lastGenerationTime = lastVirtualGenerationTime;
                GENERATION_DELAY = VIRTUAL_COINS_GENERATION_DELAY;
                elements = gameScreen.searchableMatrix.getVirtualCoinsList();

                break;

            default:
                System.out.println("ERROR! INVALID TYPE OF ELEMENT TO COLLECT.");
                return false;
        }

        if (gameScreen.getRandomGenerator() != null) {
            //  if (this.searchableMatrix.getVirtualCoinsList().isEmpty() || (isWaitingForHelp())) {
            if (elements.isEmpty()){
            //if (emptyPositions.size() != 0){
                currentTime = utils.getNanoTime();
                elapsedGenerationTime = currentTime - lastGenerationTime[0];
                if (elapsedGenerationTime >= GENERATION_DELAY) {
                    int randIndex = r.nextInt(emptyPositions.size());
                    MatrixCell pickedCell = emptyPositions.get(randIndex);
                    if(!VisualUtils.playerOverlaps(pickedCell, 1)) {
                        gameScreen.getRandomGenerator().generateRandomTile(type, pickedCell, false);
                        lastGenerationTime[0] = utils.getNanoTime();
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public boolean addIceObstacleControlledInstructions()
    {
        if(gameScreen.getHumanNumRequests() == 0) {
            if (gameScreen.getRandomGenerator() != null) {
                /*
                System.out.println("ICE OBS LIST SIZE: " + gameScreen.getMatrixBuilder()
                        .getIceObstaclesPositions().size());
                */
                if (gameScreen.getMatrixBuilder().getEmptyIceObstaclesPositions().size() != 0) {
                    currentTime = utils.getNanoTime();
                    elapsedRealGenerationTime = currentTime - lastIceRemovingTime;
                    if (elapsedRealGenerationTime >= GENERATION_DELAY_ICE_OBS) {
                        // find position to generate
                        int randIndex = r.nextInt(gameScreen.getMatrixBuilder().getEmptyIceObstaclesPositions().size());
                        MatrixCell pickedCell = gameScreen.getMatrixBuilder().getEmptyIceObstaclesPositions().get(randIndex);
                        if (!VisualUtils.playerOverlaps(pickedCell, 3)) {
                            gameScreen.getRandomGenerator().generateRandomTile(MatrixBuilder.cellType.iceObstacles,
                                    pickedCell, true);
                            lastIceGenerationTime = utils.getNanoTime();
                            // todo - certainly? maybe this should be in randomGenerator.
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean addNatureObstacleControlledInstructions()
    {
        if(gameScreen.getVirtualNumRequests() == 0) {
            if (gameScreen.getRandomGenerator() != null) {
                if (gameScreen.getMatrixBuilder().getEmptyNatureObstaclesPositions().size() != 0) {
                    currentTime = utils.getNanoTime();
                    elapsedRealGenerationTime = currentTime - lastNatureRemovingTime;
                    if (elapsedRealGenerationTime >= GENERATION_DELAY_NATURE_OBS) {
                        int randIndex = r.nextInt(gameScreen.getMatrixBuilder().getEmptyNatureObstaclesPositions().size());
                        MatrixCell pickedCell = gameScreen.getMatrixBuilder().getEmptyNatureObstaclesPositions().get(randIndex);
                        if (!VisualUtils.playerOverlaps(pickedCell, 3)) {
                            gameScreen.getRandomGenerator().generateRandomTile(MatrixBuilder.cellType.natureObstacles,
                                    pickedCell, true);
                            lastNatureGenerationTime = utils.getNanoTime();
                            // todo - certainly? maybe this should be in randomGenerator.
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public boolean addIceObstacle()
    {
        boolean wasGenerated = false;
        if (gameScreen.getRandomGenerator() != null) {
            /*
            System.out.println("ICE OBS LIST SIZE: " + gameScreen.getMatrixBuilder()
                    .getIceObstaclesPositions().size());
            */
            if (gameScreen.getMatrixBuilder().getEmptyIceObstaclesPositions().size() != 0){
                currentTime = utils.getNanoTime();
                elapsedRealGenerationTime = currentTime - lastIceGenerationTime;
                if (elapsedRealGenerationTime >= GENERATION_DELAY_ICE_OBS) {
                    // find position to generate
                    int randIndex = r.nextInt(gameScreen.getMatrixBuilder().getEmptyIceObstaclesPositions().size());
                    MatrixCell pickedCell = gameScreen.getMatrixBuilder().getEmptyIceObstaclesPositions().get(randIndex);
                    if(!VisualUtils.playerOverlaps(pickedCell, 3)) {
                        wasGenerated = gameScreen.getRandomGenerator().generateRandomTile(MatrixBuilder.cellType.iceObstacles,
                                pickedCell, true);
                        if(wasGenerated){
                            //gameScreen.getPauseButton().remove(); // TODO - should we check if the button is on?
                            lastIceGenerationTime = utils.getNanoTime();

                            if(!(gameScreen instanceof CombinedInstructions)) {
                                gameScreen.getIceObsAnimation().getCellsToReplace();
                                gameScreen.getIceObsAnimation().animate();
                            }
                        }
                    }
                }
            }
        }
        return wasGenerated;
    }

    public boolean addNatureObstacle()
    {
        boolean wasGenerated = false;
        if (gameScreen.getRandomGenerator() != null) {
            if(gameScreen.getHumanScore() >= helpCostForHuman) {
                if (gameScreen.getMatrixBuilder().getEmptyNatureObstaclesPositions().size() != 0) {
                    currentTime = utils.getNanoTime();
                    elapsedRealGenerationTime = currentTime - lastNatureGenerationTime;
                    if (elapsedRealGenerationTime >= GENERATION_DELAY_NATURE_OBS) {
                        int randIndex = r.nextInt(gameScreen.getMatrixBuilder().getEmptyNatureObstaclesPositions().size());
                        MatrixCell pickedCell = gameScreen.getMatrixBuilder().getEmptyNatureObstaclesPositions().get(randIndex);
                        if (!VisualUtils.playerOverlaps(pickedCell, 3)) {
                            wasGenerated = gameScreen.getRandomGenerator().generateRandomTile(MatrixBuilder.cellType.natureObstacles,
                                    pickedCell, true);
                            if (wasGenerated) {
                                //gameScreen.getPauseButton().remove(); // TODO - should we check if the button is on?

                                lastNatureGenerationTime = utils.getNanoTime();
                            }
                        }
                    }
                }
            }
        }
        return wasGenerated;
    }

    public void setLastIceRemovingTime(long lastIceRemovingTime) {
        this.lastIceRemovingTime = lastIceRemovingTime;
    }

    public void setLastNatureRemovingTime(long lastNatureRemovingTime) {
        this.lastNatureRemovingTime = lastNatureRemovingTime;
    }

    //generateCoinsWithLimitation
    //combinedInstructionsElementsCreation

    //true - end!
    public boolean generateCoinsWithLimitation(int coinsNum){
        if(currCounterCoin[0] == coinsNum){
            currCounterCoin[0] = 0;
            return true;
        }
        else if(addCollectibleElement(MatrixBuilder.cellType.coins)){
            currCounterCoin[0] = currCounterCoin[0] + 1;
        }
        return false;
    }

    //true - end!
    public void combinedInstructionsElementsCreation(){

    }

    public int getRandInt(){
        return new Random().nextInt(range.size());
    }

    public boolean isPotentialHelpTime() {
        return potentialHelpTime;
    }

    public void setPotentialHelpTime(boolean potentialHelpTime) {
        if(potentialHelpTime){
            gameScreen.getCountdownTimer().pause();
        }
        else{
            gameScreen.getCountdownTimer().resume();
        }
        this.potentialHelpTime = potentialHelpTime;
    }
}
