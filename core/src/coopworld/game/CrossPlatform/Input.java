package coopworld.game.CrossPlatform;

import com.badlogic.gdx.scenes.scene2d.Stage;

import coopworld.game.Sprites.Players.Player;

/**
 * Created by Chen on 09/10/2017.
 */

public abstract class Input {
    protected Player player;
    public abstract void arrowMovement();
    public abstract void init(Player player, Stage stage);
}
