package coopworld.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import coopworld.game.Screens.GameScreens.GameScreen;

/**
 * Created by Eva on 11/2/2016.
 */
public abstract class GeneratedTileObject extends InteractiveTileObject{
    /***
     * Used when we generate the object dynamically - with no Rectangle. width & height - in tiles.
     * @param world
     * @param map
     * @param col
     * @param row
     */
    public GeneratedTileObject(World world, TiledMap map,int col, int row, int size, GameScreen gameScreen)
    {
        super(world,map, col, row, size, gameScreen);
    }


    /**
     * Used in regular construction of the object in World builder - according to Rectangle piece.
     * @param world
     * @param map
     * @param bounds
     */
    public GeneratedTileObject(World world, TiledMap map, Rectangle bounds, int size, GameScreen gameScreen)
    {
        super(world, map, bounds, size, gameScreen);
    }
}

