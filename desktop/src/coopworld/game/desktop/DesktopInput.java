package coopworld.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import coopworld.game.CrossPlatform.Input;
import coopworld.game.Sprites.Players.Player;

/**
 * Created by Chen on 09/10/2017.
 */

public class DesktopInput extends Input {
    public void init(Player player, Stage stage){
        this.player = player;
    }

    @Override
    public void arrowMovement() {
        /* Stop the player when he is on ground and no presses on left/right. */
        if((!(Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DPAD_RIGHT)))
            && (!(Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DPAD_LEFT)))
            && player.b2body_boy.getLinearVelocity().y == 0){
            player.b2body_boy.setLinearVelocity(new Vector2(0f, 0));
        }

        if(Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DPAD_LEFT)){
            player.left();
        }
        if(Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DPAD_RIGHT)){
            player.right();
        }
        if(Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DPAD_UP)){
            player.up();
        }
    }
}
