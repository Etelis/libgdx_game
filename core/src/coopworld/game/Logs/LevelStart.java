package coopworld.game.Logs;

import com.badlogic.gdx.Gdx;

import coopworld.game.CooperativeGame;

/**
 * Created by User on 03/01/2017.
 */
public class LevelStart {
    private String user_id; // User.
    private int level_num; // Level Number. (?)
    private String start_time; // Level start time - TimeStamp as a string.


    public LevelStart(String user, int level_num) {
        this.start_time = ((CooperativeGame) Gdx.app.getApplicationListener()).
                crossPlatformObjects.getUtils().getTimeStamp();
        this.user_id = user;
        this.level_num = level_num;
    }
}
