package coopworld.game.Tools.ElementsGeneration;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Sprites.Coin;
import coopworld.game.Sprites.IceObstacle;
import coopworld.game.Sprites.LockedCoin;
import coopworld.game.Sprites.LockedVirtualCoin;
import coopworld.game.Sprites.NatureObstacle;
import coopworld.game.Sprites.VirtualPlayerCoin;
import coopworld.game.Tools.AiTools.DataStructures.Node;
import coopworld.game.Tools.AiTools.MatrixBuilder;
import coopworld.game.Tools.AiTools.MatrixCell;
import coopworld.game.Tools.AiTools.SearchableMatrix;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.RandObjectDetails;

/**
 * Created by Eva on 11/1/2016.
 */

/**
 * Coin creator - gener
 */
public class RandomGenerator {
    private GameScreen gameScreen;
    private TiledMap tiledMap;
    private TiledMapTile currentTiledMapTile;
    private SearchableMatrix matrix;
    private HashMap<String,MatrixBuilder.cellType> hashMap;
    private HashMap<MatrixBuilder.cellType, RandObjectDetails> cellTypeToTiledMapTile;
    private TiledMapTileLayer layer;
    private World world;
    private ArrayList<Node> generatedElements;
    private MatrixBuilder matrixBuilder;
    private NatureObstacle natureObstacle = null;
    private IceObstacle iceObstacle = null;


    public RandomGenerator(GameScreen gameScreen, TiledMap tiledMap, SearchableMatrix matrix,
                           MatrixBuilder matrixBuilder) {
        // Get graphics layer.
        this.gameScreen = gameScreen;
        this.layer = (TiledMapTileLayer)tiledMap.getLayers().get(Constants.COLLECTABLE_LAYER);
        this.world = gameScreen.getWorld();
        this.tiledMap = tiledMap;
        this.matrix = matrix;
        this.generatedElements = new ArrayList<Node>();
        this.hashMap = new HashMap<String,MatrixBuilder.cellType>();
        this.matrixBuilder = matrixBuilder;
        this.natureObstacle = null;
        this.iceObstacle = null;
        // Strings are the names of the tile set according to the tmx file.
        hashMap.put("coin", MatrixBuilder.cellType.coins);
        hashMap.put("virtual_coin", MatrixBuilder.cellType.virtualcoins);
        hashMap.put("ice_obstacle", MatrixBuilder.cellType.iceObstacles);
        hashMap.put("regular_obstacle", MatrixBuilder.cellType.natureObstacles);
        hashMap.put("locked_coin", MatrixBuilder.cellType.lockedCoins);
        hashMap.put("locked_virtual_coin", MatrixBuilder.cellType.lockedVirtualCoins);

        cellTypeToTiledMapTile = new HashMap<MatrixBuilder.cellType, RandObjectDetails>();
        TiledMapTileSets tileSets = tiledMap.getTileSets();
        Iterator<TiledMapTileSet> iterator = tileSets.iterator();
        Iterator<TiledMapTile> i;
        while (iterator.hasNext())
        {
            TiledMapTileSet t = iterator.next();
            String tileName = t.getName();
            if(this.hashMap.containsKey(tileName))
            {
                MapProperties mapProperties = t.getProperties();
                Integer imagewidth = mapProperties.get("imagewidth", Integer.class);
                Integer imageheight = mapProperties.get("imageheight", Integer.class);
                Integer tilewidth = mapProperties.get("tilewidth", Integer.class);
                Integer tileheight = mapProperties.get("tileheight", Integer.class);

                int rows = imagewidth / tilewidth;
                int cols = imageheight / tileheight;

                MatrixBuilder.cellType type = this.hashMap.get(tileName);
                i = t.iterator();
                ArrayList<TiledMapTile> tilesArray = new ArrayList<TiledMapTile>();
                while (i.hasNext())
                {
                    TiledMapTile tiledMapTile = i.next();
                    // set hash map with cell type as key and tilemaptile as value
                    tilesArray.add(tiledMapTile);
                }
                Collections.sort(tilesArray, new Comparator<TiledMapTile>() {
                    @Override
                    public int compare(TiledMapTile tiledMapTile, TiledMapTile t1) {
                        return tiledMapTile.getId() < t1.getId() ? -1 : tiledMapTile.getId() == t1.getId() ? 0 : 1;
                    }
                });
                cellTypeToTiledMapTile.put(type, new RandObjectDetails(rows, cols, tilesArray));
            }
        }
    }

    public TiledMapTile getCurrentTiledMapTile() {
        return currentTiledMapTile;
    }

    /**
     * Change tiled map tile to be generate dynamically
     * @param currentTiledMapTile new TileMapTile
     */
    public void setCurrentTiledMapTile(TiledMapTile currentTiledMapTile) {
        this.currentTiledMapTile = currentTiledMapTile;
    }

