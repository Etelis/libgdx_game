package coopworld.game.Tools.AiTools;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Sprites.Coin;
import coopworld.game.Sprites.IceObstacle;
import coopworld.game.Sprites.InteractiveTileObject;
import coopworld.game.Sprites.LockedCoin;
import coopworld.game.Sprites.LockedVirtualCoin;
import coopworld.game.Sprites.NatureObstacle;
import coopworld.game.Sprites.VirtualPlayerCoin;
import coopworld.game.Tools.AiTools.DataStructures.Node;

import static coopworld.game.Tools.Constants.COINS_MATRIX_CODE;
import static coopworld.game.Tools.Constants.EMPTY_MATRIX_CODE;
import static coopworld.game.Tools.Constants.GROUND_MATRIX_CODE;
import static coopworld.game.Tools.Constants.ICE_OBSTACLES_MATRIX_CODE;
import static coopworld.game.Tools.Constants.NATURE_OBSTACLES_MATRIX_CODE;
import static coopworld.game.Tools.Constants.REALPLAYER_MATRIX_CODE;
import static coopworld.game.Tools.Constants.VIRTPLAYER_MATRIX_CODE;
import static coopworld.game.Tools.Constants.VIRTUAL_COINS_MATRIX_CODE;

/**
 * Created by Eva on 10/22/2016.
 */
public class MatrixBuilder {
    public enum cellType{ empty,ground,virtualcoins, iceObstacles,natureObstacles,coins,realPlayer,virtPlayer, lockedCoins, lockedVirtualCoins}; // <---- Keep same order as in the map
    public static HashMap<cellType, Integer> map;
    private int[][] matrix;
    private ArrayList<Node> virtualCoinCells;
    private ArrayList<Node> coinCells;
    private ArrayList<Node> iceObstacleCells;
    private ArrayList<Node> natureObstacleCells;

    private HashMap<Node,ArrayList<Node>> blockedVirtualCoinCells;

    // for random generation use.

    private ArrayList<MatrixCell> emptyVirtualCoinsPositions;
    private ArrayList<MatrixCell> emptyCoinsPositions;
    private ArrayList<MatrixCell> emptyIceObstaclesPositions;
    private ArrayList<MatrixCell> emptyNatureObstaclesPositions;

    private ArrayList<MatrixCell> virtualCoinsPositions;
    private ArrayList<MatrixCell> coinsPositions;
    private HashMap<MatrixCell, MatrixCell> iceObstaclesPositions;
    private HashMap<MatrixCell, MatrixCell> natureObstaclesPositions;

    private int rows;
    private int cols;
    private int tileSize;

    private GameScreen gameScreen;

