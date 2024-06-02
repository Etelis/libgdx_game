package coopworld.game.Tools;

import com.badlogic.gdx.maps.tiled.TiledMapTile;

import java.util.ArrayList;

/**
 * Created by User on 18/02/2017.
 */
public class RandObjectDetails {
    int rows;
    int columns;
    ArrayList<TiledMapTile> tilesArray;

    public RandObjectDetails(int rows, int columns, ArrayList<TiledMapTile> tilesArray) {
        this.rows = rows;
        this.columns = columns;
        this.tilesArray = tilesArray;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public ArrayList<TiledMapTile> getTilesArray() {
        return tilesArray;
    }
}
