package coopworld.game.Tools.AiTools;

/**
 * Created by Eva on 10/28/2016.
 */
public class MatrixCell {
    private int col;
    private  int row;

    public MatrixCell(int row,int col)
    {
        this.col=col;
        this.row=row;
    }

    public int getX() {
        return col;
    }

    public int getY() {
        return row;
    }

    @Override
    public boolean equals(Object obj) {
        MatrixCell m =(MatrixCell)obj;
        return ((m.getX()==this.col) && (m.getY()==this.row));
    }
}
