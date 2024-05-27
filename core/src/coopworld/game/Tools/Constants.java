package coopworld.game.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import coopworld.game.Logs.EntranceData;
import coopworld.game.Logs.Enums;
import coopworld.game.Logs.GameLanguage;

/**
 * Created by User on 02/03/2017.
 */
public class Constants {
    // Each fixture has its bit - better with powers of 2.
    public static final short defaultBit = 1;
    public static final short boyBit = 2;
    public static final short brickBit = 4;
    public static final short coinBit = 8;
    public static final short vCoinBit = 16;
    public static final short destroyedBit = 32;
    public static final short natureObsBit = 64;
    public static final short iceObsBit = 128;
    // Matrix elements (from matrix builder)
    public static short EMPTY_MATRIX_CODE;
    public static short GROUND_MATRIX_CODE;
    public static short VIRTUAL_COINS_MATRIX_CODE;
    public static short ICE_OBSTACLES_MATRIX_CODE;
    public static short NATURE_OBSTACLES_MATRIX_CODE;
    public static short COINS_MATRIX_CODE;
    public static short REALPLAYER_MATRIX_CODE;
    public static short VIRTPLAYER_MATRIX_CODE;

    public static int obstaclesSize = 3;

    // Tiled map names.
    public static final short BACKGROUND_LAYER = 0;
    public static final short GRAPHIC_ELEMENTS_LAYER = 1;
    public static final short COLLECTABLE_LAYER = 2;
    public static final short GROUND_LAYER = 3;
    public static final short ICE_OBSTACLES_LAYER = 4;
    public static final short NATURE_OBSTACLES_LAYER = 5;
    public static final short COINS_LAYER = 6;
    public static final short VIRTUAL_COINS_LAYER = 7;
    public static final short LOCKED_COINS_LAYER = 8;
    public static final short VIRTUAL_LOCKED_COINS_LAYER = 9;
    public static final short HUMAN_PLAYER_POSITION = 10;
    public static final short VIRTUAL_PLAYER_POSITION = 11;

    // Elements values (free and locked).
    public static int FREE_ELEMENT_VALUE;
    public static int LOCKED_ELEMENT_VALUE;

    // TODO COOP2 WEB - REMOVED
    //public static final SimpleDateFormat HELP_REQUESTS_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public static final String COIN_SOUND = "sounds/diamond.mp3";
    public static final String SPECIAL_COIN_SOUND = "sounds/diamond3.mp3";
    public static final String LEVEL_COMPLETED_SOUND = "sounds/level_completed.ogg";
    public static final String UI_SOUND = "sounds/ui.ogg";
    public static final String ICE_OBSTACLE_DISAPPEAR_TRUE_SOUND = "sounds/ice_obs_disappear.mp3";
    public static final String ICE_OBSTACLE_DISAPPEAR_FALSE_SOUND = "sounds/ice_cube_decline.mp3";
    public static final String COIN_FALL_SOUND = "sounds/coin_fall.ogg";
    public static final String END_SCREEN_SOUND = "sounds/end_screen.ogg";
    public static final String NOTIFICATION_SOUND = "sounds/notification.ogg";

    public static String APK_VER;
    public static String PREFIX_FILES_PATH;
    public static String PREFIX_FILES_PATH_INTERNAL;

    public static final String APK_VER_PSYCHOLOGISTS = "3.1B";
    public static final String APK_VER_PSYCHOLOGISTS_REAL = "3.1B";

    public static final String USERS_PATH = "COOP_WORLD2_DATA";

    public static String TABLET_CODE = "";
    public static String SETTING_GROUP = "";

    public static final Map<String, String> strategiesMap = createMap();
    public static Map<String, String> createMap()
    {
        Map<String,String> myMap = new HashMap<String,String>();
        myMap.put("ALLC", "Always helps");
        myMap.put("TFT", "Tit-For-Tat");
        myMap.put("RANDOM", "Random");
        myMap.put("ALLD", "Never helps");
        return myMap;
    }

    public static float TFT_PROBABILITY = 0.7f;

    public static float VIEWPORT_WIDTH;
    public static float VIEWPORT_HEIGHT;

    public static float REGULAR_VOLUME = 0.15f;
    public static float LOW_VOLUME = 0.03f;
    public static final float SPEAKING_VOLUME = 0.95f;

    public static String START_CODE = "";

    public static int LEVELS_IN_SESSION = 4;
    public static ArrayList INTERVENTION_LEVELS_IN_SESSIONS = new ArrayList<>(Arrays.asList(1, 3, 5));
    public static int INTERVENTION_TOTAL_LEVELS = 7;

    public static ArrayList INTERVENTION_LEVELS_EXTENDED = new ArrayList<Integer>(Arrays.asList(4, 6));

    public static boolean IS_CHEAT = false;

    public static String USER_ID_WEB;

    public static float HELP_REQUESTS_INTERVAL = 15f; // in seconds

    public static int LEVEL_LENGTH_REGULAR = 165; // in seconds
    public static int LEVEL_LENGTH_INTERVENTION = 135; // in seconds

    public static int LEVEL_LENGTH;
    public static int LEVEL_LENGTH_EXTENDED = LEVEL_LENGTH + (int)HELP_REQUESTS_INTERVAL; // in seconds

    //public static final int LEVEL_LENGTH = 100000; // in seconds

    public static EntranceData ENTRANCE_DATA;

    public static String INFRASTRUCTURE = "Web";

    //public static GameLanguage GAME_LANGUAGE = new GameLanguage(Enums.Language.Hebrew, Enums.LanguageDirection.RTL); // TODO 2021 - CONSIDER ANOTHER PLACE.
    public static GameLanguage GAME_LANGUAGE = new GameLanguage(Enums.Language.English, Enums.LanguageDirection.LTR); // TODO 2021 - CONSIDER ANOTHER PLACE.

    public static boolean IS_PROLIFIC = false;

    public static boolean MASTER_ACCESS = false;
    public static String[] MASTER_CODES = {"111", "222", "333", "444", "555", "666", "777", "888", "999", "231",
            "11111","22222","33333","44444","55555","66666","77777","88888","99999"};

    public static String[] MASTER_CODES_TEXT = {"ana"};

    public static boolean IS_INTERVENTION;

    // TODO - think how to save it.
}