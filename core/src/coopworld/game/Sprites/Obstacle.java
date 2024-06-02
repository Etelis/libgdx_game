package coopworld.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;

import coopworld.game.CooperativeGame;
import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.MusicPlayer;

/**
 * Created by Eva on 10/16/2016.
 */
public abstract class Obstacle extends GeneratedTileObject {
    protected MusicPlayer music;
    //protected BasicAnimatedImage disappearingAnimation;

    /**
     * Constructor with the world, the tilemap and the bounds of obstacle cell.
     * calls the constructor of InteractiveTileObject.
     *
     * @param world - world of game.
     * @param map - tilemap of the level .
     * @param bounds - bounds of coin cell.
     */
    public Obstacle(World world, TiledMap map, Rectangle bounds, int size, GameScreen gameScreen){
        super(world, map, bounds, size, gameScreen);
        /* User data will be this coin object. */
        fixture.setUserData(this);
        music = MusicPlayer.getInstance();
    }

    public Obstacle (World world, TiledMap map,int col,int row, int size, GameScreen gameScreen){
        super(world, map, col,row, size, gameScreen);
        /* User data will be this coin object. */
        fixture.setUserData(this);
        music = MusicPlayer.getInstance();
    }


    public void removeGraphicObstacleFromMap()
    {
        /*
              0 0 0
              0 1 0
              0 0 0
        */
        int centerCellX = (int)(body.getPosition().x * CooperativeGame.PPM / 64);
        int centerCellY =  (int)(body.getPosition().y * CooperativeGame.PPM / 64);
        for (int a = centerCellY + 1; a >= centerCellY - 1; a--) {
            for (int b = centerCellX - 1; b <= centerCellX + 1; b++) {
                if (!((a == centerCellY) && (b == centerCellX))) { // dont remove the inner coin
                    layer.getCell(b, a).setTile(null);
                }
            }
        }
    }

    public void removeObstacleFromMatrix()
    {
        // from top-left.
        for (int i = row; i < row + 3; i++) {
            for (int j = col; j < col + 3; j++) {
                //System.out.println(GameScreen.searchableMatrix.getMatrix()[i][j] + ",,,");
                if (!((i == row + 1) && (j == col + 1))) { // don't remove the inner coin cell in matrix
                    GameScreen.searchableMatrix.resetCell(j, i);
                }
            }
        }
        //System.out.println(",,,,,,,,,,,");
    }

    public abstract void removeFromList();

    public void remove(){
        setCategoriyFilter(Constants.destroyedBit);
        removeGraphicObstacleFromMap();
        removeObstacleFromMatrix();
        removeFromList();
        // for random generator use - to mark that this position is now legal for re-creation.
        GameScreen.m.addToValidPositionsList(this);
        /*
        disappearingAnimation.resetAnimation();
        int x = (int)(bounds.getX());
        int y = (int)(bounds.getY());

        disappearingAnimation.setPosition(x, y);
        gameScreen.stage.addActor(disappearingAnimation);
        */
    }

    /**
     * This method is called with any type of collision with obstacle .
     * Will be overriden by extended classes according to player's capacity
     * function operation: write collision in log
     */
    public abstract void obstacleHit(boolean virtualHit);

    /**
     * This method is called whenever the player's head collides with obstacle .
     * function operation: calls the obstacleHit function above.
     */
    @Override
    public void HeadHit(boolean virtualHit, Contact contact){
        //obstacleHit(virtualHit);
    }

    /**
     * This method is called whenever the player's legs collides with obstacle.
     * function operation: calls the obstacleHit function above.
     */
    @Override
    public void LegsHit(boolean virtualHit ,Contact contact) {
        //obstacleHit(virtualHit);
    }

    /**
     * This method is called whenever the player's right side collides with obstacle.
     * function operation: calls the obstacleHit function above.
     */
    @Override
    public void RightHit(boolean virtualHit,Contact contact) {
        obstacleHit(virtualHit);
    }

    /**
     * This method is called whenever the player's left side collides with obstacle.
     * function operation: calls the obstacleHit function above.
     */
    @Override
    public void LeftHit(boolean virtualHit,Contact contact) {
        obstacleHit(virtualHit);
    }



}
