package coopworld.game.Logs;

import java.util.ArrayList;

/**
 * Created by Chen on 11/04/2017.
 */

public class GameParams {
    // The framework name - "RapidWorkers".
    private String framework_name;
    // apk version from manifest.
    private String apk_version;
    // Level specific parameters, for each level.
    private ArrayList<LevelParams> levels_params;
    // GameSetting - "FreePlay"/"Experimental".
    private Enums.GameSetting setting;
    // The items (coins for human player and virtual coins for the virtual player) values, locked and free.
    private ReciprocityValues reciprocity_values;
    // Game Language.
    private GameLanguage language;
    // "Pay"/"Walk".
    private Enums.HelpLogic help_logic;
    // Virtual player configuration.
    private VirtualPlayerConfig virtual_player_config;

    // Total possible levels in the game.
    private int total_levels_num;
    // Determine if the game will be played by a one-shpt player, or continual one.
    private Enums.GameContinuity continuity;
    // Determine if previous levels can be played again.
    private boolean backward_play_enabled;

    private Enums.GenerationCoinsPolicy generation_policy; // Coins dynamic generation policy -
    // WhenNoCoins/WhenPossible - generate new coins only when there
    // are no coins on board, or - when it is possible (there is place).

    private GenerationDelays generation_delays; // delay (rate) of elements generation during the game.

    private float help_requests_interval; // minimal time (in sec.) between help requests.

    private boolean show_virtual_score; // Yes/No - Virtual player score displaying.
    private boolean show_human_score; // Yes/No - Human player score displaying.

    private boolean bonus_reward; // Yes/No - Human player score displaying.

    private Enums.InstructionsMethod instructions_method;

    private boolean resetsScore;
    private boolean show_high_score;
    private boolean timer_based;

    private double show_up_fee;

    public void setLevels_params(ArrayList<LevelParams> levels_params) {
        this.levels_params = levels_params;
    }

    public void setGeneration_policy(Enums.GenerationCoinsPolicy generation_policy) {
        this.generation_policy = generation_policy;
    }

    public void setGeneration_delays(GenerationDelays generation_delays) {
        this.generation_delays = generation_delays;
    }

    public void setShow_virtual_score(boolean show_virtual_score) {
        this.show_virtual_score = show_virtual_score;
    }

    public void setVirtual_player_config(VirtualPlayerConfig virtual_player_config) {
        this.virtual_player_config = virtual_player_config;
    }

    public ReciprocityValues getReciprocityValues() {
        return reciprocity_values;
    }

    public void setReciprocityValues(ReciprocityValues reciprocityValues) {
        this.reciprocity_values = reciprocityValues;
    }

    public int getTotal_levels_num() {
        return total_levels_num;
    }

    public ArrayList<LevelParams> getLevels_params() {
        return levels_params;
    }

    public GenerationDelays getGeneration_delays() {
        return generation_delays;
    }

    public void setFramework_name(String framework_name) {
        this.framework_name = framework_name;
    }

    public void setApk_version(String apk_version) {
        this.apk_version = apk_version;
    }

    public void setGame_help_logic(Enums.HelpLogic game_help_logic) {
        this.help_logic = game_help_logic;
    }

    public void setTotal_levels_num(int total_levels_num) {
        this.total_levels_num = total_levels_num;
    }

    public GameLanguage getGame_language() {
        return language;
    }

    public Enums.HelpLogic getGame_help_logic() {
        return help_logic;
    }

    public void setGame_language(GameLanguage game_language) {
        this.language = game_language;
    }

    public VirtualPlayerConfig getVirtual_player_config() {
        return virtual_player_config;
    }

    public Enums.GameSetting getGameSetting() {
        return setting;
    }

    public void setGameSetting(Enums.GameSetting gameSetting) {
        this.setting = gameSetting;
    }

    public Enums.GameContinuity getContinuity() {
        return continuity;
    }

    public void setContinuity(Enums.GameContinuity continuity) {
        this.continuity = continuity;
    }

    public boolean isBackward_play_enabled() {
        return backward_play_enabled;
    }

    public void setBackward_play_enabled(boolean backward_play_enabled) {
        this.backward_play_enabled = backward_play_enabled;
    }

    public String getFramework_name() {
        return framework_name;
    }

    public float getHelp_requests_interval() {
        return help_requests_interval;
    }

    public void setHelp_requests_interval(float help_requests_interval) {
        this.help_requests_interval = help_requests_interval;
    }

    public void setShow_human_score(boolean show_human_score) {
        this.show_human_score = show_human_score;
    }

    public boolean isShow_human_score() {
        return show_human_score;
    }

    public boolean isBonus_reward() {
        return bonus_reward;
    }

    public void setBonus_reward(boolean bonus_reward) {
        this.bonus_reward = bonus_reward;
    }

    public Enums.InstructionsMethod getInstructions_method() {
        return instructions_method;
    }

    public void setInstructions_method(Enums.InstructionsMethod instructions_method) {
        this.instructions_method = instructions_method;
    }

    public boolean isResetsScore() {
        return resetsScore;
    }

    public void setResetsScore(boolean resetsScore) {
        this.resetsScore = resetsScore;
    }

    public boolean isShow_high_score() {
        return show_high_score;
    }

    public void setShow_high_score(boolean show_high_score) {
        this.show_high_score = show_high_score;
    }

    public boolean isTimer_based() {
        return timer_based;
    }

    public void setTimer_based(boolean timer_based) {
        this.timer_based = timer_based;
    }

    public void setShow_up_fee(double show_up_fee) {
        this.show_up_fee = show_up_fee;
    }

    public double getShow_up_fee() {
        return show_up_fee;
    }
}
