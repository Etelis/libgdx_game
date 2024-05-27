package coopworld.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import coopworld.game.CooperativeGame;
import coopworld.game.Logs.Enums;
import coopworld.game.Screens.GameScreens.CombinedInstructions;
import coopworld.game.Screens.GameScreens.GameScreen;

import static coopworld.game.Screens.GameScreens.CombinedInstructions.InstructionalStepState.CoinsCollecting;
import static coopworld.game.Tools.Constants.LOCKED_ELEMENT_VALUE;

/**
 * Created by Chen on 04/07/2017.
 */

public class LockedCoin extends Coin{
    public LockedCoin(World world, TiledMap map, Rectangle bounds, int size, GameScreen gameScreen) {
        super(world, map, bounds, size, gameScreen);
        initLockedCoin();
    }

    public LockedCoin(World world, TiledMap map, int col, int row, int size, GameScreen gameScreen) {
        super(world, map, col, row, size, gameScreen);
        initLockedCoin();
    }

    public void initLockedCoin(){
        value = LOCKED_ELEMENT_VALUE;
        elementName = "locked_coin";
    }

    @Override
    public void coinHit(boolean virtualHit){
        if(CooperativeGame.gameData.getGame_params().getInstructions_method() == Enums.InstructionsMethod.Combined){
            if(gameScreen instanceof  CombinedInstructions){
                if(((CombinedInstructions)gameScreen).getInstructionalStepCurrState() == CoinsCollecting){
                    gameScreen.setState(GameScreen.State.SpecialCoinExplanation);
                }
            }
        }
        // extended instructions method.
        else if (CooperativeGame.gameData.getGame_params().getInstructions_method() == Enums.InstructionsMethod.Extended){
            if(gameScreen.levelNum == 1) {
                gameScreen.setState(GameScreen.State.SpecialCoinExplanation);
            }
        }
        humanCoinCollision();
        // todo - deleted because of duplicate increasing.
        //increaseScore();
    }

    public void increaseScore(){
        gameScreen.increaseScore(Enums.Player.Human, value, true);
    }
}
