package coopworld.game.Tools.AiTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Sprites.Coin;
import coopworld.game.Sprites.IceObstacle;
import coopworld.game.Sprites.LockedVirtualCoin;
import coopworld.game.Sprites.NatureObstacle;
import coopworld.game.Sprites.VirtualPlayerCoin;
import coopworld.game.Tools.AiTools.DataStructures.Node;

import static coopworld.game.Tools.Constants.REALPLAYER_MATRIX_CODE;
import static coopworld.game.Tools.Constants.VIRTPLAYER_MATRIX_CODE;

/**
 * Created by Eva on 10/23/2016.
 */


public class SearchableMatrix {
    private Node[][] nodesMatrix;
    private int[][] matrix;
    private HashMap<MatrixCell, MatrixBuilder.cellType> previousPositions;
    private MatrixCell realPlayerCell;
    private MatrixCell virtPlayerCell;
    private ArrayList<Node> virtualCoinsList;
    private ArrayList<Node> coinsList;
    private ArrayList<Node> iceObstaclesList;
    private ArrayList<Node> natureObstacleList;

    private HashMap<Node, ArrayList<Node>> blockedVirtualCoinsList;
    private int rows;
    private int cols;
    private int defaultWalkCost = 3;
    private int defaultJumpCost = 4;
    private int defaultWalkCostOnGround = 1;
    private int defaultJumpCostOnGround = 2;
    private ArrayList<MatrixBuilder.cellType> forbiddenCellsType;
    private ArrayList<Integer> forbiddenCellsInteger;
    private HashMap<MatrixBuilder.cellType, Integer> map;
    private GameScreen gameScreen;

    ArrayList<Integer> upCellsToCheck, downCellsToCheck, leftCellsToCheck, rightCellsToCheck;
    ArrayList<Node> nextOptions;

    public SearchableMatrix(int[][] matrix, int rows, int cols, ArrayList<Node> virtualCoinsList
            , HashMap<Node, ArrayList<Node>> blockedVirtualCoinsList, ArrayList<Node> coinsList
            , ArrayList<Node> iceObstaclesList, ArrayList<Node> natureObstacleList, GameScreen gameScreen) {
        this.matrix = matrix;
        this.virtualCoinsList = virtualCoinsList;
        this.iceObstaclesList = iceObstaclesList;
        this.natureObstacleList = natureObstacleList;
        this.coinsList = coinsList;
        this.blockedVirtualCoinsList = blockedVirtualCoinsList;
        this.rows = rows;
        this.cols = cols;
        nodesMatrix = new Node[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                nodesMatrix[i][j] = new Node(i, j);
            }
        }

        // Set start cells as null
        this.previousPositions = new HashMap<MatrixCell, MatrixBuilder.cellType>();
        this.gameScreen = gameScreen;

        this.upCellsToCheck = new ArrayList<Integer>();
        this.downCellsToCheck = new ArrayList<Integer>();
        this.leftCellsToCheck = new ArrayList<Integer>();
        this.rightCellsToCheck = new ArrayList<Integer>();
        this.nextOptions = new ArrayList<Node>();

