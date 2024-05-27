package coopworld.game.Screens.GameScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import coopworld.game.Buttons.BackButton;
import coopworld.game.Buttons.HelpButton;
import coopworld.game.Buttons.PauseButton;
import coopworld.game.Buttons.VolumeButton;
import coopworld.game.CooperativeGame;
import coopworld.game.CrossPlatform.Input;
import coopworld.game.Logs.ElementCollectionList;
import coopworld.game.Logs.Enums;
import coopworld.game.Logs.HelpRequests.HelpRequest;
import coopworld.game.Logs.LevelLog;
import coopworld.game.Logs.LevelParams;
import coopworld.game.Logs.LevelStart;
import coopworld.game.Logs.Managers.GameDataUpdater;
import coopworld.game.Logs.OnOffEvent;
import coopworld.game.Logs.VirtualPlayerConfig;
import coopworld.game.Scenes.BasicAnimatedImage;
import coopworld.game.Scenes.Hud;
import coopworld.game.Screens.EndLevel;
import coopworld.game.Sprites.Bubbles.CanYouHelpMeBubble;
import coopworld.game.Sprites.Bubbles.ThanksBubble;
import coopworld.game.Sprites.Coin;
import coopworld.game.Sprites.IceObstacle;
import coopworld.game.Sprites.LockedCoin;
import coopworld.game.Sprites.LockedVirtualCoin;
import coopworld.game.Sprites.NatureObstacle;
import coopworld.game.Sprites.Players.IceBoyPlayer;
import coopworld.game.Sprites.Players.IceGirlPlayer;
import coopworld.game.Sprites.Players.Personalities.ALLC_virtualPlayer;
import coopworld.game.Sprites.Players.Personalities.ALLD_virtualPlayer;
import coopworld.game.Sprites.Players.Personalities.Alternately_virtualPlayer;
import coopworld.game.Sprites.Players.Personalities.PROBABILITY_virtualPlayer;
import coopworld.game.Sprites.Players.Personalities.ProbabilisticTFT_virtualPlayer;
import coopworld.game.Sprites.Players.Personalities.TFT_virtualPlayer;
import coopworld.game.Sprites.Players.Player;
import coopworld.game.Sprites.Players.RegularBoyPlayer;
import coopworld.game.Sprites.Players.RegularGirlPlayer;
import coopworld.game.Sprites.Players.VirtualPlayer;
import coopworld.game.Sprites.VirtualPlayerCoin;
import coopworld.game.Tools.AiTools.AStar;
import coopworld.game.Tools.AiTools.DataStructures.Node;
import coopworld.game.Tools.AiTools.Guide;
import coopworld.game.Tools.AiTools.MatrixBuilder;
import coopworld.game.Tools.AiTools.MatrixCell;
import coopworld.game.Tools.AiTools.SearchableMatrix;
import coopworld.game.Tools.AskingOrderManager;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.CountdownTimer;
import coopworld.game.Tools.ElementsGeneration.ElementsGenerationManager;
import coopworld.game.Tools.ElementsGeneration.ElementsRemover;
import coopworld.game.Tools.ElementsGeneration.RandomGenerator;
import coopworld.game.Tools.FpsCalculator;
import coopworld.game.Tools.GraphicTools;
import coopworld.game.Tools.Helping;
import coopworld.game.Tools.InputProcessors.InputProcessorGameScreen;
import coopworld.game.Tools.MapsDetails;
import coopworld.game.Tools.MapsUtils;
import coopworld.game.Tools.MusicPlayer;
import coopworld.game.Tools.ObstaclesManager;
import coopworld.game.Tools.PersistentData;
import coopworld.game.Tools.PopUp;
import coopworld.game.Tools.StarsLogic;
import coopworld.game.Tools.TiledAnimations.ObstacleAnimation;
import coopworld.game.Tools.Utils;
import coopworld.game.Tools.WorldBuilder;
import coopworld.game.Tools.WorldContactListener;

import static coopworld.game.Screens.GameScreens.CombinedInstructions.GeneralHumanAsking.CreatePostExplanationPopUp;
import static coopworld.game.Screens.GameScreens.CombinedInstructions.InstructionalStepState.CoinsCollecting;

/*
 * Class Name: GameScreen
 * implements screen, represents the game screen.
 */
public class GameScreen implements Screen {
    private CooperativeGame game;
    private OrthographicCamera orthographicCamera;
    /* Keep the ratio between different size of screens. */
    public static Viewport viewPort;
    public MusicPlayer music;
    /* Will load our map to the game. */
    protected TmxMapLoader tmxMapLoader;
    /* Reference to the map. */
    protected TiledMap tiledMap;
    /* Render our map to the screen.*/
    protected OrthogonalTiledMapRenderer tiledMapRenderer;
    private World world;
    private static Player player;
    protected static VirtualPlayer virtualPlayer;
    public static Stage stage;
    Skin skin;
    // todo STRICT DEBUG!!!
    //// SCORE ////
    public static int humanScore, virtualScore;
    public static int accumulatedHumanScore, accumulatedVirtualScore;

    private Label scoreVirtualLabel;
    private int numOfStars;
    //// HUMAN  ////
    public static int humanAsksForHelp = 0, humanHelped = 0;
    public static int humanArrivedIceObstacle = 0, virtualArrivedNatureObstacle = 0;
    public static boolean humanArrivedIceObstacleFlag = false;
    public static boolean waitForHelpButtonTouch = false;
    // AI MOVING //
    public static MatrixBuilder m;
    public static SearchableMatrix searchableMatrix;
    private Guide guide;
    private static AStar astar;
    //RANDOM GENERATOR USAGE
    private RandomGenerator randomGenerator;
    // minimal time between two virtual help requests.
    private float VIRTUAL_ASKS_DELAY = 6f;
    //// ASK FOR HELP ///////////////
    protected static boolean waitingForHelp;
    //////////////////////////////
    // real player - boy or girl.
    Enums.Gender pType;
    /* For buttons position. */
    float screenHeight = Constants.VIEWPORT_HEIGHT;
    float screenWidth = Constants.VIEWPORT_WIDTH;
    private float deltaTime = 0;

    boolean limitWalking = false;
    double maxVirtualJump = (screenHeight * 0.9) / CooperativeGame.PPM;
    public static int mapWidth, mapHeight;
    public static int mapCellWidth, mapCellHeight;
    private Box2DDebugRenderer b2dr;
    // delete HowToHelp?
    public enum State {
        Running, Paused, HelpAsk, LevelEnded, VirtualAskForHelp, HumanAskForHelp,
        TrainingHumanAsking,
        WaitForHelpTouch, HowToHelp, HelpAskTraining, SpecialCoinExplanation, BeforeEnd
    }
    protected State state;
    public enum VirtualAskState {
        JustAsked, BubbleDisplay, PopUpDisplay, RemoveNatureObstacle,
        RemoveNatureObstacleAndVirtualCoin, HelpExplanation, Wait, HandleHumanRespone
    }
    public VirtualAskState virtualAskState;
    public enum HumanAskState{
        VirtualAccepted, VirtualIgnored, VirtualResponseDisplay, HelpRequestAccepted,
        VirtualResponseDeclaration, Prepare, CheckTouchInput
    }
    public static HumanAskState humanAskState;
    public enum ReciprocityState{
        Init, Wait, BeforeHelp, ObstacleCreation, DuringHelp, AfterHelp, End
    }
    private ReciprocityState reciprocityState;

