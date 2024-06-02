package coopworld.game.Sprites.Players.Personalities;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import coopworld.game.CrossPlatform.GeneralUtils;
import coopworld.game.Sprites.Players.Player;
import coopworld.game.Sprites.Players.VirtualPlayer;
import coopworld.game.Tools.AiTools.DataStructures.Node;

/**
 * Created by Chen on 05/05/2017.
 */

public class PROBABILITY_virtualPlayer extends VirtualPlayer{
    Random random;
    public PROBABILITY_virtualPlayer(GeneralUtils utils, World w, Screen screen, Player p) {
        super(utils, w, screen, p);
        random = new Random();
    }

    @Override
    public Node answerCall() {
        return null;
    }

    @Override
    // todo - check it.
    public boolean response() {
        return random.nextBoolean();
    }
}
