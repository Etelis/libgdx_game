package coopworld.game.Tools.AiTools.DataStructures;

/**
 * Created by Eva on 10/28/2016.
 */
public class Node {
    private coopworld.game.Tools.AiTools.MatrixCell matrixCell;
    private Node parent;
    private int distance;
    private boolean randomlyGenerated;

    public Node(int row,int col)
    {
        this.matrixCell = new coopworld.game.Tools.AiTools.MatrixCell(row,col);
        this.parent = null;
        this.distance= 9999999; // this is the non visited distance!
        this.randomlyGenerated = false;
    }
    public int getDistance()
    {
        return this.distance;
    }

    public void setDistance(int distance)
    {
       this.distance=distance;
    }

    public void setParent(Node p)
    {
        this.parent=p;
    }

    public int computeManhattanDistance(Node other)
    {
        int currentCol = other.getMatrixCell().getX();
        int currentRow = other.getMatrixCell().getY();
        return (Math.abs(currentCol-this.matrixCell.getX()) + Math.abs(currentRow-this.matrixCell.getY()));
    }

    public Node getParent() {
        return parent;
    }

    public coopworld.game.Tools.AiTools.MatrixCell getMatrixCell() {
        return matrixCell;
    }

    @Override
    public boolean equals(Object obj) {
        Node n = (Node)obj;
        return this.matrixCell.equals(n.getMatrixCell());
    }

    public void print(){
        //System.out.println("COL: " + this.matrixCell.getX() + " ROW: " + this.matrixCell.getY());
    }

    public void setRandomlyGenerated(boolean randomlyGenerated)
    {
        this.randomlyGenerated = randomlyGenerated;
    }

    public boolean isRandomlyGenerated() {
        return randomlyGenerated;
    }
}