    long currentTime = 0;
    long lastVirtualAskTime = 0;

    boolean showScoreBoolean = false;
    public static float remainingTimeFloat;
    boolean levelEnded = false;
    public static int levelNum;

    public static int displayedCoins, displayedVirtualCoins, displayedNatureObstacles,
            displayedIceObstacles;
    boolean cheat = false;
    Animation animation;
    TextureAtlas atlas;
    boolean buttons = true;
    public static TextButton helpButton = null;
    private LevelParams levelParams;
    private static LevelLog levelLog;
    public boolean showThanksButton = false;
    public static boolean addThanksButton = false;
    private ObstacleAnimation iceObsAnimation;
    protected ThanksBubble thanksBubble;
    protected CanYouHelpMeBubble canYouHelpMeBubble;
    public static boolean newScore;
    private Texture pauseFrameTexture;
    // objects arrays.
    private static HashMap<NatureObstacle, LockedVirtualCoin> natureObstacles;
    private static HashMap<IceObstacle, LockedCoin> iceObstacles;
    private static ArrayList<Coin> coins;
    private static ArrayList<VirtualPlayerCoin> virtualCoins;

    private BasicAnimatedImage animatedIceCubeThrowing;
    private ArrayList<HelpRequest> helpRequests = new ArrayList<HelpRequest>();
    private MapsDetails mapsDetails;
    private PopUp virtualAnswerDeclaration, virtualIgnoreDeclaration;

    protected boolean isInstructionLevel;
    private boolean timerIsOn;
    protected ElementsGenerationManager elementsGenerationManager;
    Hud hud;
    private Input movmentInputManager;
    private ObstaclesManager obstaclesManager;
    private WorldBuilder worldBuilder;
    private int humanNumRequests, virtualNumRequests;
    protected ElementsRemover elementsRemover;
    VirtualPlayerConfig virtualPlayerConfig;
    public String virtualGenderPath;

    float virtPlayerXFloatPosition, virtPlayerYFloatPosition;

    private FpsCalculator fpsCalculator;
    private ArrayList<OnOffEvent> mute_events, pause_events;
    PauseButton pauseButton;
    VolumeButton volumeButton;
    InputMultiplexer inputMultiplexer;
    GameDataUpdater gameDataUpdater;

    private boolean justResumed = false, last_called = false;
    private ElementCollectionList elementCollectionList = new ElementCollectionList();

    CountdownTimer countdownTimer;

    // todo - end of declarations!
    /**
     * Constructor with the game, world type and player type.
     *
     * @param  game - the game.
     *
     */
    public GameScreen(CooperativeGame game, LevelParams levelParams) {
        this.levelNum = levelParams.getLevel_num();
        this.waitingForHelp = false;
        this.levelLog = new LevelLog(CooperativeGame.gameData.getUser().getUser_id());
        this.levelParams = levelParams;

        music = MusicPlayer.getInstance();
        if(music.isSoundIsOn()){
            music.playBackgroundMusic();
        }

        this.pType = CooperativeGame.gameData.getUser().getHuman_gender();

        this.game = game;
        this.orthographicCamera = new OrthographicCamera();
        this.tmxMapLoader = new TmxMapLoader();

        this.humanScore = 0;
        this.virtualScore = 0;

        this.accumulatedHumanScore = CooperativeGame.gameData.getCumulativeScores().getHuman_player_score();
        this.accumulatedVirtualScore = CooperativeGame.gameData.getCumulativeScores().getVirtual_player_score();

        // initial states.
        state = State.Running;
        virtualAskState = VirtualAskState.JustAsked;
        humanAskState = HumanAskState.Prepare;
        reciprocityState = ReciprocityState.Init;

        /* Get the map path string according to the inserted level number.
        The mapping <num, path> is in CooperativeGame (HashMap). */
        // If the map doesn't exists - take the default.
        String mapName;
        if(CooperativeGame.levelsPath.get(levelNum) != null){
            mapName = CooperativeGame.levelsPath.get(levelNum);
        }
        else{
            //default map!
            mapName = "6.tmx";
        }
        String mapPath = "tiled_maps/" + mapName;
        levelLog.setMap_name(mapName);

        this.getLevelLog().setLevel_type(MapsUtils.getMapType(mapName));

        tiledMap = tmxMapLoader.load(mapPath);

        displayedCoins = 0;
        displayedVirtualCoins = 0;
        displayedNatureObstacles = 0;
        displayedIceObstacles = 0;

        humanArrivedIceObstacleFlag = false;
        /// AI MOVING /////////////////////////////////////////////
        this.m = new MatrixBuilder(mapPath, tmxMapLoader, this);
        this.searchableMatrix = m.buildSearchableMatrix();
        //this.virtualCoinCells = m.computeVirtualCoinCells();
        this.guide = new Guide();
        this.astar = new AStar();

        //////////////////////////////////////////////////////////

        /* Get the Map Properties from our Tiled Map. */
        MapProperties mapProperties = tiledMap.getProperties();
        /* Grab the Map Width. */
        this.mapWidth = mapProperties.get("width", Integer.class);
        this.mapHeight = mapProperties.get("height", Integer.class);
        this.mapCellWidth = mapProperties.get("tilewidth", Integer.class); //64
        this.mapCellHeight = mapProperties.get("tileheight", Integer.class); //64

       // ArrayList<Node> randomIcePositions = getVirtualCoinsPositions(map.get(wType));

        this.viewPort = new FitViewport(this.mapWidth * this.mapCellWidth /
                CooperativeGame.PPM, this.mapHeight * this.mapCellHeight /
                CooperativeGame.PPM,
                orthographicCamera);

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/
                CooperativeGame.PPM);
        /* Place the camera to the center. */
        orthographicCamera.position.set(viewPort.getWorldWidth() / 2,
                viewPort.getWorldHeight() / 2, 0);
        /* true - sleep objects, pressed. */
        this.world = new World(new Vector2(0, -10), true);

        this.timerIsOn = true;

        // This line is a debugger object - it draws the green lines around the tiled object.
        this.b2dr = new Box2DDebugRenderer();

        stage = new Stage();
        skin = new Skin();

        // Get the current configurations for responsiveness.
        Enums.VirtualPlayerStrategy virtualPlayerStrategy = null;
        // same player for all levels.
        if(CooperativeGame.gameData.getVirtualPlayerConfig().size() == 1){
            virtualPlayerConfig = CooperativeGame.gameData.getVirtualPlayerConfig().get(0);
        }
        // different player - for instructions and for the rest of the game.
        else{
            if(levelNum == 1){
                virtualPlayerConfig = CooperativeGame.gameData.getVirtualPlayerConfig().get(0);
            }
            else{
                // TODO EXPERIMENT SARIT - REMEMBER TO FILL IT.
                virtualPlayerConfig = CooperativeGame.gameData.getVirtualPlayerConfig().get(1);
            }
        }

