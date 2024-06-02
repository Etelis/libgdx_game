package coopworld.game.Tools.TiledAnimations;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * Created by User on 02/03/2017.
 */
public abstract class TiledAnimation {
    protected float elapsedSinceAnimation;
    protected int currentAnimationFrame;
    protected ArrayList<TiledMapTileLayer.Cell> cellsToReplace;
    protected Array<StaticTiledMapTile> tilesToShow;
    protected Array<Vector2> cellsPositions;
    protected TiledMapTile newTile;
    protected float animationSpeed;
    protected TiledMap tiledMap;

    public TiledAnimation(TiledMap tiledMap,
                          float animationSpeed){
        this.tiledMap = tiledMap;
        this.cellsToReplace = new ArrayList<TiledMapTileLayer.Cell>();
        this.tilesToShow = new Array<StaticTiledMapTile>();
        this.cellsPositions = new Array<Vector2>();
        this.elapsedSinceAnimation = 0.0f;
        this.currentAnimationFrame = 0;
        this.animationSpeed = animationSpeed;
    }

    public abstract void prepareAnimation();

    public void animate(){
        for (TiledMapTileLayer.Cell cell : cellsToReplace) {
            // replace current tile (of this cell) with the new frame from coinTiles list.
            //newTile = tilesToShow.get(currentAnimationFrame);
            //cell.setTile(newTile);
            cell.setTile(new AnimatedTiledMapTile(0.3f, tilesToShow));
            //new AnimatedTiledMapTile(0.3f, tilesToShow);
        }
    }

    public abstract void getCellsToReplace();

    // back to initial state.
    public abstract void resetCells();
}
