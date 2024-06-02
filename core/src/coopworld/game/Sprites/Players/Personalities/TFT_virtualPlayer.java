package coopworld.game.Sprites.Players.Personalities;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.World;

import coopworld.game.CooperativeGame;
import coopworld.game.CrossPlatform.GeneralUtils;
import coopworld.game.Sprites.Players.Player;
import coopworld.game.Sprites.Players.VirtualPlayer;
import coopworld.game.Tools.AiTools.DataStructures.Node;

/**
 * Created by Chen on 05/05/2017.
 */

public class TFT_virtualPlayer extends VirtualPlayer {
    public TFT_virtualPlayer(GeneralUtils utils, World w, Screen screen, Player p) {
        super(utils, w, screen, p);
    }

    @Override
    // assume turn by turn.
    public boolean response() {
        // start with cooperation.
        if (CooperativeGame.humanResponses.isEmpty()){
            return true;
        }
        // at least one human response is available.
        else{
            if (CooperativeGame.humanResponses.get(CooperativeGame.humanResponses.size() - 1).
                    getWasAccepted() == true) {
                return true;
            }
            else if (CooperativeGame.humanResponses.get(CooperativeGame.humanResponses.size() - 1).
                    getWasAccepted() == false){
                return false;
            }
        }
        return true;
    }

    @Override
    public Node answerCall() {
        return null;
    }
}