        // needs to be a factory
        Player virtualPlayer = null;
        // Now it is always girl.
        if(CooperativeGame.gameData.getUser().getVirtual_gender() == Enums.Gender.Female){
            virtualPlayer = new IceGirlPlayer(this.world, this,
                    ((CooperativeGame)Gdx.app.getApplicationListener()).
                            crossPlatformObjects.getUtils());
        }
        else{
            virtualPlayer = new IceBoyPlayer(this.world, this,
                    ((CooperativeGame)Gdx.app.getApplicationListener()).
                            crossPlatformObjects.getUtils());
        }

        switch (pType)
        {
            case Female:
                this.player = new RegularGirlPlayer(this.world,
                        this, ((CooperativeGame)Gdx.app.getApplicationListener()).
                        crossPlatformObjects.getUtils());
                break;

            case Male:
                this.player = new RegularBoyPlayer(this.world, this,
                        ((CooperativeGame)Gdx.app.getApplicationListener()).
                        crossPlatformObjects.getUtils());
                break;
        }

        virtualPlayerStrategy = virtualPlayerConfig.getStrategy();

        switch (virtualPlayerStrategy)
        {
            case ALLC:
                this.virtualPlayer = new ALLC_virtualPlayer(((CooperativeGame)Gdx.app.getApplicationListener()).
                        crossPlatformObjects.getUtils(), this.world,this,virtualPlayer);
                break;
            case ALLD:
                this.virtualPlayer = new ALLD_virtualPlayer(((CooperativeGame)Gdx.app.getApplicationListener()).
                        crossPlatformObjects.getUtils(), this.world,this, virtualPlayer);
                break;
            case TFT:
                this.virtualPlayer = new TFT_virtualPlayer(((CooperativeGame)Gdx.app.getApplicationListener()).
                        crossPlatformObjects.getUtils(), this.world,this, virtualPlayer);
                break;
            case RANDOM:
                this.virtualPlayer = new PROBABILITY_virtualPlayer(((CooperativeGame)Gdx.app.getApplicationListener()).
                        crossPlatformObjects.getUtils(), this.world,this, virtualPlayer);
                break;
            case Alternate:
                this.virtualPlayer = new Alternately_virtualPlayer(((CooperativeGame)Gdx.app.getApplicationListener()).
                        crossPlatformObjects.getUtils(), this.world,this, virtualPlayer);
                break;
            case ProbabilisticTFT:
                this.virtualPlayer = new ProbabilisticTFT_virtualPlayer(((CooperativeGame)Gdx.app.getApplicationListener()).
                        crossPlatformObjects.getUtils(), this.world,this, virtualPlayer, player);
                break;
        }

        GameScreen.virtualPlayer.setConfig(virtualPlayerConfig);
        GameScreen.virtualPlayer.getVirtualPlayerRequestUtils().setGameScreen(this);

        if(CooperativeGame.gameData.getUser().getVirtual_gender() == Enums.Gender.Female){
            virtualGenderPath = "girl/";
            CooperativeGame.virtualPlayerGender = "girl/";
        }
        else{
            virtualGenderPath = "boy/";
            CooperativeGame.virtualPlayerGender = "boy/";
        }

        mapsDetails = new MapsDetails();
        mapsDetails.setInitialPositions();

        if(mapsDetails.getMapsDatails().get(mapName) != null){
            // set human player first location according to the specific map.
            if(mapsDetails.getMapsDatails().get(mapName).getHumanPlayerPosition() != null) {
                this.player.b2body_boy.setTransform(mapsDetails.getMapsDatails().get(mapName).
                        getHumanPlayerPosition(), 0);
            }
            else{
                this.player.b2body_boy.setTransform(6, 1, 0);
            }

            // set virtual player first location according to the specific map.
            if(mapsDetails.getMapsDatails().get(mapName).getVirtualPlayerPosition() != null) {
                this.virtualPlayer.b2body_boy.setTransform(mapsDetails.getMapsDatails().
                        get(mapName).getVirtualPlayerPosition(), 0);
            }
            else{
                this.virtualPlayer.b2body_boy.setTransform(10, 1, 0);
            }
        }
        // default positions for human and virtual players if this map is not in the hashMap.
        else{
            this.player.b2body_boy.setTransform(6, 1, 0);
            this.virtualPlayer.b2body_boy.setTransform(10, 1, 0);
        }

        iceObstacles = new HashMap<IceObstacle, LockedCoin>();
        natureObstacles = new HashMap<NatureObstacle, LockedVirtualCoin>();
        coins = new ArrayList<Coin>();
        virtualCoins = new ArrayList<VirtualPlayerCoin>();

        hud = new Hud(this);

        movmentInputManager = ((CooperativeGame)Gdx.app.getApplicationListener()).
                crossPlatformObjects.getInput();
        // TODO - does init is needed every time? static?
        movmentInputManager.init(player, stage);

        BackButton backButton = new BackButton(this, 0.9f, 0.88f, stage, game,
                0.08f, 0.08f);

        volumeButton = new VolumeButton(this, 0.9f, 0.88f, stage,
                0.08f, 0.08f, music.isSoundIsOn());

        pauseButton = new PauseButton(this, (float) 0.82f, (float) 0.88f, stage,
                0.08f, 0.08f);

        HelpButton helpButton = new HelpButton(this, (float) 0.69f, (float) 0.82f, stage,
                    0.13f, 0.13f);
        this.helpButton = helpButton.button;

        // build the world objects and fill the obstacles array above.
        worldBuilder = new WorldBuilder(world, tiledMap, this);
        world.setContactListener(new WorldContactListener());

        if(this.levelParams.getInstructions() != null){
            isInstructionLevel = true;
        }
        else {
            isInstructionLevel = false;
        }

        buildIceCubeThrowAnimation();

        this.randomGenerator = new RandomGenerator(this, this.tiledMap, this.searchableMatrix, m);

        // for thanks bubble - not instructions.
        this.thanksBubble = new ThanksBubble(false);
        this.canYouHelpMeBubble = new CanYouHelpMeBubble(isInstructionLevel);
        //this.comeToHelpBubble = new ComeToHelpBubble();

        this.iceObsAnimation = new ObstacleAnimation(this.tiledMap, 0.5f, Enums.ObstacleType.Ice);
        this.iceObsAnimation.prepareAnimation();

        //////////////////////

        // important to avoid new score declaration when there is no new score.
        this.newScore = false;
        // initialize pause frame.
        this.pauseFrameTexture = GraphicTools.getTexture("hud/pause_frame.png");

        hud.setIsInstructionLevel(isInstructionLevel);

        numOfStars = 1;

        // TODO MAKE IS GENERIC! Sarit
        Enums.Player firstAsker;
        AskingOrderManager askingOrderManager = new AskingOrderManager();
        firstAsker = askingOrderManager.getFirstHelper(levelNum);

        elementsGenerationManager = new ElementsGenerationManager(this, firstAsker,
                4,
                ((CooperativeGame) Gdx.app.getApplicationListener()).
                        crossPlatformObjects.getUtils());

