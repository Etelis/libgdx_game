package coopworld.game.Logs;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import java.sql.Timestamp;
import java.util.ArrayList;

import coopworld.game.CooperativeGame;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.PersistentData;

public class LogsBuilder {
    public void initGameData(){
        // set the wanted framework.
        CooperativeGame.gameData.setFramework_name("Psychologists");
        Constants.APK_VER = Constants.APK_VER_PSYCHOLOGISTS;
        Constants.PREFIX_FILES_PATH = Constants.USERS_PATH + "/" + Constants.APK_VER + "/" + "DB/";
        Constants.PREFIX_FILES_PATH_INTERNAL = Constants.USERS_PATH + "/" + Constants.APK_VER + "/" + "INTERNAL/";

        CooperativeGame.gameData.setApk_version(Constants.APK_VER_PSYCHOLOGISTS_REAL);
        /*
        CooperativeGame.gameData.setFramework_name(Enums.Framework.Psychologists);
        CooperativeGame.gameData.setApk_version(Constants.APK_VER_PSYCHOLOGISTS);
        */
        CooperativeGame.gameData.setGame_params(initGameParams());

        TabletData tabletData = getTabletData();
        CooperativeGame.gameData.setTablet_id(tabletData.getTablet_code());
        String group = tabletData.getGroup();
        if(group.equals("control")){
            PersistentData.GAME_GROUP = Enums.Group.Control;
        }
        else{
            PersistentData.GAME_GROUP = Enums.Group.Experimental;
        }

        // TODO CHEN - FOR DEBUG ONLY!
        //PersistentData.GAME_GROUP = Enums.Group.Control;
        CooperativeGame.gameData.setGroup(tabletData.getGroup());

        CooperativeGame.gameData.setStart_time(((CooperativeGame) Gdx.app.getApplicationListener()).
                crossPlatformObjects.getUtils().getTimeStamp());
    }

    public ArrayList<VirtualPlayerConfig> initVirtualPlayerConfig(GameParams gameParams){
        // TODO SARIT
        ArrayList<VirtualPlayerConfig> virtualPlayerConfigArr = new ArrayList<VirtualPlayerConfig>();

        VirtualPlayerConfig virtualPlayerConfigInstructional = new VirtualPlayerConfig();

        virtualPlayerConfigInstructional.setStrategy(Enums.VirtualPlayerStrategy.Alternate);
        virtualPlayerConfigInstructional.setTiming_request_logic(Enums.TimingRequestLogic.StandNextToObstacle);

        virtualPlayerConfigArr.add(virtualPlayerConfigInstructional);

        VirtualPlayerConfig virtualPlayerConfigRegular = new VirtualPlayerConfig();

        virtualPlayerConfigRegular.setStrategy(null);

        virtualPlayerConfigRegular.setStrategy(Enums.VirtualPlayerStrategy.ProbabilisticTFT);

        virtualPlayerConfigRegular.setTiming_request_logic(Enums.TimingRequestLogic.StandNextToObstacle);
        virtualPlayerConfigArr.add(virtualPlayerConfigRegular);

        return virtualPlayerConfigArr;
    }

    public ReciprocityValues initReciprocityValues(GameParams gameParams){
        ReciprocityValues reciprocityValues = new ReciprocityValues();
        reciprocityValues.setRegular_coin_value(1);
        reciprocityValues.setLocked_coin_value(10);
        reciprocityValues.setRegular_virtual_coin_value(1);
        reciprocityValues.setLocked_virtual_coin_value(10);
        reciprocityValues.setHelp_providing_cost_human(5);
        reciprocityValues.setHelp_providing_cost_virtual(5);

        Constants.FREE_ELEMENT_VALUE = reciprocityValues.getRegular_coin_value();
        Constants.LOCKED_ELEMENT_VALUE = reciprocityValues.getLocked_coin_value();

        return reciprocityValues;
    }


