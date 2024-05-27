package coopworld.game.Tools.TiledAnimations;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;

import java.util.Iterator;

import coopworld.game.Logs.Enums;
import coopworld.game.Tools.Constants;

/**
 * Created by User on 02/03/2017.
 */
public class ObstacleAnimation extends TiledAnimation{
    int animationId, originCellsStart;
    String animationName;

    public ObstacleAnimation(TiledMap tiledMap, float animationSpeed,
                             Enums.ObstacleType type) {
        super(tiledMap, animationSpeed);

        // ICE
        if(type == Enums.ObstacleType.Ice) {
            originCellsStart = 1088;
            animationId = 1139;
            animationName = "ice_obstacle_animation";
        }
        // NATURE - NOT IN USE.
        else {
            originCellsStart = 550;
            animationId = 1115;
            animationName = "NatureObstacleAnimation";
        }
    }

    public void prepareAnimation(){
        /*
        // get the tileset named "coinsAnimation" that contains the animation frames.
        TiledMapTileSet tileset = tiledMap.getTileSets().getTileSet(tileSetString);
        // hold all the animation frames in coinsTiles.
        for(TiledMapTile tile : tileset){
            tilesToShow.add(tile);
        }
        */

        Iterator<TiledMapTile> tiles = tiledMap.getTileSets().getTileSet(animationName).iterator();
        int i = 0;
        while (tiles.hasNext()) {
            TiledMapTile tile = tiles.next();
            // don't take the first - it is the original tile.
            if(i != 0){
                tilesToShow.add((StaticTiledMapTile)tile);
            }
            i++;
        }
    }

    public void getCellsToReplace(){
        TiledMapTileLayer graphicsElementsLayer = (TiledMapTileLayer)tiledMap.getLayers().
                get(Constants.COLLECTABLE_LAYER);
        cellsToReplace.clear();

        // bigger than 0.5f (the animation rate) in order to start the animation
        // after the button press without waiting.
        // elapsedSinceAnimation = animationSpeed + 0.1f;

        /* Now we want to get a reference to every single cell (Tile instance) in the map
        that refers to a coin cell. Loop through the entire world, checking if a cell's tile
        contains the AnimatedCoin property.  If it does, add to the coinsCells array.
        Note, this only pays attention to the very first layer of tiles.
        If you want to support animation across multiple layers you will have to loop through each
        */
        TiledMapTileLayer.Cell cell;
        for(int y = 0; y < graphicsElementsLayer.getHeight(); y++){
            for(int x = 0; x < graphicsElementsLayer.getWidth(); x++){
                cell = graphicsElementsLayer.getCell(x, y);
                if(cell != null) {
                    if (cell.getTile() != null){
                        int id = cell.getTile().getId();
                        // check if this cell is the coin via its unique id in the tmx file.
                        // todo - hardcoded now! change to searchable matrix values.
                        if ((id == originCellsStart) || (id == animationId)) {
                            cellsPositions.add(new Vector2(y, x));
                            cellsToReplace.add(cell);
                            // set to null if needed.
                        }
                        // set to null if needed.
                        if ((id >= originCellsStart - 6) && (id <= originCellsStart + 2)){
                            cell.setTile(null);
                        }
                    }
                }
            }
        }
    }

    // back to initial state.
    public void resetCells(){
        // change cells to first animation frame.
        for (TiledMapTileLayer.Cell cell : cellsToReplace) {
            // cell was removed! (Pay)
            if(cell.getTile() == null){
                continue;
            }
            TiledMapTile newTile = tiledMap.getTileSets().getTileSet(animationName).getTile(animationId);
            cell.setTile(newTile);
        }
    }
}