        elementsGenerationManager.setIntervalSeconds(CooperativeGame.gameData.
                getGame_params().getHelp_requests_interval());

        obstaclesManager = new ObstaclesManager(this);
        //TODO - JUST IS NON-INSTRUCTION, EXPERIMENTAL VERSION.
        obstaclesManager.removeAllObstacles();
        getHelpButton().remove();

        player.getHumanPlayerRequestUtils().setGameScreen(this);
        GameScreen.virtualPlayer.getVirtualPlayerRequestUtils().setGameScreen(this);

        humanNumRequests = 0;
        virtualNumRequests = 0;

        elementsRemover = new ElementsRemover(this);

        if(!(this instanceof CombinedInstructions)) {
            elementsRemover.removeAllCoins();
            elementsRemover.removeAllVirtualCoins();
        }

        fpsCalculator = new FpsCalculator(stage, 1f);

        mute_events = new ArrayList<OnOffEvent>();
        pause_events = new ArrayList<OnOffEvent>();

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new InputProcessorGameScreen(this));
        Gdx.input.setInputProcessor(inputMultiplexer);

        gameDataUpdater = new GameDataUpdater(this);
        CooperativeGame.conn.sendLevelStart(new LevelStart(CooperativeGame.gameData.getUser().getUser_id(), levelNum));

        float countFrom = this.levelParams.getLevel_minimal_length();
        countdownTimer = new CountdownTimer(countFrom, this);
        countdownTimer.start();
        // todo - END OF CONSTRUCTOR!
        //state = State.LevelEnded;
    }

    @Override
    public void show() {
    }
    /* Set position of camera to be centered on player. */
    public void handleInput(float dt) {

    }

    public void updateStopwatch(int countFrom){
        //remainingTimeFloat = countdownTimer.getTimeRemaining();
        String remainingTime = countdownTimer.getTimeFormatted();
        if(cheat){
            setState(State.LevelEnded);
        }
        hud.updateStopWatchDisplay(remainingTime);
//        if(!(remainingTimeInt <= 0)){
//            hud.updateStopWatchDisplay(remainingTime);
//        }
    }

    /* All the updating of our game world. */
    public void update(float dt) {
        world.step(1 / 60f, 6, 2);
        player.update(dt);
        virtualPlayer.update(dt);
        // todo - wasteful. improve it!
        hud.updateScoreStyle();
        hud.update(dt);
        handleInput(dt);
        countdownTimer.update(dt);
        this.orthographicCamera.update();
        /* Only what the camera can see.  */
        this.tiledMapRenderer.setView(orthographicCamera);
    }

    public void updateUser(){
        if(humanScore > CooperativeGame.gameData.getUser().getHigh_score()){
            CooperativeGame.gameData.getUser().setHigh_score(humanScore);
        }
        CooperativeGame.gameData.getUser().setLevels_played(levelNum);
    }

    /* Render the game - map, camera.. */
    @Override
    public void render(float delta) {
        //stage.setDebugAll(true);
        fpsCalculator.render();
        switch (state) {
            /* Case game is running. */
            case Running:
                // TODO - CHECK LOGIC! TIME.
                if(timerIsOn || isInstructionLevel) {
                    // timer should not be updated when obstacles are exists.
                    //if(getNatureObstacles().size() == 0 && getIceObstacles().size() == 0){
                    if(!(elementsGenerationManager.isPotentialHelpTime())){
                        if(!last_called){
                            justResumed = true;
                        }
                        updateStopwatch(this.levelParams.getLevel_minimal_length());
                        last_called = true;
                    }
                    else{
                        last_called = false;
                    }
                }
                update(delta);
                StartRender();

                // Avoid going out of the screen.
                if ((player.b2body_boy.getPosition().x > 1) || (virtualPlayer.b2body_boy.getPosition().x > 1)) {
                    limitWalking = false;
                } else {
                    limitWalking = true;
                }
                // TODO - CROSS - CONSIDER BUTTONS.
                if(buttons == true) {
                    movmentInputManager.arrowMovement();
                }
                /**
                 * AI MOVING FUNCTION
                 */
                if (!virtualPlayer.isFollowingRealPlayer()) {
                    virtPlayerXFloatPosition = (virtualPlayer.b2body_boy.getPosition().x
                            * CooperativeGame.PPM) / this.mapCellWidth;
                    virtPlayerYFloatPosition = this.mapHeight - (virtualPlayer.b2body_boy.getPosition().y
                            * CooperativeGame.PPM) / this.mapCellWidth;

                    AIMoving(virtPlayerXFloatPosition, virtPlayerYFloatPosition);
                }

                /***********************************************
                RANDOM COIN GENERATOR WHEN PLAYER'S COINS' LIST IS EMPTY
                ************************************************/
                // for free play setting.
                // todo - check virtual help.
                if(CooperativeGame.gameData.getGame_params().getInstructions_method() == Enums.InstructionsMethod.Combined){
                    if(levelNum == 1){
                        elementsGenerationManager.combinedInstructionsElementsCreation();
                    }
                    else{
                        elementsGenerationManager.experimentalElementsCreation();
                    }
                }

                else if((CooperativeGame.gameData.getGame_params().getInstructions_method() == Enums.InstructionsMethod.Extended)){
                    if(isInstructionLevel){
                        elementsGenerationManager.controlledCreationInstructions(levelNum);
                    }
                    else{
                        // for experimental setting.
                        elementsGenerationManager.experimentalElementsCreation();
                    }
                }

                if(showScoreBoolean) {
                    scoreVirtualLabel.setText(Integer.toString(virtualScore));
                }

                // thanks button logic.
                if(addThanksButton){
                    this.addThanksButton();
                }
                EndRender();
                break;

            case VirtualAskForHelp:
                // avoid two help requests at the same time.
                getHelpButton().remove();
                VirtualAskForHelpLogic(delta);
                break;

            case HumanAskForHelp:
                HumanAskForHelpLogic(delta);
                break;

            /* Case game is pausing. */
            case Paused:
                PauseLogic(delta);
                break;

            case LevelEnded:
                levelEnded = true;
                // starts logic calculate numOfStars.
                StarsLogic starsLogic = new StarsLogic(this.humanScore, this.displayedCoins);
                //this.numOfStars = starsLogic.getNumOfStars();
                this.numOfStars = 3;

                music.stopMusic();

                //////////////////////////////////
                // add the new level log to GameData object and update highScore if needed.
                gameDataUpdater.UpdateGameData(mute_events, pause_events, helpRequests, numOfStars,
                        virtualPlayerConfig.getStrategy().toString(), fpsCalculator.getFpsInfo(), elementCollectionList);
                // note - should be called after UpdateGameData because of the highScore
                // change if needed.
                if(CooperativeGame.gameData.getGame_params().getContinuity()
                        == Enums.GameContinuity.Continual) {
                    if(!(CooperativeGame.gameData.getUser().getName().equals("Admin"))){
                        updateUser();
                    }
                }

                // EndScreen - show score and numOfStars.
                game.setScreen(new EndLevel(game, "Tiled/background.png", this.levelNum, this.humanScore,
                        numOfStars, this.levelLog));
                //dispose();
                break;

            case HelpAsk:
                /**********
                 check if the position is a real coin. (and not another place..)
                 if it is a coin - add it to the friend queue, call ChangeStateHelp() to get out from help mode.
                 the friend will go to brick the coin bam according to his strategy (instantly/delay/etc).
                 maybe add a delay to make it realistic.
                 **********/
                //StartRender();
                PauseLogic(delta);
                // todo combined - generic!
                if(this instanceof CombinedInstructions){
                    ((CombinedInstructions)this).preExplanation.remove();
                }
                setState(State.HumanAskForHelp);
                this.humanAskState = HumanAskState.Prepare;
                break;
        }
    }

    private void AIMoving(float playerxFloatPosition,float playeryFloatPosition )
    {
        /**********************************************
         AI MOVING
         Compute virtual player's position, compute best next step according to A* algorithm
         Get vector according to best next step.
         ***********************************************/
        int playerColposition = Math.round((virtualPlayer.b2body_boy.getPosition().x
                * CooperativeGame.PPM) / this.mapCellWidth);
        // row counted from the bottom!!
        int playerRowPosition = this.mapHeight
                - Math.round(virtualPlayer.b2body_boy.getPosition().y * CooperativeGame.PPM
                / this.mapCellWidth);

        /*
        System.out.println("player col float " + playerxFloatPosition + ",player row float " + playeryFloatPosition);
        System.out.println("player col " + playerColposition + ",player row  " + playerRowPosition);
        System.out.println("linear apply x " + virtualPlayer.b2body_boy.getLinearVelocity().x);
        System.out.println("linear apply y " + virtualPlayer.b2body_boy.getLinearVelocity().y);
        */

        //legs
        Node currentPosition = this.searchableMatrix.getNodesMatrix()[playerRowPosition][playerColposition];

        Node bestNextStep = null;

        // NO CALL FOR HELP.
        if (virtualPlayer.isCallQueueEmpty())
        {
            // no need for help - go to virtual coin
            //comeToHelpBubble.getBubble().remove();

            // check if non blocked virtual coin is available.
            if(this.searchableMatrix.getVirtualCoinsList().isEmpty())
            {
                // NO FREE COINS. NO BLOCKED COINS.
                if(this.searchableMatrix.getBlockedVirtualCoinsList().isEmpty())
                {
                    // random generator
                    //addCollectibleElement(MatrixBuilder.cellType.virtualcoins);
                    bestNextStep = null;
                }
                // BLOCKED COIN! STAND NEXT TO IT.
                else
                {
                    // get close as possible if not waiting for help
                    if(!waitingForHelp)
                        bestNextStep = astar.GetBestNextStepToBlockedVirtualCoin
                                    (this.searchableMatrix, currentPosition);

                    //if ((!waitingForHelp) && (!CooperativeGame.dialogIsOn))
                    if ((bestNextStep == currentPosition) && (!waitingForHelp))
                    {
                        // Stop the player when he is on ground and no presses on left/right.
                        if (virtualPlayer.b2body_boy.getLinearVelocity().y == 0) {
                                virtualPlayer.b2body_boy.setLinearVelocity(new Vector2(0f, 0));
                        }
                        // Now we know that the virtual player has stopped moving.
                        if (virtualPlayer.b2body_boy.getLinearVelocity().y == 0
                                && virtualPlayer.b2body_boy.getLinearVelocity().x == 0) {
                            // VIRTUAL PLAYER ARRIVED TO A BLOCKED VIRTUAL COIN!

                            // todo - only one time!
                            virtualPlayer.getVirtualPlayerRequestUtils().setBlockedElementMatrixCell
                                    (astar.getNatureObstacleNode().getMatrixCell());

                            virtualPlayer.flipVirtualPlayerIfNeeded();

                            // check how much time has passed from the last virtual player ask.
                            // TODO EXPERIMENT SARIT - CHECK THIS
                            if(PersistentData.GAME_GROUP == Enums.Group.Experimental){
                                currentTime = System.currentTimeMillis();
                                float realPassed = currentTime - lastVirtualAskTime;
                                float realSec = (int) (realPassed / 1000);
                                if(((realSec >= VIRTUAL_ASKS_DELAY) || (lastVirtualAskTime == 0))
                                        // check if player score is not zero.
                                        && (humanScore >= CooperativeGame.gameData.getGame_params()
                                        .getReciprocityValues().getHelp_providing_cost_human())){
                                    if(this instanceof CombinedInstructions){
                                        //todo - make it generic!
                                        if(((CombinedInstructions)this).virtualAllowedToAsk){
                                            // change state to virtual ask state.
                                            setState(State.VirtualAskForHelp);
                                        }
                                    }
                                    else{
                                        // change state to virtual ask state.
                                        setState(State.VirtualAskForHelp);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                waitingForHelp = false;
                bestNextStep = astar.GetBestNextStepToVirtualCoin(this.searchableMatrix, currentPosition);
            }
        }
        // CALL FOR HELP.
        else {
            waitingForHelp = false;
            Node callNode = virtualPlayer.answerCall();
            bestNextStep = astar.GetBestNextStepToGoal(this.searchableMatrix, currentPosition
                    , callNode);
        }
        // System.out.println("best x " + bestNextStep.getMatrixCell().getX() + ",best y " + bestNextStep.getMatrixCell().getY());

        // Get direction according to bestNextStep
        Vector2 toGo = this.guide.getVector(currentPosition, bestNextStep);

        if(toGo.x > 0){
            virtualPlayer.setRight(true);
        }
        else if(toGo.x < 0){
            virtualPlayer.setRight(false);
        }
        /*
        System.out.println("To go X:" + toGo.x);
        System.out.println("To go Y:" + toGo.y);

        if ((toGo.x != 0 ) && (toGo.y != 0))
        {
            System.out.println("***************!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
         */

        // Smoother playing - can't go too fast, can't fly.
        if (toGo.x >= 0) { //going right
            if (virtualPlayer.b2body_boy.getLinearVelocity().x <= 5) {
                // virtualPlayer.b2body_boy.setLinearVelocity(toGo);
                virtualPlayer.b2body_boy.applyLinearImpulse(toGo,
                        virtualPlayer.b2body_boy.getWorldCenter(), true);
            }
        } else if (toGo.x < 0) { //going left
            if (virtualPlayer.b2body_boy.getLinearVelocity().x >= -5) {
                // virtualPlayer.b2body_boy.setLinearVelocity(toGo);
                virtualPlayer.b2body_boy.applyLinearImpulse(toGo,
                        virtualPlayer.b2body_boy.getWorldCenter(), true);
            }
        }

        // y axis
        if(toGo.y > 0){ // go up
            // If the player is standing in a place with high y value.
            if((virtualPlayer.b2body_boy.getPosition().y >= maxVirtualJump) &&
                    virtualPlayer.b2body_boy.getLinearVelocity().y == 0.1f) {
                virtualPlayer.b2body_boy.applyLinearImpulse(new Vector2(0,0.75f),
                        virtualPlayer.b2body_boy.getWorldCenter(), true);
            }
            else if(virtualPlayer.b2body_boy.getPosition().y < maxVirtualJump) {
                if(virtualPlayer.b2body_boy.getLinearVelocity().y < 2f) {
                    virtualPlayer.b2body_boy.applyLinearImpulse(new Vector2(0,0.6f),
                            virtualPlayer.b2body_boy.getWorldCenter(), true);
                }
            }
        }
    }

    public void StartRender(){
    /* White - the borders of the screen. */
        Gdx.gl.glClearColor(57, 166, 213, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.tiledMapRenderer.render();

        ////////////////////////
        //this.b2dr.render(world, orthographicCamera.combined);
        ///////////////////////
        /* See what the cam see.*/
        game.spriteBatch.setProjectionMatrix(orthographicCamera.combined);
        /* Open the box, put all the textures inside it. */
        game.spriteBatch.begin();
        drawPlayers();
    }

    public void EndRender(){
        ////////////////////////
        //this.b2dr.render(world, orthographicCamera.combined);
        ///////////////////////
        game.spriteBatch.end();

        hud.getStage().act();
        hud.getStage().draw();

        stage.act();
        stage.draw();
    }

    /* Change game state if necessary. */
    public void ChangeState(boolean resumeable){
        if(this.state == State.Running){
            switchToPauseState(resumeable);
        }
        else if(this.state == State.Paused){
            switchToRunningState(true);
        }
    }

    /* Change game state if necessary. */
    public void ChangeStateHelp(){
        if(this.state == State.Running || this.state == State.TrainingHumanAsking
                || this.state == State.WaitForHelpTouch || this.state == State.Paused){
            player.getHumanPlayerRequestUtils().init();
            setState(State.HelpAsk);
        }
        else if((this.state == State.HelpAsk)
            || (this.state == State.HumanAskForHelp)) {
            //coinsAnimation.resetCells();
            iceObsAnimation.resetCells();
            switchToRunningState(true);
        }
    }

    @Override
    /* To know the real size of the screen. */
    public void resize(int width, int height) {
        this.viewPort.update(width, height);
    }
    @Override
    public void pause() {
    }
    @Override
    public void resume() {
    }
    @Override
    public void hide() {
        dispose();
    }

    /* Dispose map, world,camera and hud. */
    @Override
    public void dispose() {
        tiledMap.dispose();
        hud.getStage().dispose();
        stage.dispose();
        world.dispose();
        skin.dispose();
    }

    public static LevelLog getLevelLog() {
        return levelLog;
    }

    public static VirtualPlayer getVirtualPlayer() {
        return virtualPlayer;
    }

    public static boolean isWaitingForHelp() {
        return waitingForHelp;
    }

    public static void setWaitingForHelp(boolean waitingForHelp) {
        GameScreen.waitingForHelp = waitingForHelp;
    }

    // include virtual declaration.
    private void buildIceCubeThrowAnimation(){
        // frames array (contains TextureRegions).
        float width = 0.48f * screenWidth, height = 0.82f * width;
        float seconds, xPos, yPos;
        xPos = 0.28f * Constants.VIEWPORT_WIDTH;
        yPos = 0.18f * Constants.VIEWPORT_HEIGHT;
        String prefixPath = "pay/", postfixPath = "ice_cube_throwing.pack";

        Array<TextureRegion> frames = new Array<TextureRegion>();

        String fullPath = prefixPath + virtualGenderPath + postfixPath;
        atlas = GraphicTools.getTextureAtlas(fullPath);

        // add the 6 frames to an array.
        for(int i = 0; i < 6; i++){
            frames.add(new TextureRegion(atlas.findRegion(Integer.toString(i + 1))));
        }
        animation = new Animation(0.1f, frames);
        // show ice throwing animation in a loop when number of iterations is the help cost to the virtual player.
        animatedIceCubeThrowing = new BasicAnimatedImage(animation,
                CooperativeGame.gameData.getGame_params().getReciprocityValues().getHelp_providing_cost_virtual(), true, this);
        animatedIceCubeThrowing.setBounds(xPos, yPos, width, height);

        // in instruction level - show it for longer time.
        // includes "I will pay one ice cube").
        if(Constants.GAME_LANGUAGE.getLanguage() == Enums.Language.Arabic){ // TODO 2021
            seconds = 3f;
        }
        else{
            seconds = 1f;
        }

        virtualAnswerDeclaration = new PopUp(CooperativeGame.languagePrefix +
                "pop-ups/virtual_response/" + virtualGenderPath + "accept_help.png", seconds,
                xPos, yPos, width, height, ((CooperativeGame)Gdx.app.getApplicationListener()).
                crossPlatformObjects.getUtils(), false, 0);
        // load pop up of virtual ignore. relevant only if virtual player strategy is not ALLD.
        // i.e she is not always defects (ignores).

        // ???? ALLC!!
        if(virtualPlayer.getConfig().getStrategy()
                != Enums.VirtualPlayerStrategy.ALLC) {
            virtualIgnoreDeclaration = new PopUp(CooperativeGame.languagePrefix +
                    "pop-ups/virtual_response/" + virtualGenderPath + "ignore_help.png", 3f,
                    xPos, yPos, width, height, ((CooperativeGame)Gdx.app.getApplicationListener()).
                    crossPlatformObjects.getUtils(), false, 0);
        }
    }

    public void addThanksButton(){
        this.addThanksButton = false;
        this.stage.addActor(this.thanksBubble.getBubble());
        this.showThanksButton = true;
        if(this instanceof CombinedInstructions){
            switchToPauseState(false);
        }

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                int n = thanksBubble.getBubble().getListeners().size;
                for(EventListener eventListener : thanksBubble.getBubble().getListeners()) {
                    eventListener.handle(new Event());
                }
                thanksBubble.getBubble().remove();
                showThanksButton = false;

            }
        }, 1.5f); // delay in seconds.
    }

    public void PauseLogic(float delta){
        update(delta);
        /* White (the borders of the screen.) */
        Gdx.gl.glClearColor(57, 166, 213, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.tiledMapRenderer.render();

        ////////////////////////
        //this.b2dr.render(world, orthographicCamera.combined);
        ///////////////////////

        /* Focus on what the cam see. */
        game.spriteBatch.setProjectionMatrix(orthographicCamera.combined);
        /* Open the box, put all the textures inside it. */
        game.spriteBatch.begin();
        // purple frame to make clear that we are in pause mode.
        // pauseFrame.draw(game.spriteBatch);
        game.spriteBatch.draw(pauseFrameTexture, 0, 0, this.mapWidth * this.mapCellWidth /
                CooperativeGame.PPM, this.mapHeight * this.mapCellHeight /
                CooperativeGame.PPM);

        drawPlayers();

        EndRender();
    }

    public static Viewport getViewPort(){
        return viewPort;
    }

    public void HumanAskForHelpLogic(float delta){
        PauseLogic(delta);

        switch (humanAskState) {
            case Prepare:
                if(this instanceof CombinedInstructions) {
                    iceObsAnimation.getCellsToReplace();
                    iceObsAnimation.animate();
                }

                if(!(this instanceof CombinedInstructions)){
                    LockedCoin lockedCoin = getIceObstacles().entrySet().iterator().next().getValue();
                    MatrixCell blockedCoinMatrixCell = new MatrixCell(lockedCoin.getRow(), lockedCoin.getCol());

                    player.getHumanPlayerRequestUtils().setBlockedElementMatrixCell(blockedCoinMatrixCell);
                    //player.getHumanPlayerRequestUtils().init();
                    if(virtualPlayer.response() == true){
                        elementsGenerationManager.setLastIceRemovingTime(((CooperativeGame)Gdx.app.getApplicationListener()).crossPlatformObjects.getUtils().getNanoTime());
                        player.getHumanPlayerRequestUtils().handleVirtualResponse(true);
                        humanAskState = HumanAskState.VirtualAccepted;
                    }
                    else{
                        elementsGenerationManager.setLastIceRemovingTime(((CooperativeGame)Gdx.app.getApplicationListener()).crossPlatformObjects.getUtils().getNanoTime());
                        player.getHumanPlayerRequestUtils().handleVirtualResponse(false);
                        humanAskState = HumanAskState.VirtualIgnored;
                    }
                }
                else{
                    humanAskState = HumanAskState.CheckTouchInput;
                }
                break;

            case CheckTouchInput:
                // check to touch only if no music is playing
                // (To ensure that the instruction is heard to the end.)
                if (Gdx.input.justTouched()) {
                    double xTouch = Gdx.input.getX();
                    double yTouch = Gdx.input.getY();
                    Vector2 newPoints = new Vector2((float) xTouch, (float) yTouch);
                    // explanation in Helping class - getBlockedCoin.
                    newPoints = this.viewPort.unproject(newPoints);

                    // check if the position is a coin.
                    MatrixCell blockedCoinMatrixCell = Helping.getBlockedCoin(newPoints, iceObstacles);
                    // for a valid press - add this blocked coin to virtual friend queue.
                    if (blockedCoinMatrixCell != null) {
                        if(this instanceof CombinedInstructions){
                            ((CombinedInstructions)this).nowClickPopUp.remove();
                        }
                        player.getHumanPlayerRequestUtils().setBlockedElementMatrixCell(blockedCoinMatrixCell);
                        //player.getHumanPlayerRequestUtils().init();
                        if(virtualPlayer.response() == true){
                            elementsGenerationManager.setLastIceRemovingTime(((CooperativeGame)Gdx.app.getApplicationListener()).crossPlatformObjects.getUtils().getNanoTime());
                            player.getHumanPlayerRequestUtils().handleVirtualResponse(true);
                            humanAskState = HumanAskState.VirtualAccepted;
                        }
                        else{
                            elementsGenerationManager.setLastIceRemovingTime(((CooperativeGame)Gdx.app.getApplicationListener()).crossPlatformObjects.getUtils().getNanoTime());
                            player.getHumanPlayerRequestUtils().handleVirtualResponse(false);
                            humanAskState = HumanAskState.VirtualIgnored;
                        }
                    }
                }
                break;

            // includes virtual response declaration.
            case VirtualAccepted:
                if(virtualAnswerDeclaration.finished()){
                    virtualAnswerDeclaration.setFinished(false);
                    getAnimatedIceCubeThrowing().resetAnimation();
                    stage.addActor(getAnimatedIceCubeThrowing());
                    // change state - animation running.
                    humanAskState = HumanAskState.VirtualResponseDisplay;
                    break;
                }

                // add to stage if needed.
                if(virtualAnswerDeclaration.getStage() == null){
                    // popUp is not into stage. add it.
                    // declaration of virtual player answer.
                    virtualAnswerDeclaration.init();
                    stage.addActor(virtualAnswerDeclaration);
                    // virtual player tells that she will help.
                    music.getVirtualAccept().play();
                }
                break;

            // includes virtual response declaration.
            case VirtualIgnored:
                if(virtualIgnoreDeclaration.finished()){
                    virtualIgnoreDeclaration.setFinished(false);
                    // TODO - WRITE THIS LOG!!!
                    // remove the wanted ice obstacle from map.
                    obstaclesManager.removeIceObstacle(true);

                    player.getHumanPlayerRequestUtils().writeHelpRequest();
                    music.iceObsDisappearanceIgnoreSound();

                    if(this instanceof CombinedInstructions){
                        ((CombinedInstructions)this).generalHumanAsking = CombinedInstructions.GeneralHumanAsking.After;
                    }
                    reciprocityState = ReciprocityState.Init;
                    elementsGenerationManager.setPotentialHelpTime(false);
                    //ChangeStateHelp();
                    break;
                }

                // add to stage if needed.
                if(virtualIgnoreDeclaration.getStage() == null){
                    // popUp is not into stage. add it.
                    // declaration of virtual player answer.
                    virtualIgnoreDeclaration.init();
                    stage.addActor(virtualIgnoreDeclaration);
                    music.getVirtualIgnore().play();
                }
                break;

            case VirtualResponseDisplay:
                // waiting for animation end.
                break;

            case HelpRequestAccepted:
                // obstacle removing sound.
                music.iceObsDisappearanceAgreeSound();

                // remove the wanted ice obstacle from map.
                obstaclesManager.removeIceObstacle(false);

                player.getHumanPlayerRequestUtils().writeHelpRequest();
                // todo - needed?? initialize states.
                //humanAskState = Prepare;
                if(this instanceof CombinedInstructions){
                    ((CombinedInstructions)this).generalHumanAsking = CreatePostExplanationPopUp;
                }
                reciprocityState = ReciprocityState.Init;
                elementsGenerationManager.setPotentialHelpTime(false);

                switchToRunningState(true);
                break;
        }
    }

    public void VirtualAskForHelpLogic(float delta){
        PauseLogic(delta);
        switch (virtualAskState) {
            case Wait:
                break;

            case JustAsked:
                music.getCanYouHelpMe(isInstructionLevel).setOnCompletionListener(new Music.OnCompletionListener() {
                    @Override
                    public void onCompletion(Music music) {
                    //todo - Hud
                    //hud.startCoinAnimation();
                    virtualAskState = VirtualAskState.BubbleDisplay;
                    return;
                    }
                });
                music.getCanYouHelpMe(isInstructionLevel).play();
                this.stage.addActor(this.canYouHelpMeBubble.getBubble());
                waitingForHelp = true;
                virtualAskState = VirtualAskState.Wait;
                break;

            case BubbleDisplay:
                // remove speech bubble. ("can you help me?")
                canYouHelpMeBubble.getBubble().remove();
                virtualPlayer.getVirtualPlayerRequestUtils().request();
                break;

            case PopUpDisplay:
                break;

            case HandleHumanRespone:
                virtualPlayer.getVirtualPlayerRequestUtils().setAnswerTime();
                updateLastVirtualAskTime();
                elementsGenerationManager.setLastNatureRemovingTime(((CooperativeGame)Gdx.app.getApplicationListener()).crossPlatformObjects.getUtils().getNanoTime());
                virtualPlayer.getVirtualPlayerRequestUtils().handleHumanResponse();
                virtualPlayer.getVirtualPlayerRequestUtils().writeHelpRequest();
                // todo check if it is the correct place.
                reciprocityState = ReciprocityState.Init;
                elementsGenerationManager.setPotentialHelpTime(false);
                break;

            // todo - duplicated code here!
            case RemoveNatureObstacleAndVirtualCoin:
                // remove obstacle with the inner element.
                obstaclesManager.removeNatureObstacle(true);
                // initialize for the next virtual help request.
                this.virtualAskState = VirtualAskState.JustAsked;
                // back to Running state.
                switchToRunningState(true);
                music.toneUpBackgroundMusic();
                break;

            case RemoveNatureObstacle:
                // remove obstacle without inner element.
                obstaclesManager.removeNatureObstacle(false);
                // initialize for the next virtual help request.
                this.virtualAskState = VirtualAskState.JustAsked;
                // back to Running state.
                switchToRunningState(true);
                music.toneUpBackgroundMusic();
                break;
        }
    }

    public static HashMap<IceObstacle, LockedCoin> getIceObstacles(){
        return iceObstacles;
    }
    public static HashMap<NatureObstacle, LockedVirtualCoin> getNatureObstacles() {
        return natureObstacles;
    }
    public ArrayList<HelpRequest> getHelpRequests() {
        return helpRequests;
    }
    public BasicAnimatedImage getAnimatedIceCubeThrowing() {
        return animatedIceCubeThrowing;
    }

    public World getWorld() {
        return world;
    }

    public void drawPlayers(){
        // flip players if needed.
        if(player.isRight()){
            player.getPlayerSprite().flip(false, false);
        }
        else{
            player.getPlayerSprite().flip(true, false);
        }

        if (virtualPlayer.isRight()) {
            virtualPlayer.getPlayerSprite().flip(false, false);
        } else {
            virtualPlayer.getPlayerSprite().flip(true, false);
        }

        player.getPlayerSprite().draw(game.spriteBatch);
        // draw virtual friend if needed (when the game stars - no need,
        // because the user learns the game rules and it can disturb).
        if(CooperativeGame.gameData.getGame_params().getInstructions_method() == Enums.InstructionsMethod.Combined){
            // todo - make it faster!!
            if(levelNum == 1){
                if(((CombinedInstructions)this).getInstructionalStepCurrState() != CoinsCollecting) {
                    if (((CombinedInstructions) this).playingTogetherInnerState == CombinedInstructions.PlayingTogetherInnerState.WaitForElementsCollection) {
                        virtualPlayer.getPlayerSprite().draw(game.spriteBatch);
                    }
                }
            }
            else{
                virtualPlayer.getPlayerSprite().draw(game.spriteBatch);
            }
        }
        else if(CooperativeGame.gameData.getGame_params().getInstructions_method() == Enums.InstructionsMethod.Extended){
            if(!(this.levelParams.getInstructions() != null
                    && (this.levelParams.getInstructions().getLevelDescription()
                    == Enums.LevelDescription.OnlyCoins))) {
                virtualPlayer.getPlayerSprite().draw(game.spriteBatch);
            }
        }
    }

    // called when human presses "Yes"/"No", i.e decide whether to help or not to help.
    // important for measure the delay between virtual help requests.
    public void updateLastVirtualAskTime(){
        // update current time for the next help requests.
        lastVirtualAskTime = System.currentTimeMillis();
    }

    public RandomGenerator getRandomGenerator() {
        return randomGenerator;
    }

    public static SearchableMatrix getSearchableMatrix() {
        return searchableMatrix;
    }

    public static MatrixBuilder getMatrixBuilder() {
        return m;
    }

    public ReciprocityState getReciprocityState() {
        return reciprocityState;
    }

    public void setState(State newState) {
        this.state = newState;
    }

    public void setReciprocityState(ReciprocityState reciprocityState) {
        this.reciprocityState = reciprocityState;
    }

    public void increaseScore(Enums.Player player, int value, boolean showVisualIncrease){
        if(player == Enums.Player.Human){
            humanScore += value;
            hud.increaseHumanScore(value);
        }
        else if(player == Enums.Player.Virtual){
            virtualScore += value;
        }
        if(showVisualIncrease){
            hud.showIncrease(value);
        }
    }

    public void decreaseScore(Enums.Player player, int value){
        if(player == Enums.Player.Human){
            humanScore -= value;
            hud.decreaseHumanScore();
        }
        else if(player == Enums.Player.Virtual){
            virtualScore -= value;
        }
    }

    public static Player getPlayer() {
        return player;
    }
    public int getHumanScore() {
        if(CooperativeGame.gameData.getGame_params().isResetsScore()) {
            return humanScore;
        }
        else{
            return humanScore + accumulatedHumanScore;
        }
    }

    public static TextButton getHelpButton() {
        return helpButton;
    }

    public int getVirtualNumRequests() {
        return virtualNumRequests;
    }

    public int getHumanNumRequests() {
        return humanNumRequests;
    }

    public void setHumanNumRequests(int humanNumRequests) {
        this.humanNumRequests = humanNumRequests;
    }

    public void setVirtualNumRequests(int virtualNumRequests) {
        this.virtualNumRequests = virtualNumRequests;
    }

    public ArrayList<Coin> getCoins(){
        return coins;
    }

    public ArrayList<VirtualPlayerCoin> getVirtualCoins(){
        return virtualCoins;
    }

    public void resetScores(){
        CooperativeGame.gameData.getCumulativeScores().setHuman_player_score(0);
        CooperativeGame.gameData.getCumulativeScores().setVirtual_player_score(0);
    }

    public State getState() {
        return state;
    }

    public void switchToPauseState(boolean resumeable){
        music.toneDownBackgroundMusic();
        if(!resumeable){
            pauseButton.remove(); // TODO - should we check if the button is on?
        }
        //player.b2body_boy.setLinearVelocity(0,0);
        //virtualPlayer.b2body_boy.setLinearVelocity(0,0);
        state = State.Paused;
        countdownTimer.pause();
    }

    public void switchToRunningState(boolean turnPauseButtonOn){
        if(turnPauseButtonOn && (!pauseButton.isOn())){
            pauseButton.add();
            //stage.addActor(pauseButton.getButton());
        }
        music.toneUpBackgroundMusic();
        justResumed = true;
        state = State.Running;
        countdownTimer.resume();
    }

    public ArrayList<OnOffEvent> getMute_events() {
        return mute_events;
    }

    public ArrayList<OnOffEvent> getPause_events() {
        return pause_events;
    }

    public ObstacleAnimation getIceObsAnimation() {
        return iceObsAnimation;
    }

    public PauseButton getPauseButton() {
        return pauseButton;
    }

    public static void setLevelLog(LevelLog levelLog) {
        GameScreen.levelLog = levelLog;
    }

    public ElementCollectionList getElementCollectionList() {
        return elementCollectionList;
    }

    public Hud getHud(){
        return hud;
    }

    public CountdownTimer getCountdownTimer() {
        return countdownTimer;
    }
}