        setForbiddenCells();
    }

    private void setForbiddenCells() {

        /**
         * Set forbidden cells type for path searching
         */
        this.forbiddenCellsType = new ArrayList<MatrixBuilder.cellType>();
        forbiddenCellsType.add(MatrixBuilder.cellType.ground);
        forbiddenCellsType.add(MatrixBuilder.cellType.natureObstacles);

        this.map = MatrixBuilder.map;

        /**
         * Get forbidden cells integer according to forbidden cells types
         */
        this.forbiddenCellsInteger = new ArrayList<Integer>();
        for (MatrixBuilder.cellType c : this.forbiddenCellsType) {
            this.forbiddenCellsInteger.add(map.get(c));
        }
    }

    public void ResetMazeDistance() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                nodesMatrix[i][j].setDistance(9999999);
            }
        }
    }

    public void addBlockedVirtualCoin(Node n, ArrayList<Node> validSpots) {
        blockedVirtualCoinsList.put(n, validSpots);
    }

    public boolean removeBlockedVirtualCoin(LockedVirtualCoin lvc) {
        // the previous method version is the noted code below.
        // after some changes (add lockedVirtualCoin and changing arrays to hash map) -
        // it stopped working.
        // the new version works, but I could not understand why the old one does not.
        // (the nodes are probably not identical, but why?)
        Node toRemove = this.nodesMatrix[lvc.getRow()][lvc.getCol()];
        for (Map.Entry<Node, ArrayList<Node>> entry : blockedVirtualCoinsList.entrySet()) {
            Node n = entry.getKey();
            if(n.getMatrixCell().equals(toRemove.getMatrixCell())){
                blockedVirtualCoinsList.remove(n);
                return true;
            }
        }
        return false;
        /*
        MatrixCell mx = toRemove.getMatrixCell();
        if (this.blockedVirtualCoinsList.remove(toRemove) == null){
            return false;
        }
        return true;
        */
    }

    public HashMap<Node, ArrayList<Node>> getBlockedVirtualCoinsList() {
        return blockedVirtualCoinsList;
    }

    public ArrayList<Node> getCoinsList() {
        return coinsList;
    }

    public ArrayList<Node> getVirtualCoinsList() {
        return virtualCoinsList;
    }

    public ArrayList<Node> getIceObstaclesList() {
        return iceObstaclesList;
    }

    public ArrayList<Node> getNatureObstaclesList()
    {
        return natureObstacleList;
    }

    /**
     * Could be used for further implementation with weighted edges.
     */
    public int cost(Node current, Node next)
    {
        MatrixCell currentCell = current.getMatrixCell();
        MatrixCell nextCell = next.getMatrixCell();

        int currentY = currentCell.getY();

        if (currentY == nextCell.getY()) // same row
        {
            // player's feet are on ground
            if (MatrixBuilder.map.get(MatrixBuilder.cellType.ground).equals(matrix[currentY+1][currentCell.getX()]))
            {
                return this.defaultWalkCostOnGround;
            }
            return this.defaultWalkCost;
        }
        else // same col
        {
            // player's feet are on ground
            if (MatrixBuilder.map.get(MatrixBuilder.cellType.ground).equals(matrix[currentY+1][currentCell.getX()]))
            {
                return this.defaultJumpCostOnGround;
            }
            return this.defaultJumpCost;
        }

    }

    private boolean checkCardinalOption(ArrayList<Integer> cellsValue)
    {
        boolean validCardinalOption = true;
        for (Integer i : this.forbiddenCellsInteger)
        {
            for (Integer cell_int : cellsValue)
            {
                if (cell_int.equals(i))
                {
                    validCardinalOption = false;
                    return validCardinalOption;
                }
            }
        }
        return validCardinalOption;
    }

    /**
     * Next options are only right left up down so the path is always a straight path or must run "orthogonally".
     * There is no diagonal walking.
     * @param n current node
     * @return array list of all next options
     */
    public ArrayList<Node> getAllNextStepOptions(Node n)
    {
        int row = n.getMatrixCell().getY();
        int col = n.getMatrixCell().getX();


        nextOptions.clear();


        // Get all four directions north, east, south,west

        //TODO change back to hash map values
        //check up cardinal option
        /*
        101
        101
        0*0
         */
        // check if up is not out of bounds
        if((row >= 2) && (col < GameScreen.mapWidth - 1)) {
            upCellsToCheck.clear();
            upCellsToCheck.add(matrix[row - 1][col]);
            upCellsToCheck.add(matrix[row-2][col]);
            upCellsToCheck.add(matrix[row - 1][col - 1]);
            upCellsToCheck.add(matrix[row - 2][col - 1]);
            upCellsToCheck.add(matrix[row - 1][col + 1]);
            upCellsToCheck.add(matrix[row - 1][col + 1]);

            boolean upOK = checkCardinalOption(upCellsToCheck);
            if(upOK)
            {
                nextOptions.add(this.getNodesMatrix()[row-1][col]);
            }
        }

        if(row < GameScreen.mapHeight - 1) {
            downCellsToCheck.clear();
            downCellsToCheck.add(matrix[row + 1][col]);
            boolean downOK = checkCardinalOption(downCellsToCheck);
            // check if the player if not in the edge of a brick and trying to go down,
            // because then his b2body will not allow it and the player will be stuck in the corner.
            if(downOK){
                if(matrix[n.getMatrixCell().getY() + 1][n.getMatrixCell().getX() - 1] == MatrixBuilder.map.get(MatrixBuilder.cellType.ground)){
                    downOK = false;
                }
            }
            if (downOK) {
                nextOptions.add(this.getNodesMatrix()[row + 1][col]);
            }
        }

        //left
        // 10
        // 0*

        // 100
        // 00*

        //10*

        //0*
        //10
        if((row > 0) && (row < GameScreen.mapHeight - 1) && (col >= 2)) {
            leftCellsToCheck.clear();
            leftCellsToCheck.add(matrix[row][col - 1]);
            leftCellsToCheck.add(matrix[row][col - 2]);
            leftCellsToCheck.add(matrix[row - 1][col - 1]);
            leftCellsToCheck.add(matrix[row - 1][col - 2]);
            // check those options only if not standing on ground
            if (!MatrixBuilder.map.get(MatrixBuilder.cellType.ground).equals(matrix[row + 1][col]))
            {
                leftCellsToCheck.add(matrix[row + 1][col - 1]);
                leftCellsToCheck.add(matrix[row + 1][col - 2]);
            }



            boolean leftOK = checkCardinalOption(leftCellsToCheck);
            if (leftOK) {
                nextOptions.add(this.getNodesMatrix()[row][col - 1]);
            }
        }


        //right
        // 01
        // *0

        // 001
        // *00

        // *01

        //*0
        //01
        if((row > 0) && (row < GameScreen.mapHeight - 1) && (col < GameScreen.mapWidth - 2)) {
            rightCellsToCheck.clear();
            rightCellsToCheck.add(matrix[row][col + 1]);
            rightCellsToCheck.add(matrix[row - 1][col + 1]);
            rightCellsToCheck.add(matrix[row][col + 2]);
            rightCellsToCheck.add(matrix[row - 1][col + 2]);
            // check those options only if not standing on ground
            if (!MatrixBuilder.map.get(MatrixBuilder.cellType.ground).equals(matrix[row + 1][col]))
            {
                rightCellsToCheck.add(matrix[row + 1][col + 1]);
                rightCellsToCheck.add(matrix[row + 1][col + 2]);
            }
            //rightCellsToCheck.add(matrix[row + 1][col + 1]);


            boolean rightOK = checkCardinalOption(rightCellsToCheck);
            if (rightOK) {
                nextOptions.add(this.getNodesMatrix()[row][col + 1]);
            }
        }

        return nextOptions;
    }

    public Node[][] getNodesMatrix()
    {
        return this.nodesMatrix;
    }

   /* public Node getClosestCoinNode(int currentRow, int currentCol)
    {

        if (this.virtualCoinsList.size() > 0) {
            Node closest = this.virtualCoinsList.get(0);
            int minDistance = closest.computeManhattanDistance(currentRow, currentCol);

            for (Node n : this.virtualCoinsList.subList(1, this.virtualCoinsList.size())) {
                int dist = n.computeManhattanDistance(currentRow, currentCol);
                if (dist < minDistance) {
                    closest = n;
                    minDistance=dist;
                }
            }
            return closest;
        }
        else
            return null;

    }*/

    private MatrixBuilder.cellType getKeyFromValue(int value) {
        for (MatrixBuilder.cellType key : MatrixBuilder.map.keySet()) {
            if (MatrixBuilder.map.get(key).equals(value)) {
                return key;
            }
        }
        return null;
    }

    private void setPreviousPositions(int col,int row)
    {
        //set previous positions
        int currentInt = this.matrix[row][col];
        MatrixBuilder.cellType previousCellType = getKeyFromValue(currentInt);
        this.previousPositions.put(this.nodesMatrix[row][col].getMatrixCell(),previousCellType);
        //System.out.println("size after setting previous positions = " + previousPositions.size());
    }
    public void setRealPlayerCell(int col,int row)
    {
        /*System.out.println();
        printMatrix();*/
        setPreviousPositions(col,row);

        //update cell
        this.matrix[row][col] = MatrixBuilder.map.get(MatrixBuilder.cellType.realPlayer);
    }

    public void setVirtPlayerCell(int col,int row)
    {
        setPreviousPositions(col,row);

        //update cell
        this.matrix[row][col] = MatrixBuilder.map.get(MatrixBuilder.cellType.virtPlayer);
    }

    public void resetPreviousCells()
    {
        /*System.out.println("Resetting previous cells");
        System.out.println("Previous cells:");
        System.out.println("size = " + previousPositions.size());*/

        for(Map.Entry<MatrixCell,MatrixBuilder.cellType> entry : previousPositions.entrySet())
        {
            MatrixBuilder.cellType value = entry.getValue();
            MatrixCell previousCell = entry.getKey();

           // System.out.println(previousCell.getY() + "," + previousCell.getX() );
           // System.out.println("integer is" + coopworld.game.Tools.AiTools.MatrixBuilder.map.get(value));

            this.matrix[previousCell.getY()][previousCell.getX()] =
                    MatrixBuilder.map.get(value);

           /* System.out.println("After resetting");
            printMatrix();
            System.out.print("^^^^^^");*/
        }

        previousPositions.clear();

        //System.out.println("size = " + previousPositions.size());
    }

    public void resetCell(int col,int row)
    {
        this.matrix[row][col] = MatrixBuilder.map.get(MatrixBuilder.cellType.empty); // set back to empty
        //printMatrix();
    }

    public boolean removeVirtualCoinFromList(VirtualPlayerCoin v)
    {
        Node toRemove=this.nodesMatrix[v.getRow()][v.getCol()];
        return this.virtualCoinsList.remove(toRemove);
    }

    public boolean removeCoinFromList(Coin c)
    {
        Node toRemove=this.nodesMatrix[c.getRow()][c.getCol()];
        return this.coinsList.remove(toRemove);
    }

    public boolean removeIceObstacleFromList(IceObstacle i)
    {
        Node toRemove=this.nodesMatrix[i.getRow()][i.getCol()];
        return this.iceObstaclesList.remove(toRemove);
    }

    public boolean removeNatureObstacleFromList(NatureObstacle n)
    {
        Node toRemove=this.nodesMatrix[n.getRow()][n.getCol()];
        return this.natureObstacleList.remove(toRemove);
    }

    public void addNatureObstacle(NatureObstacle n)
    {
        Node toAdd = this.nodesMatrix[n.getRow()][n.getCol()];
        this.natureObstacleList.add(toAdd);
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int[][] getMatrixCopyWithPlayers() {
        int realPlayerRow = gameScreen.getPlayer().getRealPlayerRow(gameScreen.mapCellWidth, gameScreen.mapHeight);
        int realPlayerCol = gameScreen.getPlayer().getRealPlayerCol(gameScreen.mapCellWidth);
        int virtualPlayerRow = gameScreen.getVirtualPlayer().getVirtPlayerRow(gameScreen.mapCellWidth, gameScreen.mapHeight);
        int virtualPlayerCol = gameScreen.getVirtualPlayer().getVirtPlayerCol(gameScreen.mapCellWidth);

        // deep copy - matrix into newMatrix
        int[][] newMatrix = new int[matrix.length][];
        for ( int i = 0; i < newMatrix.length; i++ ) {
            newMatrix[i] = new int[matrix[i].length];
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, newMatrix[i].length);
        }

        // real player
        newMatrix[realPlayerRow][realPlayerCol] = REALPLAYER_MATRIX_CODE;
        // virtual player
        newMatrix[virtualPlayerRow][virtualPlayerCol] = VIRTPLAYER_MATRIX_CODE;

        //printMatrix(newMatrix);

        return newMatrix;
    }

    public void printMatrix()
    {
        /* for debug use */
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(this.matrix[i][j]);
            }
            System.out.println();
        }
    }

    public void printMatrix(int[][] matrixToPrint)
    {
        System.out.println("THE NEW MATRIX:");
        System.out.println("_____________________________");

        /* for debug use */
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(matrixToPrint[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Get first empty node from the bottom left so virtual player get go around
     * @return first empty node from bottom left
     */
    public Node getGroundNodeToGoAround()
    {
        for (int i = rows - 1; i >= 0; i--) {
            for (int j = 0; j < cols; j++) {
                if(this.map.get(MatrixBuilder.cellType.empty).equals(this.matrix[i][j]))
                {
                    return this.nodesMatrix[i][j];
                }
            }
        }
        return null;
    }

    // check if there are any more real coins blocked
    public boolean blockedRealCoinsExists(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (this.map.get(MatrixBuilder.cellType.iceObstacles).equals(this.matrix[i][j])) {
                    return true;
                }
            }
        }
        return false;
    }
}