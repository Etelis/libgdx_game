package coopworld.game.Logs;

/**
 * Created by Chen on 12/04/2017.
 */

public class Scores {
    private int human_player_score;
    private int virtual_player_score;


    public Scores() {
    }

    public void setHuman_player_score(int human_player_score) {
        this.human_player_score = human_player_score;
    }

    public void setVirtual_player_score(int virtual_player_score) {
        this.virtual_player_score = virtual_player_score;
    }

    public int getHuman_player_score() {
        return human_player_score;
    }

    public int getVirtual_player_score() {
        return virtual_player_score;
    }

    public void increaseHumanScore(int inc){
        this.human_player_score += inc;
    }

    public void increaseVirtualScore(int inc){
        this.virtual_player_score += inc;
    }
}
