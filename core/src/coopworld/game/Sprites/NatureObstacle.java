package coopworld.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Tools.AiTools.DataStructures.Node;
import coopworld.game.Tools.Constants;


/**
 * Created by Eva on 11/14/2016.
 */
public class NatureObstacle extends Obstacle {
    /**
     * Constructor with the world, the tilemap and the bounds of obstacle cell.
     * calls the constructor of Obstacle.
     *
     * @param world - world of game.
     * @param map - tilemap of the level .
     * @param bounds - bounds of coin cell.
     */
    public NatureObstacle(World world, TiledMap map, Rectangle bounds, int size, GameScreen gameScreen){
        super(world, map, bounds, size, gameScreen);
        setCategoriyFilter(Constants.natureObsBit);
        /*
        disappearingAnimation = GraphicTools.getBasicAnimatedImage
                ("animations/ice_disappear.pack", 0.0667f, "ice_disappear", 1);
        */
    }

    public NatureObstacle(World world, TiledMap map, int col, int row, int size, GameScreen gameScreen){
        super(world, map, col, row, size, gameScreen);
        setCategoriyFilter(Constants.natureObsBit);
        /*
        disappearingAnimation = GraphicTools.getBasicAnimatedImage
                ("animations/ice_disappear.pack", 0.0667f, "ice_disappear", 1);
        */
    }

    public void setCoinFree()
    {
        HashMap<Node,ArrayList<Node>> hashMap = GameScreen.searchableMatrix.getBlockedVirtualCoinsList();
        Set<Node> keyset = hashMap.keySet();
        Iterator<Node> iter = keyset.iterator();
        Node key;
        while (iter.hasNext()) {
           key = iter.next();
            if ((key.getMatrixCell().getX() == col+1) && (key.getMatrixCell().getY() == row+1)) {
                //set free!
                GameScreen.searchableMatrix.getVirtualCoinsList().add(key);
                keyset.remove(key);
                break;
            }
        }
    }
    public void removeFromList(){
        GameScreen.getNatureObstacles().remove(this);
    }
    /**
     * This method is called with any type of collision with obstacle.
     * function operation: write collision in log
     */
    public void obstacleHit(boolean virtualHit){
        /* Write collision in log */
        if(virtualHit) {
            // todo CHECK
            GameScreen.virtualArrivedNatureObstacle++;
            Gdx.app.log("INTO obstacle", "virtual player collision");
        }
        else {
            Gdx.app.log("INTO obstacle", "real player collision");
            this.music.thanksSound();
            // todo! check.
            GameScreen.humanHelped++;
            //System.out.println("++++++++++++++++++++++++++++ ROW " + this.row + " COL " + this.col);

            remove();

            // for random generator use - to mark that this position is now legal for re-creation.
            setCoinFree();

            GameScreen.addThanksButton = true;

            // nature obstacle was broken so virtual player could get to its ice cube
            if (GameScreen.isWaitingForHelp()) {
                GameScreen.setWaitingForHelp(false);
            }
            if (!GameScreen.searchableMatrix.removeNatureObstacleFromList(this)) {
                Gdx.app.log("Error!", "Nature Obstacle couldn't be removed from the list");
            }
        }
    }


}