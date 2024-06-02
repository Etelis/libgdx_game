package coopworld.game.Sprites.Players.Personalities;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import coopworld.game.CooperativeGame;
import coopworld.game.CrossPlatform.GeneralUtils;
import coopworld.game.Logs.Enums;
import coopworld.game.Sprites.Players.Player;
import coopworld.game.Sprites.Players.VirtualPlayer;
import coopworld.game.Tools.AiTools.DataStructures.Node;
import coopworld.game.Tools.Constants;

/**
 * Created by Chen on 05/05/2017.
 */

public class ProbabilisticTFT_virtualPlayer extends VirtualPlayer {
    Random rand;
    Player humanPlayer;

    public ProbabilisticTFT_virtualPlayer(GeneralUtils utils, World w, Screen screen, Player p, Player humanPlayer) {
        super(utils, w, screen, p);
        rand = new Random();
        this.humanPlayer = humanPlayer;
    }

    @Override
    // assume turn by turn.
    public boolean response() {
        // start with cooperation.
        if (CooperativeGame.humanResponses.isEmpty()){ // relevant for the first response.
            humanPlayer.getHumanPlayerRequestUtils().fillChosenStrategy(Enums.ChosenStrategy.PREDEFINED_HELP);
            return true;
        }
        // at least one human response is available.
        else{
            boolean lastHumanResponse = CooperativeGame.humanResponses.get(CooperativeGame.humanResponses.size() - 1).
                    getWasAccepted();
            if (rand.nextDouble() < Constants.TFT_PROBABILITY) { // TFT
                humanPlayer.getHumanPlayerRequestUtils().fillChosenStrategy(Enums.ChosenStrategy.TFT);
                return lastHumanResponse;
            }
            else{ // reversed TFT
                humanPlayer.getHumanPlayerRequestUtils().fillChosenStrategy(Enums.ChosenStrategy.REVERSED_TFT);
                return (!lastHumanResponse);
            }
        }
    }

    @Override
    public Node answerCall() {
        return null;
    }
}
