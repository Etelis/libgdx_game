package coopworld.game.Tools;

import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

import coopworld.game.CooperativeGame;
import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Sprites.IceObstacle;
import coopworld.game.Sprites.LockedCoin;
import coopworld.game.Tools.AiTools.MatrixCell;

/**
 * Created by User on 08/03/2017.
 */
public class Helping {
    public static boolean isBlockedCoin(int coinRow, int coinCol, HashMap<IceObstacle, LockedCoin> iceObstacles){
        for (Map.Entry<IceObstacle, LockedCoin> entry : iceObstacles.entrySet()) {
            if ((entry.getValue().getRow() == coinRow) && (entry.getValue().getCol() == coinCol)){
                return true;
            }
        }
        return false;
    }

    // return null if the pressed position is not next to a blocked coin.
    // todo - maybe  add a voice command to explain it.
    public static MatrixCell getBlockedCoin(Vector2 newPoint, HashMap<IceObstacle, LockedCoin> iceObstacles){
        //System.out.println("X touched :" + newPoints.x + ",Y Touched : " + newPoints.y);

        /* The points returned by the input.getX and getY function are screen coordinates
            meaning that they depend on the screen size.
            To get something general - we use the view port
            http://stackoverflow.com/questions/21351339/libgdx-get-scaled-touch-position
            which will translate the coordinate according to the view port set in this screen
           (( this.viewPort = new FitViewport(this.mapWidth * this.mapCellWidth /
            CooperativeGame.PPM, this.mapHeight * this.mapCellHeight /
            CooperativeGame.PPM,
            orthographicCamera); ))

            Explanation:
            Here 19.2 = this.mapWidth * this.mapCellWidth / CooperativeGame.PPM
            11.52 = this.mapHeight * this.mapCellHeight /  CooperativeGame.PPM

            (0,11.52) ************************************** (19.2,11.52)
                      **************************************
                      **************************************
            (0,0)     ************************************** (19.2,0)

            need to translate to

            (0,0)     ************************************** (mapWidth -1 ,0)
                      **************************************
                      **************************************
     (0,mapHeight -1) ************************************** (mapWidth -1 ,mapHeight -1)

            To get the row and col needed, we use the new points x and y which will be
            on scale of mapWidth*64/CooperativeGame.PPM,mapHeight*64/CooperativeGame.PPM - so we need to multiply
            by CooperativeGame.PPM (=100) and then divide by 64.
            Again, row indexing here is from bottom to top meaning we will substract the
            number to Gamescreen.mapheight to get the result we need.
        */

        int rowPressed = GameScreen.mapHeight - (int)(newPoint.y * CooperativeGame.PPM
                / (GameScreen.mapCellHeight)) - 1;
        int colPressed = (int)(newPoint.x * CooperativeGame.PPM / GameScreen.mapCellHeight);
        // The press is out of screen - check the position (row, col) only if it is valid.
        // todo - test it on other devices. with white borders - right and left.
        if(!(isValidIndex(rowPressed, colPressed))) {
            return null;
        }
        // valid press position.
        for(int i = 0; i < Constants.obstaclesSize; i++){
            for(int j = 0; j < Constants.obstaclesSize; j++){
                // check if it is a coin
                // todo - crash! length=18; index=-1 (UP) think it is OK now (5.9.17)
                if(!(isValidIndex(rowPressed - 1 + i, colPressed - 1 + j))) {
                    continue;
                }
                int curr = GameScreen.searchableMatrix.getMatrix()
                        [rowPressed - 1 + i][colPressed - 1 + j];
                if((int)Constants.COINS_MATRIX_CODE == GameScreen.searchableMatrix.getMatrix()
                    [rowPressed - 1 + i][colPressed - 1 + j]) {
                // if it is a blocked coin
                if (isBlockedCoin(rowPressed - 1 + i, colPressed - 1 + j, iceObstacles)){
                    return new MatrixCell(rowPressed - 1 + i, colPressed - 1 + j);
                }
            }
        }
    }
        // no nearby coin was detected.
        return null;
    }

    public static boolean isValidIndex(int row, int col){
        if(!((row >= 0 && row < GameScreen.mapHeight) && (col >= 0
                && col < GameScreen.mapWidth))){
            return false;
        }
        return true;
    }

    public static void playIceCubeThrowing(){

    }

    // row
    public static int getTiledRow(float yLibgdx){
        return (int)(yLibgdx / (GameScreen.mapCellHeight));
        //return GameScreen.mapHeight - (int)(yLibgdx / (GameScreen.mapCellHeight)) - 1;
    }

    // col
    public static int getTiledCol(float xLibgdx){
        return (int)(xLibgdx / GameScreen.mapCellHeight);
    }

}
