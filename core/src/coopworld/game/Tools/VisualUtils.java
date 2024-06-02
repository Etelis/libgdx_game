package coopworld.game.Tools;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import coopworld.game.CooperativeGame;
import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Tools.AiTools.MatrixCell;

public class VisualUtils {


    public static boolean playerOverlaps(MatrixCell matrixCell, int elementSideSize){
        // compute players positions.
        int virtualPlayerCol = Math.round((GameScreen.getVirtualPlayer().b2body_boy.getPosition().x
                * CooperativeGame.PPM) / GameScreen.mapCellWidth);
        int virtualPlayerRow = Math.round(GameScreen.mapHeight - (GameScreen.getVirtualPlayer().b2body_boy.getPosition().y
                * CooperativeGame.PPM) / GameScreen.mapCellWidth);

        int humanPlayerCol = Math.round((GameScreen.getPlayer().b2body_boy.getPosition().x
                * CooperativeGame.PPM) / GameScreen.mapCellWidth);
        int humanPlayerRow = Math.round(GameScreen.mapHeight - (GameScreen.getPlayer().b2body_boy.getPosition().y
                * CooperativeGame.PPM) / GameScreen.mapCellWidth);

        ArrayList<Vector2> elementPoints = new ArrayList<Vector2>();
        for(int i = 0; i < elementSideSize; i++){
            for(int j = 0; j < elementSideSize; j++){
                // y for rows, x for cols.
                elementPoints.add(new Vector2(matrixCell.getY() + i, matrixCell.getX() + j));
            }
        }

        ArrayList<Vector2> humanPlayerPoints = new ArrayList<Vector2>();
        // players size -> 2 * 3
        for(int i = humanPlayerRow; i > humanPlayerRow - 3; i--){
            for(int j = humanPlayerCol; j > humanPlayerCol - 2; j--) {
                humanPlayerPoints.add(new Vector2(i, j));
            }
        }

        ArrayList<Vector2> virtualPlayerPoints = new ArrayList<Vector2>();
        // players size -> 2 * 3
        for(int i = virtualPlayerRow; i > virtualPlayerRow - 3; i--){
            for(int j = virtualPlayerCol; j > virtualPlayerCol - 2; j--) {
                virtualPlayerPoints.add(new Vector2(i, j));
            }
        }

        // check overlap for real player
        if(elementPoints.contains(humanPlayerPoints)){
            return true;
        }

        for(Vector2 elementPoint : elementPoints){
            for(Vector2 humanPlayerPoint : humanPlayerPoints){
                // same position or distance 1.
                if(humanPlayerPoint.equals(elementPoint)
                        || (pointsDistance(humanPlayerPoint, elementPoint) == 1)){
                    return true;
                }
            }

            for(Vector2 virtualPlayerPoint : virtualPlayerPoints){
                // same position or distance 1.
                if(virtualPlayerPoint.equals(elementPoint)
                        || (pointsDistance(virtualPlayerPoint, elementPoint) == 1)){
                    return true;
                }
            }
        }
        return false;
    }


    public static int pointsDistance(Vector2 p1, Vector2 p2){
        return (int)Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }
}
