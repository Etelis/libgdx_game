package coopworld.game.Logs;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

import coopworld.game.CooperativeGame;
import coopworld.game.Logs.HelpRequests.HelpRequest;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.FpsInfo;
import coopworld.game.Tools.PersistentData;

/**
 * Created by User on 03/01/2017.
 */
public class LevelLog {
    private String framework_name;
    // apk version from manifest.
    private String apk_version;
    private String experimenter;

    private String tablet_id;

    private String user_id; // User.
    private String user_name;
    private String player_gender;
    private String virtual_gender;

    private int level_num; // Level Number. (?)
    private String start_time; // Level start time - TimeStamp as a string.
    private String end_time; // Level end time - TimeStamp as a string.
    private ArrayList<coopworld.game.Logs.HelpRequests.HelpRequest> help_requests; // Details about helpRequests during the level.
    private Scores scores; // human and virtual players scores.
    private int num_of_stars; // Number of stars in the end of the level.
    private transient TotalElementsAmount total_elements_amount;
    private int screenshots_num;
    private String textual_output;
    private String virtual_strategy;
    private String group;
    private String map_name;
    private String level_type;
    private boolean new_record;

    private FpsInfo fps_info;
    private ArrayList<OnOffEvent> mute_events, pause_events;

    private String infrastructure;
    private boolean cheat_used;

    private String comments;

    private int tries;
    private String sendingInfo;

    private ArrayList<ElementCollectionList.ElementCollection> element_collection_list;

    public LevelLog(String user) {
        this.framework_name = CooperativeGame.gameData.getFramework_name();
        this.apk_version = CooperativeGame.gameData.getApk_version();
        this.tablet_id = CooperativeGame.gameData.getTablet_id();
        this.experimenter = CooperativeGame.gameData.getUser().getExperimenter();
        this.help_requests = new ArrayList<>();
        this.start_time = ((CooperativeGame) Gdx.app.getApplicationListener()).
                crossPlatformObjects.getUtils().getTimeStamp();
        this.user_id = user;
        this.screenshots_num = 0;
        this.group = PersistentData.GAME_GROUP.toString().toLowerCase();
        this.textual_output = "None";
        this.infrastructure = Constants.INFRASTRUCTURE;
        this.cheat_used = false;
        this.comments = "";
        this.tries = 1;
        this.sendingInfo = "";
    }

    public void setLevel_num(int level_num) {
        this.level_num = level_num;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setHelp_requests(ArrayList<coopworld.game.Logs.HelpRequests.HelpRequest> help_requests) {
        this.help_requests = help_requests;
    }

    public void setNum_of_stars(int num_of_stars) {
        this.num_of_stars = num_of_stars;
    }

    public void setTotal_elements_amount(TotalElementsAmount total_elements_amount) {
        this.total_elements_amount = total_elements_amount;
    }

    public void setScores(Scores scores) {
        this.scores = scores;
    }

    public int getLevel_num() {
        return level_num;
    }

    public void incScreenShotsNum(){
        this.screenshots_num += 1;
    }

    // private ArrayList<int[][]> boards; // Screen-shot of the board (int mapping - 18*30).
    // List of presses.

    public ArrayList<HelpRequest> getHelp_requests() {
        return help_requests;
    }

    public int getHumanScore(){
        return scores.getHuman_player_score();
    }

    public String getStart_time() {
        return start_time;
    }

    public void setTextual_output(String textual_output) {
        this.textual_output = textual_output;
    }

    public String getTextual_output() {
        return textual_output;
    }

    public String getVirtual_strategy() {
        return virtual_strategy;
    }

    public void setVirtual_strategy(String virtual_strategy) {
        this.virtual_strategy = virtual_strategy;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setPlayer_gender(String player_gender) {
        this.player_gender = player_gender;
    }

    public void setVirtual_gender(String virtual_gender) {
        this.virtual_gender = virtual_gender;
    }

    public void setMap_name(String map_name) {
        this.map_name = map_name;
    }

    public void setNew_record(boolean new_record) {
        this.new_record = new_record;
    }

    public void setLevel_type(String level_type) {
        this.level_type = level_type;
    }

    public void setFps_info(FpsInfo fps_info) {
        this.fps_info = fps_info;
    }

    public void setMute_events(ArrayList<OnOffEvent> mute_events) {
        this.mute_events = mute_events;
    }

    public void setPause_events(ArrayList<OnOffEvent> pause_events) {
        this.pause_events = pause_events;
    }

    public void setCheat_used(boolean cheat_used) {
        this.cheat_used = cheat_used;
    }

    public void addToComments(String str) {
        comments += str;
    }

    public void addToSendingInfo(String str) {
        sendingInfo += str;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public void setElementCollectionList(ElementCollectionList elementCollectionList) {
        this.element_collection_list = elementCollectionList.getCollectionList();
    }
}
