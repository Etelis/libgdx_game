package coopworld.game.Sprites.Players.Personalities;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.World;

import coopworld.game.CrossPlatform.GeneralUtils;
import coopworld.game.Sprites.Players.Player;
import coopworld.game.Sprites.Players.VirtualPlayer;
import coopworld.game.Tools.AiTools.DataStructures.Node;

/**
 * Created by Chen on 05/05/2017.
 */

public class ALLC_virtualPlayer extends VirtualPlayer{
    public boolean response(){
        return true;
    }

    public Node answerCall() {
        return null;
    }

    public ALLC_virtualPlayer(GeneralUtils utils, World w, Screen screen, Player p) {
        super(utils, w, screen, p);
    }
}
