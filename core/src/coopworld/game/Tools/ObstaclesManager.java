package coopworld.game.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Sprites.IceObstacle;
import coopworld.game.Sprites.LockedCoin;
import coopworld.game.Sprites.LockedVirtualCoin;
import coopworld.game.Sprites.NatureObstacle;

/**
 * Created by Chen on 29/10/2017.
 */

public class ObstaclesManager {
    GameScreen gameScreen;

    public ObstaclesManager(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void removeAllNatureObstacles(){
        // remove the wanted nature obstacle from map.
        HashMap<NatureObstacle, LockedVirtualCoin> natureObstacles = gameScreen.getNatureObstacles();
        List<NatureObstacle> natureObstaclesToRemove = new ArrayList<>();
        List<LockedVirtualCoin> lockedVirtualCoinsToRemove = new ArrayList<>();
        Iterator it = natureObstacles.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<NatureObstacle, LockedVirtualCoin> entry = (Map.Entry)it.next();
            NatureObstacle natureObstacle = entry.getKey();
            // remove the inner element.
            LockedVirtualCoin lockedVirtualCoin = entry.getValue();
            //lockedVirtualCoin.remove();
            // remove the obstacle.
            //natureObstacle.remove();

            natureObstaclesToRemove.add(natureObstacle);
            lockedVirtualCoinsToRemove.add(lockedVirtualCoin);
        }
        for(LockedVirtualCoin lvc : lockedVirtualCoinsToRemove){
            lvc.remove();
        }

        for(NatureObstacle no : natureObstaclesToRemove){
            no.remove();
        }

        /*
        for (Map.Entry<NatureObstacle, LockedVirtualCoin> entry : natureObstacles.entrySet()) {
            NatureObstacle natureObstacle = entry.getKey();
                // remove the inner element.
                LockedVirtualCoin lockedVirtualCoin = entry.getValue();
                lockedVirtualCoin.remove();
                // remove the obstacle.
                natureObstacle.remove();
        }
        */
    }

    public void removeAllIceObstacle(){
        // remove the wanted nature obstacle from map.
        HashMap<IceObstacle, LockedCoin> iceObstacles = gameScreen.getIceObstacles();
        List<IceObstacle> iceObstaclesToRemove = new ArrayList<>();
        List<LockedCoin> lockedCoinsToRemove = new ArrayList<>();

        for (Map.Entry<IceObstacle, LockedCoin> entry : iceObstacles.entrySet()) {
            IceObstacle iceObstacle = entry.getKey();
            // remove the inner element.
            LockedCoin lockedCoin = entry.getValue();
            /*
            lockedCoin.remove();
            // remove the obstacle.
            iceObstacle.remove();
            */
            iceObstaclesToRemove.add(iceObstacle);
            lockedCoinsToRemove.add(lockedCoin);

        }

        for(LockedCoin ls : lockedCoinsToRemove){
            ls.remove();
        }

        for(IceObstacle io : iceObstaclesToRemove){
            io.remove();
        }

    }

    public void removeAllObstacles(){
        removeAllNatureObstacles();
        removeAllIceObstacle();
    }


    public void removeNatureObstacle(boolean removeInnerElement){
        // remove the wanted nature obstacle from map.
        HashMap<NatureObstacle, LockedVirtualCoin> natureObstacles = gameScreen.getNatureObstacles();

        for (Map.Entry<NatureObstacle, LockedVirtualCoin> entry : natureObstacles.entrySet()) {
            NatureObstacle natureObstacle = entry.getKey();
            if (natureObstacle.getRow() == (gameScreen.getVirtualPlayer().getVirtualPlayerRequestUtils().getBlockedElementMatrixCell().
                    getY() - 1)
                    && (natureObstacle.getCol() == (gameScreen.getVirtualPlayer().getVirtualPlayerRequestUtils().
                    getBlockedElementMatrixCell().getX() - 1))){
                // don't the virtual coin too.
                if(removeInnerElement) {
                    LockedVirtualCoin lockedVirtualCoin = entry.getValue();
                    lockedVirtualCoin.remove();
                }
                // don't remove the virtual coin. make it free so that the virtual player
                // could collect it.
                else{
                    natureObstacle.setCoinFree();
                }
                natureObstacle.remove();

                // initialize for the next virtual help request.
                gameScreen.virtualAskState = GameScreen.VirtualAskState.JustAsked;
                // back to Running state.
                gameScreen.switchToRunningState(true);
                gameScreen.music.toneUpBackgroundMusic();
                break;
            }
        }
    }

    public void removeIceObstacle(boolean removeInnerElement) {
        // remove the wanted ice obstacle from map.
        HashMap<IceObstacle, LockedCoin> iceObstacles = gameScreen.getIceObstacles();
        for (Map.Entry<IceObstacle, LockedCoin> entry : iceObstacles.entrySet()) {
            IceObstacle iceObstacle = entry.getKey();
            if (iceObstacle.getRow() == (gameScreen.getPlayer().getHumanPlayerRequestUtils().getBlockedElementMatrixCell().
                    getY() - 1)
                    && (iceObstacle.getCol() == (gameScreen.getPlayer().getHumanPlayerRequestUtils().
                    getBlockedElementMatrixCell().getX() - 1))) {
                // don't delete the inner.
                if(removeInnerElement) {
                    LockedCoin lockedCoin = entry.getValue();
                    lockedCoin.remove();
                }
                // remove button only if this ice obstacle is the last one.
                if(gameScreen.getIceObstacles().size() == 1){
                    gameScreen.getHelpButton().remove();
                }
                iceObstacle.setCategoriyFilter(Constants.destroyedBit);
                iceObstacle.removeGraphicObstacleFromMap();
                iceObstacle.removeObstacleFromMatrix();
                gameScreen.getVirtualPlayer().dequeueNodeFromQueue(iceObstacle.getRow() + 1,
                        iceObstacle.getCol() + 1);
                gameScreen.m.addToValidPositionsList(iceObstacle);
                gameScreen.searchableMatrix.removeIceObstacleFromList(iceObstacle);
                gameScreen.getIceObstacles().remove(iceObstacle);
                gameScreen.ChangeStateHelp();
                gameScreen.music.toneUpBackgroundMusic();
                break;
            }
        }
    }
}
