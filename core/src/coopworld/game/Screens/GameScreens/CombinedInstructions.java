package coopworld.game.Screens.GameScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

import coopworld.game.CooperativeGame;
import coopworld.game.Logs.Enums;
import coopworld.game.Logs.LevelParams;
import coopworld.game.Scenes.AnimatedImage;
import coopworld.game.Tools.AiTools.MatrixBuilder;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;
import coopworld.game.Tools.PersistentData;
import coopworld.game.Tools.PopUp;

import static coopworld.game.Screens.GameScreens.CombinedInstructions.GeneralVirtualAsking.GenerateLockedCoin;
import static coopworld.game.Screens.GameScreens.CombinedInstructions.GeneralVirtualAsking.WaitUntilLevelEnd;
import static coopworld.game.Screens.GameScreens.CombinedInstructions.InstructionalStepState.CoinsCollecting;
import static coopworld.game.Screens.GameScreens.CombinedInstructions.InstructionalStepState.HumanAsking;
import static coopworld.game.Screens.GameScreens.CombinedInstructions.InstructionalStepState.PlayingTogether;
import static coopworld.game.Screens.GameScreens.CombinedInstructions.InstructionalStepState.VirtualAsking;
import static coopworld.game.Screens.GameScreens.CombinedInstructions.PlayingTogetherInnerState.WaitForElementsCollection;
import static coopworld.game.Screens.GameScreens.GameScreen.State.LevelEnded;

public class CombinedInstructions extends GameScreen {

    public enum InstructionalStepState {
        CoinsCollecting, PlayingTogether, HumanAsking, VirtualAsking
    }
    InstructionalStepState instructionalStepCurrState;

    public enum PlayingTogetherInnerState {
        BeforeExplanation, AfterCreatePopUpExplanation, AfterExplanation, WaitForElementsCollection
    }
    PlayingTogetherInnerState playingTogetherInnerState;

    int pointer;

    public enum HumanAskingState {
        ForceAccept, ForceReject
    }
    HumanAskingState humanAskingInnerState;

    public enum GeneralHumanAsking {
        Before, GenerateLockedCoin, CreatePreExplanationPopUp, CreatePostExplanationPopUp, Waitt,
        After, NowClick, WaitForLetsSee, LetsSee
    }
    GeneralHumanAsking generalHumanAsking;

    public enum VirtualAskingState {
       ForceAccept, ForceReject
    }
    VirtualAskingState virtualAskingState;

    public enum GeneralVirtualAsking {
        Before, GenerateLockedCoin, Waitt, CreatePopUp, After, WaitUntilLevelEnd
    }
    GeneralVirtualAsking generalVirtualAsking;

    ArrayList<InstructionalStepState> instructionalStepArray;

    int iceObstaclesCreated, regularObstaclesCreated;

    private float timeRegularObstacles = 10f;
    private float timeIceObstacles = 10f;
    private float timeEndLevel = 30f;
    private float timeLetsSee = 6f;

    long startTime = 0;

    AnimatedImage arrow;

    public PopUp nowClickPopUp, preExplanation, nowLetsSee = null;

    boolean preExplanationIsOn;

    String humanGender;

    ArrayList<String> paths;

    String popUpFilePrefix;

    long popUpsMinimalTime;

    protected boolean virtualAllowedToAsk;

    boolean visitedHandle = false;

