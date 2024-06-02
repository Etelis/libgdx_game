package coopworld.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;

import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.MusicPlayer;

import static coopworld.game.Tools.Constants.FREE_ELEMENT_VALUE;

/**
 * Created by Eva on 9/30/2016.
 */
public class VirtualPlayerCoin extends GeneratedTileObject {
    MusicPlayer music;
    protected int value;
    protected String elementName;

    /**
     * Constructor with the world, the tilemap and the bounds of coin cell.
     * calls the constructor of InteractiveTileObject.
     *
     * @param world - world of game.
     * @param map - tilemap of the level .
     * @param bounds - bounds of coin cell.
     */
    public VirtualPlayerCoin (World world, TiledMap map, Rectangle bounds, int size, GameScreen gameScreen){
        super(world, map, bounds, size, gameScreen);
        initVirtualCoin();

        /*
        System.out.println("##############################################");
        System.out.println("bounds get x" + bounds.getX());
        System.out.println("bounds get y" + bounds.getY());
        System.out.println("body y: " + body.getPosition().y + "body x: " + body.getPosition().x);
        System.out.println("bounds width " + Float.toString((bounds.getWidth()/2)/ CooperativeGame.PPM) + "bounds height  " + Float.toString((bounds.getHeight()/2)/ CooperativeGame.PPM));
        System.out.println("##############################################");
        */
    }

    public VirtualPlayerCoin (World world, TiledMap map,int col,int row, int size, GameScreen gameScreen){
        super(world, map, col, row, size, gameScreen);
        initVirtualCoin();
    }

    public void initVirtualCoin(){
        /* User data will be this coin object. */
        fixture.setUserData(this);
        /* Use to cell of coin after collision. */
        setCategoriyFilter(Constants.vCoinBit);
        music = MusicPlayer.getInstance();
        value = FREE_ELEMENT_VALUE;
        elementName = "ice_cube";
    }

    /**
     * This method is called with any type of collision with Virtual coin .
     * function operation: write collision in log, play sound
     * and deletes the coin from the map by setting the cell og coin to null.
     */
    public void virtualCoinHit(boolean virtualHit){
        // Write collision in log, play sound.
        if(!virtualHit) {
            Gdx.app.log("INTO virtual coin", "real player collision");
        }
        else {
            Gdx.app.log("INTO virtual coin", "virtual player collision");
            //music.diamondSound();
            //System.out.println("**************************************X: " + this.col + "Y:"+ this.row);

            gameScreen.getElementCollectionList().addElementCollection(elementName);
            remove();

            GameScreen.virtualScore += value;

            // for random generator use - to mark that this position is now legal for re-creation.
            if(!isBlocked()) {
                GameScreen.m.addToValidPositionsList(this);
            }

            /*
            // todo - Pay attention! only if it was blocked!
            if (!GameScreen.searchableMatrix.removeBlockedVirtualCoin(this)) {
                Gdx.app.log("Error!", "Virtual coin couldn't be removed from the blocked virtual" +
                        "coins list - (maybe it was not blocked at all...)");
            }
            */

            Gdx.app.log("UPDATE : ", Integer.toString(GameScreen.searchableMatrix.getVirtualCoinsList().size()));
        }
    }

    public void remove(){
        setCategoriyFilter(Constants.destroyedBit);
        getCell().setTile(null);  // Delete the coin from map by setting its cell to null.
        GameScreen.searchableMatrix.resetCell(this.col, this.row); // set back to 0 in matrix.

        // todo - not sure about this...
        if (!GameScreen.searchableMatrix.removeVirtualCoinFromList(this)) {
            Gdx.app.log("Error!", "Virtual coin couldn't be removed from the virtual" +
                    "coins list");
        }
        int b = gameScreen.getVirtualCoins().size();
        gameScreen.getVirtualCoins().remove(this);
        int a = gameScreen.getVirtualCoins().size();

    }

    @Override
    public void HeadHit(boolean virtualHit,Contact contact){
        virtualCoinHit(virtualHit);
    }

    /**
     * This method is called whenever the player's legs collides with coin.
     * function operation: calls the coinHit function above.
     */
    @Override
    public void LegsHit(boolean virtualHit,Contact contact) {
        virtualCoinHit(virtualHit);

    }

    /**
     * This method is called whenever the player's right side collides with coin.
     * function operation: calls the coinHit function above.
     */
    @Override
    public void RightHit(boolean virtualHit,Contact contact) {
        virtualCoinHit(virtualHit);
    }

    /**
     * This method is called whenever the player's left side collides with coin.
     * function operation: calls the coinHit function above.
     */
    public void LeftHit(boolean virtualHit,Contact contact) {
        virtualCoinHit(virtualHit);
    }

    // check if this coin is blocked. assuming blocked means that a nature obstacle is to the right.
    public boolean isBlocked(){
        if (GameScreen.searchableMatrix.getMatrix()
                [this.getRow()][this.getCol() + 1] == Constants.NATURE_OBSTACLES_MATRIX_CODE){
            return true;
        }
        return false;
    }
}