    /**
     * Change tiled map tile to be generate dynamically
     * @param id new TileMapTile id according to tmx file (tiled map)
     */
    public void setCurrentTiledMapTile(int id) {
        this.currentTiledMapTile = tiledMap.getTileSets().getTile(id);
    }


    public ArrayList<Node> getGeneratedElements() {
        return generatedElements;
    }

    /**
     * Generate a random tile
     */

    public boolean generateRandomTile(MatrixBuilder.cellType cellType, MatrixCell pickedCell, boolean lockedElement) {
        int row = pickedCell.getY();
        int col = pickedCell.getX();

        Node newNode = null;
        switch (cellType) {
            case coins:
                //System.out.println("CREATING COIN IN GENERATOR!++++++++++++++++++++++++++++++++++++++++++++++++++++");
                Coin c = new Coin(world, tiledMap, col, row, 1, gameScreen);
                newNode = this.matrix.getNodesMatrix()[row][col];
                //newCoin.setRandomlyGenerated(true);
                this.matrix.getCoinsList().add(newNode);
                if(!lockedElement){
                    // for random generator use - marks that this is not a valid position
                    // to re-creation now.
                    this.matrixBuilder.removeFromValidPositionsList(c);
                }
                GameScreen.displayedCoins += 1;
                gameScreen.getCoins().add(c);
                break;

            case lockedCoins:
                //System.out.println("CREATING LOCKED COIN IN GENERATOR!++++++++++++++++++++++++++++++++++++++++++++++++++++");
                LockedCoin lc = new LockedCoin(world, tiledMap, col, row, 1, gameScreen);
                newNode = this.matrix.getNodesMatrix()[row][col];
                //newCoin.setRandomlyGenerated(true);
                // todo
                this.matrix.getCoinsList().add(newNode);
                if(!lockedElement){
                    // for random generator use - marks that this is not a valid position
                    // to re-creation now.
                    this.matrixBuilder.removeFromValidPositionsList(lc);
                }
                // todo
                GameScreen.displayedCoins += 1;
                GameScreen.getIceObstacles().put(iceObstacle, lc);
                iceObstacle = null;

                break;

            case virtualcoins:
                //System.out.println("CREATING VIRTUAL COIN IN GENERATOR!++++++++++++++++++++++++++++++++++++++++++++++++++++");
                VirtualPlayerCoin v = new VirtualPlayerCoin(world, tiledMap, col, row, 1, gameScreen);
                newNode = this.matrix.getNodesMatrix()[row][col];
                //newCoin.setRandomlyGenerated(true);
                // if the virtual coin is not blocked - add it to the (free) virtual coins list.
                if(!lockedElement) {
                    this.matrix.getVirtualCoinsList().add(newNode);
                    // for random generator use - marks that this is not a valid position
                    // to re-creation now.
                    this.matrixBuilder.removeFromValidPositionsList(v);
                }
                GameScreen.displayedVirtualCoins += 1;
                gameScreen.getVirtualCoins().add(v);


                break;

            case lockedVirtualCoins:
                //System.out.println("CREATING LOCKED VIRTUAL COIN IN GENERATOR!++++++++++++++++++++++++++++++++++++++++++++++++++++");
                LockedVirtualCoin lv = new LockedVirtualCoin(world, tiledMap, col, row, 1, gameScreen);
                newNode = this.matrix.getNodesMatrix()[row][col];
                //newCoin.setRandomlyGenerated(true);
                // if the virtual coin is not blocked - add it to the (free) virtual coins list.
                if(!lockedElement) {
                    this.matrix.getVirtualCoinsList().add(newNode);
                    // for random generator use - marks that this is not a valid position
                    // to re-creation now.
                    this.matrixBuilder.removeFromValidPositionsList(lv);
                }
                // todo
                GameScreen.displayedVirtualCoins += 1;
                GameScreen.getNatureObstacles().put(natureObstacle, lv);
                natureObstacle = null;

                break;


            case iceObstacles:
                //System.out.println("CREATING ICE OBSTACLE IN GENERATOR!++++++++++++++++++++++++++++++++++++++++++++++++++++");
                // don't generate if there is a coin inside.
                if(this.matrix.getMatrix()[row+1][col+1] == MatrixBuilder.map.get(MatrixBuilder.cellType.coins)){
                    return false;
                }
                IceObstacle i = new IceObstacle(world, tiledMap, col, row, 3, gameScreen);
                //gameScreen.getIceObstacles().add(i);
                iceObstacle = i;

                newNode = this.matrix.getNodesMatrix()[row][col];
                this.matrix.getIceObstaclesList().add(newNode);

                // for random generator use - marks that this is not a valid position
                // to re-creation now.
                this.matrixBuilder.removeFromValidPositionsList(i);
                GameScreen.displayedIceObstacles += 1;
                break;

            case natureObstacles:
                //System.out.println("CREATING NATURE OBSTACLE IN GENERATOR!++++++++++++++++++++++++++++++++++++++++++++++++++++");
                // don't generate if there is a virtual coin inside.
                // todo - check!
                if(this.matrix.getMatrix()[row+1][col+1] == MatrixBuilder.map.get(MatrixBuilder.cellType.virtualcoins)){
                    return false;
                }
                NatureObstacle n = new NatureObstacle(world, tiledMap, col, row, 3, gameScreen);
                //gameScreen.getNatureObstacles().add(n);
                natureObstacle = n;

                //??
                newNode = this.matrix.getNodesMatrix()[row][col];
                this.matrix.getNatureObstaclesList().add(newNode);

                // for random generator use - marks that this is not a valid position
                // to re-creation now.
                this.matrixBuilder.removeFromValidPositionsList(n);
                GameScreen.displayedNatureObstacles += 1;

                break;
            default:
                break;
        }
        // insert the generated element to the integers matrix.
        insertToMatrix(row, col, cellType);
        // add the visual element to the map.
        setGraphics(row, col, cellType);

        // create inner element (coin/ice obstacle) if needed (if an obstacle was created).
        switch (cellType) {
            case iceObstacles:
                generateRandomTile(MatrixBuilder.cellType.lockedCoins,
                        new MatrixCell(pickedCell.getY() + 1, pickedCell.getX() + 1), true);
                // todo
                GameScreen.displayedCoins += 1;

                break;

            case natureObstacles:
                generateRandomTile(MatrixBuilder.cellType.lockedVirtualCoins,
                        new MatrixCell(pickedCell.getY() + 1,
                                pickedCell.getX() + 1), true);
                // add virtual coin to blockedVirtualCoins array in searchable matrix.
                Node n = matrix.getNodesMatrix()[pickedCell.getY() + 1][pickedCell.getX() + 1];
                matrix.addBlockedVirtualCoin(n, matrixBuilder.findClosestGroundSpots(n));
                // todo
                GameScreen.displayedVirtualCoins += 1;

                break;

            default:
                break;
        }
        return true;
    }