    public GameParams initGameParams(){
        GameParams gameParams = new GameParams();
        gameParams.setFramework_name(CooperativeGame.gameData.getFramework_name());
        gameParams.setApk_version(CooperativeGame.gameData.getApk_version());

        ArrayList<LevelParams> levelsParams = new ArrayList<LevelParams>();
        // CHANGE IT!
        int totalLevelsNum = CooperativeGame.levelsPath.size();
        gameParams.setTotal_levels_num(totalLevelsNum);

        // Create level params for each level. Run over the total levels number.
        for(int i = 1; i <= totalLevelsNum; i++){
            levelsParams.add(new LevelParams(i));
        }
        gameParams.setGameSetting(Enums.GameSetting.Experimental);
        gameParams.setLevels_params(levelsParams);
        gameParams.setReciprocityValues(initReciprocityValues(gameParams));

        gameParams.setGeneration_policy(Enums.GenerationCoinsPolicy.WhenPossible);

        GenerationDelays generation_delays = new GenerationDelays();
        // for free play version - use extended form of generation delays.
        // i.e include delays for locked items (no need for that in the experimental version).
        if(gameParams.getGameSetting() == Enums.GameSetting.FreePlay){
            generation_delays.setBlocked_coins_generation_delay(10);
            generation_delays.setBlocked_virtual_coins_generation_delay(10);
        }

        gameParams.setGeneration_delays(generation_delays);
        gameParams.setShow_virtual_score(false);
        gameParams.setShow_human_score(true);
        gameParams.setGame_help_logic(Enums.HelpLogic.Pay);

        gameParams.setHelp_requests_interval(Constants.HELP_REQUESTS_INTERVAL);
        generation_delays.setCoins_generation_delay(3.5f);
        generation_delays.setVirtual_coins_generation_delay(3.5f);

        gameParams.setBackward_play_enabled(false);

        CooperativeGame.gameData.setVirtual_player_config(initVirtualPlayerConfig(gameParams));
        gameParams.setInstructions_method(Enums.InstructionsMethod.Combined);
        gameParams.setContinuity(Enums.GameContinuity.Continual);
        gameParams.setTimer_based(true);

        gameParams.setResetsScore(true);
        gameParams.setBonus_reward(false);
        gameParams.setShow_high_score(true);

        gameParams.setGame_language(Constants.GAME_LANGUAGE);

        CooperativeGame.gameData.setVirtual_player_config(initVirtualPlayerConfig(gameParams));
        gameParams.setInstructions_method(Enums.InstructionsMethod.Combined);
        gameParams.setContinuity(Enums.GameContinuity.Continual);
        gameParams.setTimer_based(true);

        gameParams.setResetsScore(true);
        gameParams.setBonus_reward(false);
        gameParams.setShow_high_score(true);

        return gameParams;
    }

    public String createStringFromJson(Object gameData){
        // Before sending the json string to DB, it is necessary to perform these steps
        // in order to ensure a valid json.
        Json json = new Json();
        // serialize all fields, including default values.
        // for example, it makes sure that an int which is 0 will not be ignored in the json string.
        //json.setUsePrototypes(false);
        // important for valid json!
        json.setOutputType(JsonWriter.OutputType.json);

        // the string jsonStr represents the GameData object in a json format.
        return json.toJson(gameData);
    }

    public String getCurrentTime(){
        // TODO GWT - SUPPORT IT!
        /*
        return Constants.HELP_REQUESTS_FORMAT.
                format(new Timestamp(System.currentTimeMillis()));
                */
        return null;
    }

    public String getStringTimeStamp(Timestamp timestamp){
        // TODO GWT - SUPPORT IT!
        /*
        return Constants.HELP_REQUESTS_FORMAT.
                format(timestamp);
                */
        return null;
    }

    class TabletData {
        String tablet_code, group;

        public String getTablet_code() {
            return tablet_code;
        }

        public String getGroup() {
            return group;
        }

        public void setTablet_code(String tablet_code) {
            this.tablet_code = tablet_code;
        }

        public void setGroup(String group) {
            this.group = group;
        }
    }

    public TabletData getTabletData(){
        TabletData tabletData = new TabletData();
        String tablet_code = "-1", group = "not_specified";
        FileHandle file;
        boolean b = Gdx.files.isExternalStorageAvailable();
        if(b) {
            try{
                file = Gdx.files.getFileHandle("tablet_config.json", Files.FileType.External);
                String text = file.readString();
                JsonReader json = new JsonReader();
                // read the json file.
                JsonValue base = json.parse(text);
                // get the json data according to the wanted fields.
                tablet_code = base.getString("tablet_code");
                group = base.getString("group");

                tabletData.setTablet_code(tablet_code);
                tabletData.setGroup(group);

            } catch (Exception exception) {
                // if the type is classpath or internal and the file does not exist.
                // and if the file handle represents a directory, doesn't exist, or could not be read.

                // TODO SARIT - CHECK THIS RETURN.
            }
        }
        tabletData.setTablet_code(tablet_code);
        Constants.TABLET_CODE = tablet_code;

        tabletData.setGroup(group);
        Constants.SETTING_GROUP = group;

        return tabletData;
    }
}

