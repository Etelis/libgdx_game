package coopworld.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import coopworld.game.Screens.GameScreens.CombinedInstructions;
import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Tools.Constants;

/**
 * Created by Eva on 11/14/2016.
 */
public class IceObstacle extends Obstacle {
    /**
     * Constructor with the world, the tilemap and the bounds of obstacle cell.
     * calls the constructor of Obstacle.
     *
     * @param world - world of game.
     * @param map - tilemap of the level .
     * @param bounds - bounds of coin cell.
     */
    public IceObstacle(World world, TiledMap map, Rectangle bounds, int size, GameScreen gameScreen){
        super(world, map, bounds, size, gameScreen);
        setCategoriyFilter(Constants.iceObsBit);
        if(!(gameScreen instanceof CombinedInstructions)){
            gameScreen.stage.addActor(gameScreen.getHelpButton());
        }
    }
    public IceObstacle (World world, TiledMap map, int col,int row, int size, GameScreen gameScreen){
        super(world, map, col, row, size, gameScreen);
        setCategoriyFilter(Constants.iceObsBit);
        if(!(gameScreen instanceof CombinedInstructions)){
            gameScreen.stage.addActor(gameScreen.getHelpButton());
        }
    }

    public void removeFromList(){
        //gameScreen.getHelpButton().remove();
        GameScreen.getIceObstacles().remove(this);
    }

    /**
     * This method is called with any type of collision with obstacle .
     * function operation: write collision in log
     */
    public void obstacleHit(boolean virtualHit){
        // virtual breaks the ice obstacle.
        if(virtualHit) {
            Gdx.app.log("INTO obstacle", "virtual player collision");
            //System.out.println("++++++++++++++++++++++++++++ ROW " + this.row + " COL " + this.col);
            remove();

            // try to remove node from queue call if it exists.
            // +1 for row and col - because we need to deque the coin index and not the obstacle.
            GameScreen.getVirtualPlayer().dequeueNodeFromQueue(row + 1, col + 1);

            if (!GameScreen.searchableMatrix.removeIceObstacleFromList(this)) {
                Gdx.app.log("Error!", "Ice Obstacle couldn't be removed from the list");
            }
        }
        // human
        else {
            // todo CHECK
            gameScreen.humanArrivedIceObstacle++;
            gameScreen.humanArrivedIceObstacleFlag = true;
            Gdx.app.log("INTO obstacle", "real player collision");
        }
    }
}