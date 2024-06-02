package coopworld.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.HashMap;

import coopworld.game.Configuration.ConfigurationsExtractor;
import coopworld.game.Configuration.DevelopmentConfig;
import coopworld.game.CrossPlatform.CrossPlatformObjects;
import coopworld.game.Logs.Enums;
import coopworld.game.Logs.GameData;
import coopworld.game.Logs.HelpRequests.HelpRequest;
import coopworld.game.Logs.LogsBuilder;
import coopworld.game.Networking.Connection;
import coopworld.game.Networking.ConnectionKinderGardens;
import coopworld.game.Networking.ResponseListeners.WebsiteStatusListener;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.Loader;
import coopworld.game.Tools.MapsDetails;
import coopworld.game.Tools.Paths;

/*
 * Class Name: CooperativeGame
 * extends Game class, set default values of the game, and load game assets.
 */
public class CooperativeGame extends Game {
    public CrossPlatformObjects crossPlatformObjects;
    public SpriteBatch spriteBatch;
    //public static float gameWidth;
    public static Connection conn;

    //public static float gameHeight;
    // Pixels Per Meter, for the velocity, big number - fast moving. small number - slow moving.
    public static final float PPM = 100;

    public static HashMap<Integer, String> levelsPath = new HashMap<Integer, String>();

    public static HashMap<String, String> eventsSounds = new HashMap<String, String>();

    public static Loader loader;

    public static GameData gameData;
    private LogsBuilder logsBuilder;
    // initialize first positions of the players.
    public static MapsDetails mapsDetails;

    public static DevelopmentConfig developmentConfig;

    public static String languagePrefix;

    public static ArrayList<HelpRequest> humanResponses;
    public static ArrayList<HelpRequest> virtualResponses;
    public static ArrayList<HelpRequest> allResponses;

    private static boolean timerIsOn; // determine if the level is ended when time is over.

    public static String virtualPlayerGender;

    public CooperativeGame(CrossPlatformObjects crossPlatformObjects) {
        this.crossPlatformObjects = crossPlatformObjects;
    }

    public CrossPlatformObjects getCrossPlatformObjects() {
        return crossPlatformObjects;
    }

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.log("Lifecycle", "hello!!");

        if(Constants.USER_ID_WEB == "TEST"){ // TODO - REMOVE IN PROD
            Constants.USER_ID_WEB = "chen-demo";
        }

        Gdx.input.setCatchBackKey(true);
        //Gdx.input.setCatchKey(Input.Keys.BACK, true);
        spriteBatch = new SpriteBatch();
        gameData = new GameData();
        loader = new Loader();
        logsBuilder = new LogsBuilder();
        mapsDetails = new MapsDetails();
        timerIsOn = true;

        // Set game height and width.
        //gameWidth = Constants.VIEWPORT_WIDTH;
        //gameHeight = Constants.VIEWPORT_HEIGHT;
        float density = Gdx.graphics.getDensity();

        humanResponses = new ArrayList<HelpRequest>();
        virtualResponses = new ArrayList<HelpRequest>();
        allResponses = new ArrayList<HelpRequest>();

        mapsDetails.setInitialPositions();

        // initialize paths (important to avoid duplicate concat).
        Paths.init();

        // initialize the wanted values for GameData object.
        logsBuilder.initGameData();

        // initialize levelsPath array.
        initLevelsMaps();

        // Save the strings names of the relevant assets elements.
        setExternalElements();

        // Load needed assets.
        loader.load();

        developmentConfig = ConfigurationsExtractor.getDevelopmentConfig();

        //dispose();
        this.conn = new ConnectionKinderGardens(this);
        this.conn.setDebugMode(!developmentConfig.data_storing);


        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                // Log the exception or handle the error gracefully.
                Gdx.app.error("UncaughtException", "Caught an uncaught exception", throwable);

                // Print out the error message of the exception
                String errorMessage = throwable.getMessage();
                Gdx.app.error("UncaughtException", "Error message: " + errorMessage);

            }
        });

        /*
        try {
            CooperativeGame.conn.sendEntranceData(Constants.ENTRANCE_DATA);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        String gp = conn.makeString(CooperativeGame.gameData.getGame_params());

        Constants.VIEWPORT_WIDTH = Gdx.graphics.getWidth();
        Constants.VIEWPORT_HEIGHT = Gdx.graphics.getHeight();

        // TODO 2021 - WRITE A FUNCTION FOR THIS.
        this.conn.getWebsiteStatus(new WebsiteStatusListener(this));
        //((CooperativeGame) Gdx.app.getApplicationListener()).crossPlatformObjects.getUtils().getUser(Constants.USER_ID_WEB);
    }

    public void initLevelsMaps(){
        levelsPath.put(1, "Level1_combined.tmx");
        levelsPath.put(2, "regular_level_1.tmx");
        levelsPath.put(3, "colored_forest_level_1.tmx");
        levelsPath.put(4, "colored_forest_level_2.tmx");
        levelsPath.put(5, "candy_level_1.tmx");
        levelsPath.put(6, "candy_level_2.tmx");
        levelsPath.put(7, "beach_level_1.tmx");
        levelsPath.put(8, "beach_level_2.tmx");

        /*
        // regular
        levelsPath.put(1, "Level1_combined.tmx");
        levelsPath.put(2, "Level2_combined.tmx");

        // colored forest
        levelsPath.put(3, "colored_forest_level.tmx");
        levelsPath.put(4, "colored_forest_level2.tmx");

        // ice
        //levelsPath.put(5, "Level5-ice.tmx");
        //levelsPath.put(6, "Level2-ice.tmx");

        // beach
        levelsPath.put(5, "beach_level.tmx");
        levelsPath.put(6, "beach_level2.tmx");

        // desert
        //levelsPath.put(9, "desert_level2.tmx");
        //levelsPath.put(10, "desert_level.tmx");

        // candies
        levelsPath.put(7, "candy_level2.tmx");
        levelsPath.put(8, "candy_level.tmx");

         */
    }

    public void setExternalElements(){
        // DEFAULT VALUES for files using.
        if(Constants.GAME_LANGUAGE.getLanguage() == Enums.Language.English){
            languagePrefix = "eng_res/";
        }
        else if(Constants.GAME_LANGUAGE.getLanguage() == Enums.Language.Hebrew){
            languagePrefix = "heb_res/";
        }
        else if(Constants.GAME_LANGUAGE.getLanguage() == Enums.Language.Arabic){
            languagePrefix = "ar_res/";
        }
        else if(Constants.GAME_LANGUAGE.getLanguage() == Enums.Language.Macedonian){
            languagePrefix = "mk_res/";
        }
        else{
            languagePrefix = "heb_res/";
        }

        eventsSounds.put("levelCompleted", languagePrefix + "sounds/level_completed.mp3");
        eventsSounds.put("thanks", languagePrefix + "instructions_sounds/thanks.mp3");
        eventsSounds.put("coming", languagePrefix + "instructions_sounds/coming.mp3");
        eventsSounds.put("helpAsking1", languagePrefix + "instructions_sounds/help1 - collision with ice obs.mp3");
        eventsSounds.put("helpAsking2", languagePrefix + "instructions_sounds/help2 - choose coin.mp3");
    }

    // Render method of the super class (Game).
    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        //System.out.println("DISPOSE GAME!");
        super.dispose();
    }

    @Override
    public void pause() {

        int v = 1;
        v++;
    }
}
