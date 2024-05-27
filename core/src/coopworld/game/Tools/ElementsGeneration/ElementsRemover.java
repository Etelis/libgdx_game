package coopworld.game.Tools.ElementsGeneration;

import java.util.Random;

import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Sprites.Coin;
import coopworld.game.Sprites.VirtualPlayerCoin;
import coopworld.game.Tools.AiTools.MatrixBuilder;
import coopworld.game.Tools.AiTools.MatrixCell;
import coopworld.game.Tools.VisualUtils;

/**
 * Created by Chen on 26/11/2017.
 */

public class ElementsRemover {
    static GameScreen gameScreen;

    public ElementsRemover(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public static void removeCoin(Coin c) {
        c.remove();
        gameScreen.getCoins().remove(c);
    }

    public static void removeAllCoins() {
        int size = gameScreen.getCoins().size();
        for (int i = 0; i < size; i++) {
            Coin c = gameScreen.getCoins().get(0);
            c.remove();
            gameScreen.getMatrixBuilder().addToValidPositionsList(c);
        }
        //gameScreen.getCoins().clear();
    }

    public static void removeAllVirtualCoins() {
        int size = gameScreen.getVirtualCoins().size();
        for (int i = 0; i < size; i++) {
            VirtualPlayerCoin vc = gameScreen.getVirtualCoins().get(0);
            vc.remove();
            gameScreen.getMatrixBuilder().addToValidPositionsList(vc);
        }
        //gameScreen.getVirtualCoins().clear();
    }

    public static void generateAllCoin(){
        for(int i = 0; i < gameScreen.getMatrixBuilder().
                getEmptyCoinsPositions().size(); i++){
            MatrixCell pickedCell = gameScreen.getMatrixBuilder().getEmptyCoinsPositions().
                    get(i);
            if(!VisualUtils.playerOverlaps(pickedCell, 1)) {
                gameScreen.getRandomGenerator().generateRandomTile(MatrixBuilder.cellType.coins,
                        pickedCell, false);
            }
        }
    }

    public static void generateAllVirtualCoins(){
        for(int i = 0; i < gameScreen.getMatrixBuilder().
                getEmptyVirtualCoinsPositions().size(); i++){
            MatrixCell pickedCell = gameScreen.getMatrixBuilder().getEmptyVirtualCoinsPositions().
                    get(i);
            if(!VisualUtils.playerOverlaps(pickedCell, 1)) {
                gameScreen.getRandomGenerator().generateRandomTile
                        (MatrixBuilder.cellType.virtualcoins, pickedCell, false);
            }
        }
    }

    // CREATE ALL COINS, ALL ICE CUBES.

    public static boolean createOneIceObstacle(){
        if(gameScreen.getMatrixBuilder().getEmptyIceObstaclesPositions().size() != 0) {
            MatrixCell pickedCell = gameScreen.getMatrixBuilder().getEmptyIceObstaclesPositions().get(0);
            if (!VisualUtils.playerOverlaps(pickedCell, 3)) {
                return gameScreen.getRandomGenerator().generateRandomTile(MatrixBuilder.cellType.iceObstacles,
                        pickedCell, true);
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public static boolean createOneNatureObstacle(){
        int num = gameScreen.getMatrixBuilder().getEmptyNatureObstaclesPositions().size();
        Random rand = new Random();
        if(num != 0) {
            MatrixCell pickedCell = gameScreen.getMatrixBuilder().getEmptyNatureObstaclesPositions().get(rand.nextInt(num));
            if (!VisualUtils.playerOverlaps(pickedCell, 3)) {
                return gameScreen.getRandomGenerator().generateRandomTile(MatrixBuilder.cellType.natureObstacles,
                        pickedCell, true);
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }
}
