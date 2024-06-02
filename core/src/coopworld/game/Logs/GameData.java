package coopworld.game.Logs;

import java.util.ArrayList;

/**
 * Created by User on 22/03/2017.
 */
public class GameData {
    private transient User user; // User.
    private String user_id; // User id.

    // will not be sent! gameData is sent to the server only on start, without level logs.
    private transient ArrayList<LevelLog> level_logs; // LevelLog for each played level.
    private String tablet_id;
    private String group_from_tablet;

    private String framework_name;
    // apk version from manifest.
    private String apk_version;
    private transient GameParams game_params;
    // TODO - RAPID WORKERS. for experiments.
    // Virtual player configuration.
    private ArrayList<VirtualPlayerConfig> virtual_player_config;
    private String start_time; // Game start time - TimeStamp as a string.
    private transient Scores cumulativeScores;

    public GameData() {
        this.level_logs = new ArrayList<LevelLog>();
        //this.user = new User();
        this.virtual_player_config = new ArrayList<VirtualPlayerConfig>();
        // set scores.
        Scores cumulativeScores = new Scores();
        cumulativeScores.setHuman_player_score(0);
        cumulativeScores.setVirtual_player_score(0);
        this.cumulativeScores = cumulativeScores;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void addLevel_log(LevelLog level_log) {
        this.level_logs.add(level_log);
    }

    public User getUser() {
        return user;
    }

    public void setTablet_id(String tablet_id) {
        this.tablet_id = tablet_id;
    }

    public void setFramework_name(String framework_name) {
        this.framework_name = framework_name;
    }

    public void setApk_version(String apk_version) {
        this.apk_version = apk_version;
    }

    public GameParams getGame_params() {
        return game_params;
    }

    public void setGame_params(GameParams game_params) {
        this.game_params = game_params;
    }

    public ArrayList<VirtualPlayerConfig> getVirtualPlayerConfig() {
        return virtual_player_config;
    }

    public void setVirtual_player_config(ArrayList<VirtualPlayerConfig> virtualPlayerConfig) {
        this.virtual_player_config = virtualPlayerConfig;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public Scores getCumulativeScores() {
        return cumulativeScores;
    }

    public String getApk_version() {
        return apk_version;
    }

    public String getFramework_name() {
        return framework_name;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getTablet_id() {
        return tablet_id;
    }

    public void setGroup(String group) {
        this.group_from_tablet = group;
    }
}