    /**
     * SearchableMatrix builder - knows how to build a numerical matrix out of the tiled map tmx file
     * @param tmxMapLoader
     */
    public MatrixBuilder(String mapPath,
                         TmxMapLoader tmxMapLoader, GameScreen gameScreen)
    {
        int i,j;
        this.gameScreen = gameScreen;
        this.virtualCoinCells = new ArrayList<Node>();
        this.coinCells = new ArrayList<Node>();
        this.iceObstacleCells = new ArrayList<Node>();
        this.natureObstacleCells = new ArrayList<Node>();

        this.virtualCoinsPositions = new ArrayList<MatrixCell>();
        this.coinsPositions = new ArrayList<MatrixCell>();
        this.iceObstaclesPositions = new HashMap<MatrixCell, MatrixCell>();
        this.natureObstaclesPositions = new HashMap<MatrixCell, MatrixCell>();

        this.blockedVirtualCoinCells = new HashMap<Node, ArrayList<Node>>();

        // IMPORTANT - the arrays are empty because we assume that in the start of the level -
        // all the elements are visible.
        this.emptyVirtualCoinsPositions = new ArrayList<MatrixCell>();
        this.emptyCoinsPositions = new ArrayList<MatrixCell>();
        this.emptyIceObstaclesPositions = new ArrayList<MatrixCell>();
        this.emptyNatureObstaclesPositions = new ArrayList<MatrixCell>();

        // Add any new type of wanted cells in matrix
        this.map = new HashMap<cellType, Integer>(); // <--- Whenever you had a new type to the map add it to line with the next <-- mark

        EMPTY_MATRIX_CODE = 0;
        this.map.put(cellType.empty, new Integer(EMPTY_MATRIX_CODE));

        GROUND_MATRIX_CODE = 1;
        this.map.put(cellType.ground, new Integer(GROUND_MATRIX_CODE));

        VIRTUAL_COINS_MATRIX_CODE = 2;
        this.map.put(cellType.virtualcoins, new Integer(VIRTUAL_COINS_MATRIX_CODE));
        this.map.put(cellType.lockedVirtualCoins, new Integer(VIRTUAL_COINS_MATRIX_CODE));

        ICE_OBSTACLES_MATRIX_CODE = 3;
        this.map.put(cellType.iceObstacles, new Integer(ICE_OBSTACLES_MATRIX_CODE));

        NATURE_OBSTACLES_MATRIX_CODE = 4;
        this.map.put(cellType.natureObstacles, new Integer(NATURE_OBSTACLES_MATRIX_CODE));

        COINS_MATRIX_CODE = 5;
        this.map.put(cellType.coins, new Integer(COINS_MATRIX_CODE));
        this.map.put(cellType.lockedCoins, new Integer(COINS_MATRIX_CODE));

        REALPLAYER_MATRIX_CODE = 8;
        this.map.put(cellType.realPlayer, new Integer(REALPLAYER_MATRIX_CODE));

        VIRTPLAYER_MATRIX_CODE = 9;
        this.map.put(cellType.virtPlayer, new Integer(VIRTPLAYER_MATRIX_CODE));

        // parse tiled map
        try {
            FileHandle tmxFile = tmxMapLoader.resolve(mapPath);
            XmlReader xml = new XmlReader();
            XmlReader.Element root = xml.parse(tmxFile);

            // Dimensions of the map are properties of the root
            cols = root.getIntAttribute("width"); // 30
            rows = root.getIntAttribute("height"); // 18
            if(root.getIntAttribute("tilewidth") != root.getIntAttribute("tileheight"))
            {
                System.out.println("Cannot build matrix according to current map!");
            }
            else
                tileSize = root.getIntAttribute("tilewidth");

            // initial matrix - only empty
            this.matrix = new int[rows][cols];
            for (i = 0; i < rows; i++) {
                for (j = 0; j < cols; j++) {
                    matrix[i][j] = this.map.get(cellType.empty);
                }
            }

            Array<XmlReader.Element> objectgroupElements = root.getChildrenByName("objectgroup");
            for (XmlReader.Element groundObject : objectgroupElements) {
                if (groundObject.getName().equals("objectgroup")) {
                    String name = groundObject.getAttribute("name", null);
                    if ((name.equals(cellType.ground.name()))
                            || (name.equals(cellType.virtualcoins.name()))
                            || (name.equals(cellType.natureObstacles.name()))
                            || (name.equals(cellType.iceObstacles.name()))
                            || (name.equals(cellType.coins.name()))
                            || (name.equals(cellType.lockedCoins.name()))
                            || (name.equals(cellType.lockedVirtualCoins.name())))

                    {  //<----- Add name of new layer you would like to add to matrix
                        cellType currentCellType = cellType.valueOf(name); //<------ WARNING! The layer name and the name in enum need to be the same!

                        for (XmlReader.Element objectElement : groundObject.getChildrenByName("object")) {
                            // fetch all rectangle information to set up the search matrix
                            int xLeftCorner = Math.round(objectElement.getFloatAttribute("x", 0) / tileSize);
                            int yLeftCorner = Math.round(objectElement.getFloatAttribute("y", 0) / tileSize);
                            int width = Math.round(objectElement.getFloatAttribute("width", 0) / tileSize);
                            int height = Math.round(objectElement.getFloatAttribute("height", 0) / tileSize);

                            if((currentCellType.equals(cellType.virtualcoins))
                            || (currentCellType.equals(cellType.lockedVirtualCoins)))
                            {
                                if(!(objectElement.get("visible","true").equals("false")))
                                {
                                    // todo - consider.
                                    GameScreen.displayedVirtualCoins += 1;
                                    // Store values in container. split by type (locked and unlocked
                                    // is done later on.
                                    this.virtualCoinCells.add(new Node(yLeftCorner,xLeftCorner));
                                }
                            }

                            if(currentCellType.equals(cellType.lockedCoins))
                            {
                                if(!(objectElement.get("visible","true").equals("false")))
                                {
                                    GameScreen.displayedCoins += 1;
                                }
                            }

                            // todo - displayed!
                            if(currentCellType.equals(cellType.coins))
                            {
                                if(!(objectElement.get("visible","true").equals("false")))
                                {
                                    GameScreen.displayedCoins += 1;
                                    // Store values in container
                                    this.coinCells.add(new Node(yLeftCorner,xLeftCorner));
                                }
                            }

                            // CHEN
                            else if(currentCellType.equals(cellType.iceObstacles))
                            {
                                if(!(objectElement.get("visible","true").equals("false")))
                                {
                                    GameScreen.displayedIceObstacles += 1;
                                    // Store values in container
                                    this.iceObstacleCells.add(new Node(yLeftCorner,xLeftCorner));
                                }
                            }

                            // CHEN
                            else if(currentCellType.equals(cellType.natureObstacles))
                            {
                                if(!(objectElement.get("visible","true").equals("false")))
                                {
                                    GameScreen.displayedNatureObstacles += 1;
                                    // Store values in container
                                    this.natureObstacleCells.add(new Node(yLeftCorner,xLeftCorner));
                                }
                            }
                            // insert element to matrix. no need to ignore inner cell in case of
                            // obstacles because inserting obstacles is occues before inserting 1*1
                            // elements (according to the tmx file - layers order).
                            for (int a = yLeftCorner; a < yLeftCorner + height; a++) {
                                for (int b = xLeftCorner; b < width + xLeftCorner; b++) {
                                  //  Gdx.app.log("SearchableMatrix change for:", Integer.toString(a) + "," + Integer.toString(b));
                                    matrix[a][b] = this.map.get(currentCellType); // will mark the cell with corresponding integer
                                }
                            }
                        }
                    }
                }
            }

            // split the virtual coins list into 2 list : blocked coins and regular coins
            int nodex;
            int nodey;
            MatrixCell c;
            Node n;
            Iterator<Node> iter = this.virtualCoinCells.iterator();
            while (iter.hasNext()) {
                n = iter.next();
                c = n.getMatrixCell();
                nodex = c.getX();
                nodey = c.getY();
                if(this.map.get(cellType.natureObstacles).equals(matrix[nodey+1][nodex]))
                {
                    // blocked coin - find closest spot to stand close to obstacle
                    this.blockedVirtualCoinCells.put(n, findClosestGroundSpots(n));
                    iter.remove();
                }
            }

            for(Node freeVirtualCoin : this.virtualCoinCells)
            {
                this.virtualCoinsPositions.add(freeVirtualCoin.getMatrixCell());
            }

            for(Node coin : this.coinCells)
            {
                this.coinsPositions.add(coin.getMatrixCell());
            }
            for(Node iceObstacle : this.iceObstacleCells)
            {
                // assume that into an ice obstacle there is a locked coin.
                this.iceObstaclesPositions.put(iceObstacle.getMatrixCell(),
                        new MatrixCell(iceObstacle.getMatrixCell().getX() + 1,
                                iceObstacle.getMatrixCell().getY() + 1));
            }
            for(Node natureObstacle : this.natureObstacleCells)
            {
                // assume that into a nature obstacle there is a locked ice cube.
                this.natureObstaclesPositions.put(natureObstacle.getMatrixCell(),
                        new MatrixCell(natureObstacle.getMatrixCell().getX() + 1,
                                natureObstacle.getMatrixCell().getY() + 1));
            }

        } catch (Exception e) {
            throw new GdxRuntimeException("Couldn't parse tilemap '" + mapPath + "'", e);
        }

        /* for debug use */
        for (i = 0; i < rows; i++) {
            for (j = 0; j < cols; j++) {
               System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
    }

    public ArrayList<Node> findClosestGroundSpots(Node blockedCoin)
    {
        int rowindex= blockedCoin.getMatrixCell().getY();
        int col= blockedCoin.getMatrixCell().getX();

        while (rowindex <= rows - 1)
        {
            if(this.map.get(cellType.ground).equals(matrix[rowindex][col]))
            {
                rowindex--; // want to stand on the floor (above, not "into")! (BTW, it does come redundant operation here...)
                break;
            }
            rowindex++; // it is not the floor yet, so try one step down.
        }
        // find spot to stand on right/left of the obstacle
        ArrayList<Node> possibleStandSpots = new ArrayList<Node>();
        // check out of bounds
        if(col - 3 >= 0)
        {
            possibleStandSpots.add(new Node(rowindex,col-3));
        }
        if(col + 3 <= cols - 1) // bug fixed here (virtual player got stuck on the obstacle.) "-1" was added to fix this.
        {
            possibleStandSpots.add(new Node(rowindex,col+3));
        }

        return possibleStandSpots;
    }

    /**
     * The matrix will always be initialized since its done in the class' constructor.
     * @return a numerical matrix according to hashmap
     */
    public int[][] build(){
        return this.matrix;
    }

    public ArrayList<Node> computeVirtualCoinCells(){
        return this.virtualCoinCells;
    }

    public HashMap<cellType, Integer> getMatrixBuilderHashMap()
    {
        return this.map;
    }

    public SearchableMatrix buildSearchableMatrix() {
        return new SearchableMatrix(this.matrix, this.rows, this.cols, this.virtualCoinCells,
                this.blockedVirtualCoinCells, this.coinCells, this.iceObstacleCells,
                this.natureObstacleCells, this.gameScreen);
    }

    public ArrayList<Node> getCoinCells() {
        return coinCells;
    }

    public ArrayList<Node> getIceObstacleCells() {
        return iceObstacleCells;
    }

    public ArrayList<Node> getNatureObstacleCells() {
        return natureObstacleCells;
    }

    public HashMap<MatrixCell, MatrixCell> getIceObstaclesPositions() {
        return this.iceObstaclesPositions;
    }

    public HashMap<MatrixCell, MatrixCell> getNatureObstaclesPositions() {
        return this.natureObstaclesPositions;
    }

    public ArrayList<MatrixCell> getVirtualCoinsPositions() {
        return this.virtualCoinsPositions;
    }

    public ArrayList<MatrixCell> getCoinsPositions() {
        return coinsPositions;
    }

    public void addToValidPositionsList(InteractiveTileObject i){
        MatrixCell cellToAdd = new MatrixCell(i.getRow(), i.getCol());
        // avoid creation of regular coins where a special coin appeared.
        if(i instanceof LockedCoin){
            return;
        }
        // the same goes for virtual currencies.
        if(i instanceof LockedVirtualCoin){
            return;
        }
        if(i instanceof Coin){
            this.emptyCoinsPositions.add(cellToAdd);
        }
        else if(i instanceof VirtualPlayerCoin){
            this.emptyVirtualCoinsPositions.add(cellToAdd);
        }
        else if(i instanceof IceObstacle){
            this.emptyIceObstaclesPositions.add(cellToAdd);
        }
        else if(i instanceof NatureObstacle){
            this.emptyNatureObstaclesPositions.add(cellToAdd);
        }
        else{
            throw new IllegalArgumentException("Element type is illegal.");
        }
    }

    public void removeFromValidPositionsList(InteractiveTileObject i){
        MatrixCell cellToRemove = new MatrixCell(i.getRow(), i.getCol());
        if(i instanceof Coin){
            this.emptyCoinsPositions.remove(cellToRemove);
        }
        else if(i instanceof VirtualPlayerCoin){
            this.emptyVirtualCoinsPositions.remove(cellToRemove);
        }
        else if(i instanceof IceObstacle){
            this.emptyIceObstaclesPositions.remove(cellToRemove);
        }
        else if(i instanceof NatureObstacle){
            this.emptyNatureObstaclesPositions.remove(cellToRemove);
        }
        else{
            throw new IllegalArgumentException("Element type is illegal.");
        }
    }

    public static HashMap<cellType, Integer> getMap() {
        return map;
    }

    public ArrayList<MatrixCell> getEmptyVirtualCoinsPositions() {
        return emptyVirtualCoinsPositions;
    }

    public ArrayList<MatrixCell> getEmptyCoinsPositions() {
        return emptyCoinsPositions;
    }

    public ArrayList<MatrixCell> getEmptyIceObstaclesPositions() {
        return emptyIceObstaclesPositions;
    }

    public ArrayList<MatrixCell> getEmptyNatureObstaclesPositions() {
        return emptyNatureObstaclesPositions;
    }
}
