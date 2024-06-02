package coopworld.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import coopworld.game.Logs.Enums;
import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.MusicPlayer;

import static coopworld.game.Tools.Constants.FREE_ELEMENT_VALUE;

/*
 * Class Name: Coin
 * Description: represents the coins/diamonds in the map.
 * when player hits coin- the coin disappears from the map.
 */
public class Coin extends GeneratedTileObject {
    public static ArrayList<Integer> timesList = new ArrayList<Integer>();
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
    public Coin (World world, TiledMap map, Rectangle bounds, int size, GameScreen gameScreen){
        super(world, map, bounds, size, gameScreen);
        initCoin();
    }

    public Coin (World world, TiledMap map, int col, int row, int size, GameScreen gameScreen){
        super(world, map, col, row, size, gameScreen);
        initCoin();
    }

    public void initCoin(){
        /* User data will be this coin object. */
        fixture.setUserData(this);
        /* Use to cell of coin after collision. */
        setCategoriyFilter(Constants.coinBit);
        music = MusicPlayer.getInstance();
        value = FREE_ELEMENT_VALUE;
        elementName = "coin";
    }
    /**
     * This method is called with any type of collision with coin .
     * function operation: write collision in log, play sound
     * and deletes the coin from the map by setting the cell og coin to null.
     */
    public void coinHit(boolean virtualHit){
        /* Write collision in log, play sound. */
        if(virtualHit) {
            Gdx.app.log("INTO coin", "virtual player collision");
        }
        else {
            humanCoinCollision();
            // for random generator use - to mark that this position is now legal for re-creation.
            GameScreen.m.addToValidPositionsList(this);
        }
    }

    protected void humanCoinCollision(){
        Gdx.app.log("INTO coin", "real player collision");
        music.diamondSound(value);
        //System.out.println("**************************************X: " + this.col + "Y:" + this.row);
        remove();
        gameScreen.getElementCollectionList().addElementCollection(elementName);
        increaseScore();
    }

    public void increaseScore(){
        gameScreen.increaseScore(Enums.Player.Human, value, false);
    }

    public void remove(){
        setCategoriyFilter(Constants.destroyedBit);
        getCell().setTile(null);  /* Delete the coin from map by setting its cell to null. */
        if (!GameScreen.searchableMatrix.removeCoinFromList(this)) {
            Gdx.app.log("Error!", "Coin couldn't be removed from the list");
        }
        GameScreen.searchableMatrix.resetCell(this.col, this.row); // set back to 0 in matrix
        gameScreen.getCoins().remove(this);
    }

    /**
     * This method is called whenever the player's head collides with coin .
     * function operation: calls the coinHit function above.
     */
    @Override
    public void HeadHit(boolean virtualHit, Contact contact){
        coinHit(virtualHit);
    }

    /**
     * This method is called whenever the player's legs collides with coin.
     * function operation: calls the coinHit function above.
     */
    @Override
    public void LegsHit(boolean virtualHit ,Contact contact) {
        coinHit(virtualHit);
    }

    /**
     * This method is called whenever the player's right side collides with coin.
     * function operation: calls the coinHit function above.
     */
    @Override
    public void RightHit(boolean virtualHit,Contact contact) {
        coinHit(virtualHit);
    }

    /**
     * This method is called whenever the player's left side collides with coin.
     * function operation: calls the coinHit function above.
     */
    @Override
    public void LeftHit(boolean virtualHit,Contact contact) {
        coinHit(virtualHit);
    }

    /*
    // check if this coin is blocked. assuming blocked means that a ice obstacle is to the right.
    public boolean isBlocked(){
        if (GameScreen.searchableMatrix.getMatrix()
                [this.getRow()][this.getCol() + 1] == Constants.ICE_OBSTACLES_MATRIX_CODE){
            return true;
        }
        return false;
    }
    */
}
