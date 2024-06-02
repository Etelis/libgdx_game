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

public class ALLD_virtualPlayer extends VirtualPlayer{
    public ALLD_virtualPlayer(GeneralUtils utils, World w, Screen screen, Player p) {
        super(utils, w, screen, p);
    }

    @Override
    public Node answerCall() {
        return null;
    }

    public boolean response(){
        return false;
    }

}
