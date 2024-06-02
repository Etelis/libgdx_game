package coopworld.game.Tools.AiTools;

import com.badlogic.gdx.math.Vector2;

import coopworld.game.Tools.AiTools.DataStructures.Node;

/**
 * Created by Eva on 10/29/2016.
 */
public class Guide {

    /**
     * The path is always a straight path or must run "orthogonally" - conclusion: current node and next node are always
     * right next to each other - whether right,left,up or down.
     * @param current current node
     * @param nextStep nextStep node
     * @return vector according to which the virtual player should move
     */

    Vector2 zeroVec = new Vector2(0f,0f);

    Vector2 xMin25Vec = new Vector2(-0.25f, 0);
    Vector2 x25Vec = new Vector2(0.25f, 0);

    Vector2 xMin3Vec = new Vector2(0, -0.3f);;
    Vector2 x3Vec = new Vector2(0, 0.3f);

    public Vector2 getVector(Node current, Node nextStep)
    {
        if ((nextStep == null) || (nextStep.equals(current)))
            return zeroVec;
        else
        {
            MatrixCell currentCell = current.getMatrixCell();
            MatrixCell nextCell = nextStep.getMatrixCell();
            //  System.out.println("Current cell X:" + currentCell.getX() + " , Y:" + currentCell.getY());
            //  System.out.println("Next cell X:" + nextCell.getX() + " , Y:" + nextCell.getY());
            if (currentCell.getY() == nextCell.getY()) // same row
            {
                //     System.out.println("Right here1!!");
                // go left
                if (currentCell.getX() > nextCell.getX())
                {
                    //  return new Vector2(-1f, 0);
                    return xMin25Vec;
                }
                else // go right
                {
                    //  return new Vector2(1f, 0);
                    return x25Vec;
                }
            }
            else // same col
            {
                //     System.out.println("Right here2!!");
                // go up
                if (currentCell.getY() < nextCell.getY())
                {
                    return xMin3Vec;
                    //return new Vector2(0, -0.7f);
                }
                else // go down
                {
                    return x3Vec;
                    //return new Vector2(0, 0.7f);
                }
            }
        }
    }
}