    public void setGraphics(int row, int col, MatrixBuilder.cellType cellType){
        /**
         * Set tile into graphics elements layer - x and y are according to libgdx tilemap
         */
        if(cellType == MatrixBuilder.cellType.iceObstacles){
            //System.out.println("AA- ERROR - DIFFERENT SIZES!");
        }
        int x =  col;
        int y = gameScreen.mapHeight - row -1 ;

        /**
         * VERY IMPORTANT : SET ALL TILES WITH TRANSPARENT TILE SO THEYRE PART OF THIS LAYER
         */

        RandObjectDetails randObjDetails = this.cellTypeToTiledMapTile.get(cellType);
        // create an array to hold all the cells that we want to replace.
        ArrayList<TiledMapTileLayer.Cell> cellsToReplace = new ArrayList<TiledMapTileLayer.Cell>();

        // Run over libgdx layer and insert all the cells to replace to an array.
        for(int j = 0; j < randObjDetails.getRows(); j++) {
            for (int i = 0; i < randObjDetails.getColumns(); i++) {
                cellsToReplace.add(this.layer.getCell(x + i, y - j));
            }
        }

        ArrayList<TiledMapTile> tilesToSet = randObjDetails.getTilesArray();
        if(cellsToReplace.size() != tilesToSet.size()){
            //System.out.println("AA- ERROR - DIFFERENT SIZES!");
            return;
        }

        for (int i = 0; i < cellsToReplace.size(); i++) {
            TiledMapTile t = tilesToSet.get(i);
            TiledMapTileLayer.Cell c = cellsToReplace.get(i);

            cellsToReplace.get(i).setTile(tilesToSet.get(i));
        }
    }
    public void insertToMatrix(int row, int col, MatrixBuilder.cellType cellType){
        // todo - instead of if statement insert size to the loop.
        // 1*1 element - coin/ice cube
        if(cellType == MatrixBuilder.cellType.coins
                || cellType == MatrixBuilder.cellType.virtualcoins
                || cellType == MatrixBuilder.cellType.lockedCoins
                || cellType == MatrixBuilder.cellType.lockedVirtualCoins){
            this.matrix.getMatrix()[row][col] = MatrixBuilder.map.get(cellType);
        }
        // 3*3 element - nature obstacle/ice obstacle
        else if(cellType == MatrixBuilder.cellType.iceObstacles
                || cellType == MatrixBuilder.cellType.natureObstacles){
            for (int i = row; i < row + 3; i++) {
                for (int j = col; j < col + 3; j++) {
                    if (!((i == row + 1) && (j == col + 1))) { // don't change the inner cell in matrix
                        this.matrix.getMatrix()[i][j] = MatrixBuilder.map.get(cellType);
                    }
                }
            }
        }
    }
}