    public CombinedInstructions(CooperativeGame game, LevelParams levelParams) {
        super(game, levelParams);
        elementsRemover.removeAllVirtualCoins();
        paths = new ArrayList<String>();

        instructionalStepArray = new ArrayList<InstructionalStepState>();

        instructionalStepArray.add(CoinsCollecting);
        instructionalStepArray.add(PlayingTogether);
        // TODO - CONTINUE IMPLEMENTING THIS ARRAY AND RESTRICT THESE LINES ONLY FOR THE
        // EXPERIMENTAL GROUP.
        instructionalStepArray.add(HumanAsking);
        instructionalStepArray.add(VirtualAsking);

        pointer = 0;
        instructionalStepCurrState = instructionalStepArray.get(pointer);
        //instructionalStepCurrState = VirtualAsking;

        playingTogetherInnerState = PlayingTogetherInnerState.WaitForElementsCollection;

        //playingTogetherInnerState = PlayingTogetherInnerState.BeforeExplanation;
        humanAskingInnerState = HumanAskingState.ForceAccept;
        generalHumanAsking = GeneralHumanAsking.Before;
        virtualAskingState = VirtualAskingState.ForceAccept;
        generalVirtualAsking = GeneralVirtualAsking.Before;

        iceObstaclesCreated = 0;
        regularObstaclesCreated = 0;

        arrow = GraphicTools.getAnimatedImage("hud/arrow.pack", 0.07f,
                "arrow");
        arrow.setPosition(0.48f * screenWidth, 0.86f * screenHeight);

        arrow.setSize(0.25f * Constants.VIEWPORT_WIDTH, 0.15f * Constants.VIEWPORT_HEIGHT);
        //arrow.debug();
        preExplanationIsOn = true;
        virtualAllowedToAsk = false;

        if(Constants.GAME_LANGUAGE.getLanguage() == Enums.Language.English){
            humanGender = "";
        }
        // hebrew/arabic version
        else{
            if(CooperativeGame.gameData.getUser().getHuman_gender() == Enums.Gender.Female){
                humanGender = "girl/";
            }
            else{
                humanGender = "boy/";
            }
        }

        String virtualPlayerGenderReference;
        if(CooperativeGame.gameData.getUser().getVirtual_gender() == Enums.Gender.Male){
            virtualPlayerGenderReference = "virtual_boy";
        }
        else{
            virtualPlayerGenderReference = "virtual_girl";
        }
        popUpFilePrefix = CooperativeGame.languagePrefix + "combined_instructions/" +
                virtualPlayerGenderReference + "/" + humanGender;

        popUpsMinimalTime = (long)5f;


        System.out.println("Timer started!!!");
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                System.out.println("Timer UP!!!");
            }
        }, 1);
    }

    public void instructionalStateUp(){
        pointer++;
        if(pointer < instructionalStepArray.size()){
            instructionalStepCurrState = instructionalStepArray.get(pointer);
        }
    }

    @Override
    public void render(float delta) {
        switch (state) {
            case SpecialCoinExplanation:
                float width = 0.48f * screenWidth * 0.9f, height = 0.82f * width * 0.9f;
                float seconds, xPos, yPos;
                xPos = 0.2f * Constants.VIEWPORT_WIDTH;
                yPos = 0.2f * Constants.VIEWPORT_HEIGHT;

                width *= 1.3;
                height *= 1.3;

                String postfix = (CooperativeGame.gameData.
                        getGame_params().getReciprocityValues().
                        getLocked_coin_value()) + "";

                String specialCoinPath = CooperativeGame.languagePrefix +
                        "pop-ups/special_coin_explanation/" + humanGender + postfix + ".png";
                // TODO, -1..... mmmm
                final PopUp p = new PopUp(specialCoinPath, -1,
                        xPos, yPos, width, height, ((CooperativeGame) Gdx.app.getApplicationListener()).
                        crossPlatformObjects.getUtils(), true, popUpsMinimalTime);
                paths.add(specialCoinPath);

                p.setScaling(Scaling.fillX);
                p.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                    if(p.isMinimalTimePassed()){
                        p.setFinished(true);
                        p.remove();
                        music.toneUpBackgroundMusic();
                        switchToRunningState(true);
                    }
                    }
                });
                p.init();
                music.playEndGameSound();
                stage.addActor(p);

                switchToPauseState(false);
                break;
        }
        switch(instructionalStepCurrState){
            case CoinsCollecting:
                // TODO - make it generic.
                if(elementsGenerationManager.generateCoinsWithLimitation(10)){
                    // go the PlayingTogether.
                    instructionalStateUp();
                    playingTogetherInnerState = PlayingTogetherInnerState.BeforeExplanation;
                    //elementsRemover.removeAllCoins();
                }
                break;

            case PlayingTogether:
                switch(playingTogetherInnerState){
                    // create pop up - meet Bob.
                    case BeforeExplanation:
                        float width = 0.44f * screenWidth;
                        float height = 0.65f * width;
                        float seconds, xPos, yPos;
                        xPos = 0.14f * Constants.VIEWPORT_WIDTH;
                        yPos = 0.12f * Constants.VIEWPORT_HEIGHT;

                        width *= 1.7;
                        height *= 1.7;

                        String meetBobPath = popUpFilePrefix + "meet_Bob.png";
                        // TODO, -1..... mmmm
                        final PopUp meetBobPopUp = new PopUp(meetBobPath, -1,
                            xPos, yPos, width, height, ((CooperativeGame) Gdx.app.getApplicationListener()).
                            crossPlatformObjects.getUtils(), false, popUpsMinimalTime);
                        paths.add(meetBobPath);

                        meetBobPopUp.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            if(meetBobPopUp.isMinimalTimePassed()){
                                meetBobPopUp.setFinished(true);
                                meetBobPopUp.remove();
                                music.toneUpBackgroundMusic();
                                switchToRunningState(true);
                                playingTogetherInnerState = PlayingTogetherInnerState.AfterExplanation;
                            }
                            }
                        });
                        meetBobPopUp.init();
                        music.playEndGameSound();

                        stage.addActor(meetBobPopUp);
                        playingTogetherInnerState = PlayingTogetherInnerState.AfterCreatePopUpExplanation;
                        switchToPauseState(false);
                        break;

                    case AfterExplanation:
                        elementsRemover.generateAllCoin();
                        elementsRemover.generateAllVirtualCoins();
                        playingTogetherInnerState = WaitForElementsCollection;

                        break;

                    case WaitForElementsCollection:
                        // generate X coins, generate virtual coins without limitation.
                        elementsGenerationManager.addCollectibleElement(MatrixBuilder.cellType.virtualcoins);
                        if(elementsGenerationManager.generateCoinsWithLimitation(10)){
                            if(PersistentData.GAME_GROUP == Enums.Group.Experimental){
                                instructionalStepCurrState = HumanAsking;
                            }
                            else{
                                setState(LevelEnded);
                            }
                        }
                        break;
                }
                break;

            case HumanAsking:
                if(state == State.Running){
                    elementsGenerationManager.addCollectibleElement(MatrixBuilder.cellType.coins);
                    elementsGenerationManager.addCollectibleElement(MatrixBuilder.cellType.virtualcoins);

                }
                switch(humanAskingInnerState){
                    // create pop up - meet Bob.
                    case ForceAccept:
                        if(generalHumanAsking == GeneralHumanAsking.Before){
                            if(iceObstaclesCreated < 2){
                                if(elementsRemover.createOneIceObstacle()){
                                    getPauseButton().remove(); // TODO - should we check if the button is on?

                                    iceObstaclesCreated++;
                                    if(iceObstaclesCreated == 2){
                                        // start time counting.
                                        startTime = System.currentTimeMillis();
                                    }
                                }
                            }
                            else{
                                // if time has passed - create pop-up.
                                if(timeHasPassedIceObstacles()){
                                    generalHumanAsking = GeneralHumanAsking.CreatePreExplanationPopUp;
                                }
                            }
                            break;
                        }
                        else if(generalHumanAsking == GeneralHumanAsking.CreatePreExplanationPopUp) {
                            float width = 0.48f * screenWidth * 0.9f, height = 0.54f * width * 0.9f;
                            float seconds, xPos, yPos;
                            xPos = 0.14f * Constants.VIEWPORT_WIDTH;
                            yPos = 0.3f * Constants.VIEWPORT_HEIGHT;

                            width *= 1.7;
                            height *= 1.7;

                            String iceObsExplanationPath = popUpFilePrefix + "ice_obstacle_explanation.png";
                            // TODO, -1..... mmmm
                            preExplanation = new PopUp(iceObsExplanationPath, -1,
                                    xPos, yPos, width, height, ((CooperativeGame) Gdx.app.getApplicationListener()).
                                    crossPlatformObjects.getUtils(), false, popUpsMinimalTime);
                            paths.add(iceObsExplanationPath);

                            preExplanation.addListener(new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                if(preExplanation.isMinimalTimePassed()){
                                    preExplanation.setFinished(true);
                                    preExplanation.remove();
                                    music.toneUpBackgroundMusic();
                                    switchToRunningState(false);
                                    generalHumanAsking = GeneralHumanAsking.Waitt;
                                }
                                }
                            });
                            helpButton.addListener(new ClickListener() {
                                @Override
                                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                                preExplanation.setFinished(true);
                                preExplanation.remove();
                                if(nowLetsSee != null){
                                    nowLetsSee.remove();
                                }

                                // now click on the locked coin...
                                //pauseButton.add();
                                getHelpButton().remove();
                                arrow.remove();
                                generalHumanAsking = GeneralHumanAsking.NowClick;
                                return true;
                                }
                            });
                            stage.addActor(helpButton);
                            // add arrow.
                            stage.addActor(arrow);

                            music.playNotificationSound();

                            stage.addActor(preExplanation);
                            switchToPauseState(false);
                            generalHumanAsking = GeneralHumanAsking.Waitt;
                        }

                        else if(generalHumanAsking == GeneralHumanAsking.NowClick){
                            float width = 0.48f * screenWidth * 0.9f, height = 0.54f * width * 0.9f;
                            float seconds, xPos, yPos;
                            xPos = 0.14f * Constants.VIEWPORT_WIDTH;
                            yPos = 0.35f * Constants.VIEWPORT_HEIGHT;

                            width *= 1.7;
                            height *= 1.7;

                            String nowClickPath = popUpFilePrefix + "now_click_Bob.png";
                            // TODO, -1..... mmmm
                            nowClickPopUp = new PopUp(nowClickPath, -1,
                                    xPos, yPos, width, height, ((CooperativeGame) Gdx.app.getApplicationListener()).
                                    crossPlatformObjects.getUtils(), false, popUpsMinimalTime);
                            paths.add(nowClickPath);
                            nowClickPopUp.addListener(new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                if(nowClickPopUp.isMinimalTimePassed()){
                                    //getPauseButton().remove(); // TODO - should we check if the button is on?

                                    nowClickPopUp.setFinished(true);
                                    nowClickPopUp.remove();
                                    music.toneUpBackgroundMusic();
                                    generalHumanAsking = GeneralHumanAsking.Waitt;
                                }
                                }
                            });

                            nowClickPopUp.init();
                            music.playNotificationSound();

                            stage.addActor(nowClickPopUp);
                            //switchToPauseState();

                            generalHumanAsking = GeneralHumanAsking.Waitt;
                            break;
                        }

                        else if(generalHumanAsking == GeneralHumanAsking.CreatePostExplanationPopUp) {
                            if (getHelpRequests().size() == 1) {
                                float width = 0.48f * screenWidth * 0.9f, height = 0.54f * width * 0.9f;
                                float seconds, xPos, yPos;
                                xPos = 0.14f * Constants.VIEWPORT_WIDTH;
                                yPos = 0.35f * Constants.VIEWPORT_HEIGHT;

                                width *= 1.7;
                                height *= 1.7;

                                String postfix = "_" + (CooperativeGame.gameData.
                                        getGame_params().getReciprocityValues().
                                        getHelp_providing_cost_virtual());

                                String bobAgreedPath = popUpFilePrefix + "Bob_agreed" + postfix + ".png";
                                // TODO, -1..... mmmm
                                // TODO - change popUpName.
                                final PopUp popUp = new PopUp(bobAgreedPath, -1,
                                        xPos, yPos, width, height, ((CooperativeGame) Gdx.app.getApplicationListener()).
                                        crossPlatformObjects.getUtils(), false, popUpsMinimalTime);
                                paths.add(bobAgreedPath);

                                popUp.addListener(new ClickListener() {
                                    @Override
                                    public void clicked(InputEvent event, float x, float y) {
                                        if(popUp.isMinimalTimePassed()){
                                            popUp.setFinished(true);
                                            popUp.remove();
                                            music.toneUpBackgroundMusic();
                                            switchToRunningState(true);
                                            // start time counting.
                                            startTime = System.currentTimeMillis();

                                            generalHumanAsking = GeneralHumanAsking.WaitForLetsSee;
                                        }
                                    }
                                });

                                popUp.init();
                                music.playNotificationSound();

                                stage.addActor(popUp);
                                switchToPauseState(false);

                                generalHumanAsking = GeneralHumanAsking.Waitt;
                            }
                        }

                        else if(generalHumanAsking == GeneralHumanAsking.WaitForLetsSee){
                            // after special coin collecting.
                            if(timeHasPassedLetsSee()){
                                generalHumanAsking = GeneralHumanAsking.LetsSee;
                            }
                        }

                        else if(generalHumanAsking == GeneralHumanAsking.LetsSee){
                            float width = 0.48f * screenWidth * 0.9f, height = 0.54f * width * 0.9f;
                            float seconds, xPos, yPos;
                            xPos = 0.14f * Constants.VIEWPORT_WIDTH;
                            yPos = 0.3f * Constants.VIEWPORT_HEIGHT;

                            width *= 1.7;
                            height *= 1.7;

                            String nowLetsSeePath = popUpFilePrefix + "now_lets_see.png";
                            // TODO, -1..... mmmm
                            nowLetsSee = new PopUp(nowLetsSeePath, -1,
                                    xPos, yPos, width, height, ((CooperativeGame) Gdx.app.getApplicationListener()).
                                    crossPlatformObjects.getUtils(), false, popUpsMinimalTime);
                            paths.add(nowLetsSeePath);
                            nowLetsSee.addListener(new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                if(nowLetsSee.isMinimalTimePassed()){
                                    nowLetsSee.setFinished(true);
                                    nowLetsSee.remove();
                                    music.toneUpBackgroundMusic();
                                    switchToRunningState(false);
                                    preExplanationIsOn = false;

                                    generalHumanAsking = GeneralHumanAsking.Waitt;
                                }
                                }
                            });

                            stage.addActor(helpButton);
                            // add arrow.
                            stage.addActor(arrow);

                            music.playNotificationSound();

                            stage.addActor(nowLetsSee);
                            preExplanationIsOn = true;

                            switchToPauseState(false);
                            generalHumanAsking = GeneralHumanAsking.Waitt;
                        }

                        else if(generalHumanAsking == GeneralHumanAsking.After){
                            humanAskingInnerState = HumanAskingState.ForceReject;
                            generalHumanAsking = GeneralHumanAsking.Before;

                        }

                        break;

                    case ForceReject:
                        if(generalHumanAsking == GeneralHumanAsking.Before){
                            //elementsRemover.generateAllCoin();
                            //elementsRemover.generateAllVirtualCoins();

                            generalHumanAsking = GeneralHumanAsking.CreatePreExplanationPopUp;

                            break;
                        }

                        else if(generalHumanAsking == GeneralHumanAsking.CreatePreExplanationPopUp){
                            if(getHelpRequests().size() == 2){
                                float width = 0.48f * screenWidth * 0.9f, height = 0.54f * width * 0.9f;
                                float seconds, xPos, yPos;
                                xPos = 0.14f * Constants.VIEWPORT_WIDTH;
                                yPos = 0.35f * Constants.VIEWPORT_HEIGHT;

                                width *= 1.7;
                                height *= 1.7;

                                String bobIgnoredPath = popUpFilePrefix + "Bob_ignored.png";
                                // TODO, -1..... mmmm
                                final PopUp bobIgnored = new PopUp(bobIgnoredPath, -1,
                                        xPos, yPos, width, height, ((CooperativeGame) Gdx.app.getApplicationListener()).
                                        crossPlatformObjects.getUtils(), false, popUpsMinimalTime);
                                paths.add(bobIgnoredPath);
                                bobIgnored.addListener(new ClickListener(){
                                    @Override
                                    public void clicked(InputEvent event, float x, float y) {
                                    if(bobIgnored.isMinimalTimePassed()){
                                        bobIgnored.setFinished(true);
                                        bobIgnored.remove();
                                        music.toneUpBackgroundMusic();
                                        switchToRunningState(true);
                                        generalHumanAsking = GeneralHumanAsking.After;
                                    }
                                    }
                                });

                                bobIgnored.init();
                                music.playNotificationSound();

                                stage.addActor(bobIgnored);
                                switchToPauseState(false);
                                generalHumanAsking = GeneralHumanAsking.Waitt;
                            }
                        }
                        else if(generalHumanAsking == GeneralHumanAsking.After){
                            // after special coin collecting.
                            generalHumanAsking = GeneralHumanAsking.Before;
                            if(humanAskingInnerState == HumanAskingState.ForceReject){
                                instructionalStepCurrState = VirtualAsking;
                                this.virtualPlayer.b2body_boy.setTransform(new Vector2(11, 2), 0);
                                elementsRemover.removeAllVirtualCoins();
                            }
                        }
                        break;
                }
                break;

            case VirtualAsking:
                if(state == State.Running){
                    elementsGenerationManager.addCollectibleElement(MatrixBuilder.cellType.coins);
                }
                switch(virtualAskingState){
                    case ForceAccept:
                        if(generalVirtualAsking == GeneralVirtualAsking.Before){
                            getVirtualPlayer().getVirtualPlayerRequestUtils().getHelpRequestPopUp().forceYes();
                            generalVirtualAsking = GenerateLockedCoin;
                            break;
                        }

                        else if(generalVirtualAsking == GenerateLockedCoin) {
                                if(regularObstaclesCreated < 2){
                                    if(elementsRemover.createOneNatureObstacle()){
                                        getPauseButton().remove(); // TODO - should we check if the button is on?

                                        regularObstaclesCreated++;
                                        if(regularObstaclesCreated == 2){
                                            // start time counting.
                                            startTime = System.currentTimeMillis();


                                            int nn = thanksBubble.getBubble().getListeners().size;
                                            // after Bob collects the ice cube.
                                            thanksBubble.getBubble().addListener(new EventListener() {
                                                @Override
                                                public boolean handle(Event event) {
                                                    if(!visitedHandle){
                                                        Timer.schedule(new Timer.Task() {
                                                            @Override
                                                            public void run() {
                                                                // influence ForceReject state.
                                                                generalVirtualAsking = GeneralVirtualAsking.CreatePopUp;
                                                                switchToPauseState(false);
                                                            }
                                                        }, 2.5f); // delay in seconds.
                                                        generalVirtualAsking = GeneralVirtualAsking.Waitt;

                                                        visitedHandle = true;
                                                    }
                                                    else{
                                                        getLevelLog().addToComments("visitedHandle|");
                                                    }
                                                    return true;
                                                }
                                            });
                                        }
                                    }
                                }
                                else{
                                    // if time has passed - create pop-up.
                                    if(timeHasPassedRegularObstacles()){ //18.5.21- this is for the first pop-up (in ForceAccept).
                                        generalVirtualAsking = GeneralVirtualAsking.CreatePopUp;
                                    }
                                }
                            break;
                        }

                        else if(generalVirtualAsking == GeneralVirtualAsking.CreatePopUp){
                                float width = 0.48f * screenWidth * 0.9f, height = 0.54f * width * 0.9f;
                                float seconds, xPos, yPos;
                                xPos = 0.14f * Constants.VIEWPORT_WIDTH;
                                yPos = 0.35f * Constants.VIEWPORT_HEIGHT;

                                width *= 1.7;
                                height *= 1.7;

                                String postfix = "_" + (CooperativeGame.gameData.
                                        getGame_params().getReciprocityValues().
                                        getHelp_providing_cost_human());

                                String helpBobPath = popUpFilePrefix + "help_Bob" + postfix + ".png";
                                // TODO, -1..... mmmm
                                final PopUp helpPopUp = new PopUp(helpBobPath, -1,
                                        xPos, yPos, width, height, ((CooperativeGame) Gdx.app.getApplicationListener()).
                                        crossPlatformObjects.getUtils(), false, popUpsMinimalTime);
                                paths.add(helpBobPath);
                                helpPopUp.addListener(new ClickListener(){
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    helpPopUp.setFinished(true);
                                    helpPopUp.remove();

                                    music.toneUpBackgroundMusic();
                                    switchToRunningState(false);
                                    //generalVirtualAsking = GeneralVirtualAsking.After;
                                    }
                                });

                                helpPopUp.init();
                                music.playNotificationSound();

                                stage.addActor(helpPopUp);
                                // just now, after the popup disappered - it is OK that the
                                // virtual player will ask for help.
                                virtualAllowedToAsk = true;

                                switchToPauseState(false);
                                generalVirtualAsking = GeneralVirtualAsking.Waitt;
                        }
                        else if(generalVirtualAsking == GeneralVirtualAsking.Waitt){

                        }

                        else if(generalVirtualAsking == GeneralVirtualAsking.After){
                            //switchToPauseState();
                            virtualAskingState = VirtualAskingState.ForceReject;
                            generalVirtualAsking = GeneralVirtualAsking.Before;
                        }

                        break;


                    case ForceReject:
                        if(generalVirtualAsking == GeneralVirtualAsking.Before){
                            getVirtualPlayer().getVirtualPlayerRequestUtils().getHelpRequestPopUp().forceNo();
                            generalVirtualAsking = GeneralVirtualAsking.GenerateLockedCoin;
                        }

                        else if(generalVirtualAsking == GenerateLockedCoin) {

                        }

                        else if(generalVirtualAsking == GeneralVirtualAsking.CreatePopUp){ // TODO TEST IT!
                            float width = 0.48f * screenWidth * 0.9f, height = 0.54f * width * 0.9f;
                            float seconds, xPos, yPos;
                            xPos = 0.14f * Constants.VIEWPORT_WIDTH;
                            yPos = 0.35f * Constants.VIEWPORT_HEIGHT;

                            width *= 1.7;
                            height *= 1.7;

                            String postfix = "_" + (CooperativeGame.gameData.
                                getGame_params().getReciprocityValues().
                                getHelp_providing_cost_human());

                            String dontHelpPath = popUpFilePrefix + "dont_help_Bob" + postfix +".png";
                            // TODO, -1..... mmmm
                            final PopUp dontHelpPopUp = new PopUp(dontHelpPath, -1,
                                xPos, yPos, width, height, ((CooperativeGame) Gdx.app.getApplicationListener()).
                                crossPlatformObjects.getUtils(), false, popUpsMinimalTime);
                            paths.add(dontHelpPath);
                            dontHelpPopUp.addListener(new ClickListener(){
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                if(dontHelpPopUp.isMinimalTimePassed()){
                                    dontHelpPopUp.setFinished(true);
                                    dontHelpPopUp.remove();
                                    music.toneUpBackgroundMusic();
                                    switchToRunningState(false);
                                }
                                }
                            });

                            dontHelpPopUp.init();
                            music.playNotificationSound();

                            stage.addActor(dontHelpPopUp);
                            switchToPauseState(false);
                            generalVirtualAsking = GeneralVirtualAsking.Waitt;

                            break;
                        }
                        // state == running is important here to ensure that the pop-up has deleted.
                        else if((generalVirtualAsking == GeneralVirtualAsking.After) && state == State.Running) {
                            float width = 0.48f * screenWidth * 0.9f, height = 0.54f * width * 0.9f;
                            float seconds, xPos, yPos;
                            xPos = 0.14f * Constants.VIEWPORT_WIDTH;
                            yPos = 0.35f * Constants.VIEWPORT_HEIGHT;

                            width *= 1.7;
                            height *= 1.7;

                            String humanIgnoredPath = popUpFilePrefix + "human_ignored.png";
                            // TODO, -1..... mmmm
                            final PopUp humanIgnored = new PopUp(humanIgnoredPath, -1,
                                    xPos, yPos, width, height, ((CooperativeGame) Gdx.app.getApplicationListener()).
                                    crossPlatformObjects.getUtils(), false, popUpsMinimalTime);
                            paths.add(humanIgnoredPath);
                            humanIgnored.addListener(new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                if(humanIgnored.isMinimalTimePassed()){
                                    humanIgnored.setFinished(true);
                                    humanIgnored.remove();
                                    music.toneUpBackgroundMusic();
                                    switchToRunningState(true);
                                }
                                }
                            });

                            humanIgnored.init();
                            music.playNotificationSound();

                            stage.addActor(humanIgnored);
                            // start time counting.
                            startTime = System.currentTimeMillis();
                            generalVirtualAsking = WaitUntilLevelEnd;
                            switchToPauseState(false);
                            break;
                        }
                        if(generalVirtualAsking == WaitUntilLevelEnd){
                            if(timeHasPassedEndLevel()){
                                setState(LevelEnded);
                            }
                            else{
                                if(state == State.Running){
                                    elementsGenerationManager.addCollectibleElement(MatrixBuilder.cellType.virtualcoins);
                                }
                            }
                        }

                        // collect!
                        break;
                }
                break;
        }

        super.render(delta);
    }

    public InstructionalStepState getInstructionalStepCurrState() {
        return instructionalStepCurrState;
    }

    public void setGeneralVirtualAsking(GeneralVirtualAsking generalVirtualAsking) {
        this.generalVirtualAsking = generalVirtualAsking;
    }

    public boolean timeHasPassedRegularObstacles(){
        // check how much time has pass from the last virtual player ask.
        float passedTime = (int) ((System.currentTimeMillis() - startTime) / 1000);
        if(passedTime >= timeRegularObstacles){
            return true;
        }
        return false;
    }

    public boolean timeHasPassedIceObstacles(){
        // check how much time has pass from the last virtual player ask.
        float passedTime = (int) ((System.currentTimeMillis() - startTime) / 1000);
        if(passedTime >= timeIceObstacles){
            return true;
        }
        return false;
    }

    public boolean timeHasPassedEndLevel(){
        // check how much time has pass from the last virtual player ask.
        float passedTime = (int) ((System.currentTimeMillis() - startTime) / 1000);
        if(passedTime >= timeEndLevel){
            return true;
        }
        return false;
    }

    public boolean timeHasPassedLetsSee(){
        // check how much time has pass from the last virtual player ask.
        float passedTime = (int) ((System.currentTimeMillis() - startTime) / 1000);
        if(passedTime >= timeLetsSee){
            return true;
        }
        return false;
    }

    // pause
    // sound
    // pop up for X seconds
    // continue

    @Override
    public void dispose() {
        CooperativeGame.allResponses.clear();
        CooperativeGame.humanResponses.clear();
        CooperativeGame.virtualResponses.clear();

        String diagnosticts1, diagnosticts2;
        diagnosticts1 = CooperativeGame.loader.getAssetManager().getDiagnostics();
        String javaHeap1, nativeHeap1, javaHeap2, nativeHeap2;
        javaHeap1 = String.valueOf(Gdx.app.getJavaHeap());
        nativeHeap1 = String.valueOf(Gdx.app.getNativeHeap());

        stage.dispose();

        for(String path : paths){
            GraphicTools.getTexture(path);
        }

        diagnosticts2 = CooperativeGame.loader.getAssetManager().getDiagnostics();
        javaHeap2 = String.valueOf(Gdx.app.getJavaHeap());
        nativeHeap2 = String.valueOf(Gdx.app.getNativeHeap());
        diagnosticts2 += "";
    }
}
