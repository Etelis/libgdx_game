package coopworld.game.Tools.RequestUtils;

import com.badlogic.gdx.Gdx;

import coopworld.game.CooperativeGame;
import coopworld.game.Logs.Enums;
import coopworld.game.Logs.HelpRequests.HelpRequest;
import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Tools.AiTools.MatrixCell;

/**
 * Created by Chen on 29/10/2017.
 */

public class HumanPlayerRequestUtils {
    GameScreen gameScreen;
    HelpRequest helpRequest;

    // The nature obstacle cell that the virtual player ask to remove.
    protected MatrixCell blockedElementMatrixCell;

    public void init(){
        helpRequest = new HelpRequest();
        String currTime = ((CooperativeGame) Gdx.app.getApplicationListener()).
                crossPlatformObjects.getUtils().getTimeStamp();
        helpRequest.setAppearing_time(currTime);
        helpRequest.setHelpAsker(Enums.Player.Human);

        helpRequest.setBoard(gameScreen.searchableMatrix.getMatrixCopyWithPlayers());
        helpRequest.setHuman_score_before(gameScreen.getHumanScore());
        helpRequest.setRemaining_time_in_clock(gameScreen.getHud().getWatchRemainingTime());
    }

    public void handleVirtualResponse(boolean virtualResponse){
        fillHelpRequest(virtualResponse);
    }

    public void fillHelpRequest(boolean virtualResponse){
        String currTime = ((CooperativeGame) Gdx.app.getApplicationListener()).
                crossPlatformObjects.getUtils().getTimeStamp();
        helpRequest.setAsking_time(currTime);
        helpRequest.setAnswer_time(currTime);

        helpRequest.setWasAccepted(virtualResponse);
    }

    public void fillChosenStrategy(Enums.ChosenStrategy chosenStrategy){
        helpRequest.setChosen_virtual_strategy(chosenStrategy);
    }

    public void setAnswerTime(){
        helpRequest.setAnswer_time(((CooperativeGame) Gdx.app.getApplicationListener()).
                crossPlatformObjects.getUtils().getTimeStamp());
    }

    public void writeHelpRequest(){
        gameScreen.getHelpRequests().add(helpRequest);
        CooperativeGame.virtualResponses.add(helpRequest);
        CooperativeGame.allResponses.add(helpRequest);
        // for the next round.
        helpRequest = null;

        gameScreen.setHumanNumRequests(gameScreen.getHumanNumRequests() + 1);
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public MatrixCell getBlockedElementMatrixCell() {
        return blockedElementMatrixCell;
    }

    public void setBlockedElementMatrixCell(MatrixCell blockedElementMatrixCell) {
        this.blockedElementMatrixCell = blockedElementMatrixCell;
    }
}
