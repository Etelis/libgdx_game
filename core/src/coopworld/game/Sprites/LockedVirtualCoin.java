package coopworld.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import coopworld.game.Screens.GameScreens.GameScreen;

import static coopworld.game.Tools.Constants.LOCKED_ELEMENT_VALUE;

/**
 * Created by Chen on 04/07/2017.
 */

public class LockedVirtualCoin extends VirtualPlayerCoin {

    public LockedVirtualCoin(World world, TiledMap map, Rectangle bounds, int size, GameScreen gameScreen) {
        super(world, map, bounds, size, gameScreen);
        initVirtualLockedCoin();
    }

    public LockedVirtualCoin(World world, TiledMap map, int col, int row, int size, GameScreen gameScreen) {
        super(world, map, col, row, size, gameScreen);
        initVirtualLockedCoin();
    }

    public void initVirtualLockedCoin(){
        value = LOCKED_ELEMENT_VALUE;
        elementName = "locked_ice_cube";
    }

    @Override
    public void remove(){
        // todo - Pay attention! only if it was blocked!
        if (!GameScreen.searchableMatrix.removeBlockedVirtualCoin(this)) {
            Gdx.app.log("Error!", "Virtual coin couldn't be removed from the blocked virtual" +
                    "coins list - (maybe it was not blocked at all...)");
        }
        super.remove();
    }
}
