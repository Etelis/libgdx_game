package coopworld.game.Tools.RequestUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;

import coopworld.game.CooperativeGame;
import coopworld.game.Logs.Enums;
import coopworld.game.Logs.HelpRequests.HelpRequest;
import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Sprites.HelpRequestPopUp;
import coopworld.game.Tools.AiTools.MatrixCell;

/**
 * Created by Chen on 29/10/2017.
 */

public class VirtualPlayerRequestUtils {
    GameScreen gameScreen;
    HelpRequest helpRequest;
    private HelpRequestPopUp helpRequestPopUp;
    private Group helpRequestPopUpGroup;
    // The nature obstacle cell that the virtual player ask to remove.
    protected MatrixCell blockedElementMatrixCell;

    public VirtualPlayerRequestUtils() {
        // see set gameScreen.
    }

    public void init(){
        helpRequest = new HelpRequest();
        helpRequest.setAsking_time(((CooperativeGame) Gdx.app.getApplicationListener()).
                crossPlatformObjects.getUtils().getTimeStamp());
        helpRequest.setHelpAsker(Enums.Player.Virtual);
        helpRequest.setBoard(gameScreen.searchableMatrix.getMatrixCopyWithPlayers());
        helpRequest.setHuman_score_before(gameScreen.getHumanScore());
        helpRequest.setRemaining_time_in_clock(gameScreen.getHud().getWatchRemainingTime());
    }

    public void handleHumanResponse(){
        // TODO HELP REQ!
        boolean humanAccept = this.helpRequestPopUp.getHumanResponse();
        // pop-up removing.
        helpRequestPopUpGroup.remove();
        // human press "Yes".
        if(humanAccept){
            // decrement score.
            // todo - obstacle disappear.
            gameScreen.decreaseScore(Enums.Player.Human, CooperativeGame.gameData.getGame_params().
                    getReciprocityValues().getHelp_providing_cost_human());
            // virtual player say thanks.
            gameScreen.music.thanksSound();
            gameScreen.addThanksButton = true;
            gameScreen.virtualAskState = GameScreen.VirtualAskState.RemoveNatureObstacle;
            gameScreen.music.coinFallSound();

            fillHelpRequest(true);
        }
        // human press "No".
        else{
            gameScreen.virtualAskState = GameScreen.VirtualAskState.RemoveNatureObstacleAndVirtualCoin;
            fillHelpRequest(false);
        }

        gameScreen.music.toneUpBackgroundMusic();

        //todo - HUD
        //gameScreen.getHud().startCoinAnimation();
    }

    public void fillHelpRequest(boolean humanResponse){
        helpRequest.setWasAccepted(humanResponse);
    }

    public void request() {
        Gdx.input.vibrate(70);
        init();
        // Pop-Up
        gameScreen.stage.addActor(helpRequestPopUpGroup);
        gameScreen.virtualAskState = GameScreen.VirtualAskState.PopUpDisplay;
    }

    public void setAnswerTime(){
        helpRequest.setAnswer_time(((CooperativeGame) Gdx.app.getApplicationListener()).
                crossPlatformObjects.getUtils().getTimeStamp());
    }

    public void writeHelpRequest(){
        gameScreen.getHelpRequests().add(helpRequest);
        CooperativeGame.humanResponses.add(helpRequest);
        CooperativeGame.allResponses.add(helpRequest);
        // for the next round.
        helpRequest = null;

        gameScreen.setVirtualNumRequests(gameScreen.getVirtualNumRequests() + 1);
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        helpRequestPopUp = new HelpRequestPopUp(this.gameScreen);
        helpRequestPopUpGroup = helpRequestPopUp.helpRequestPopUp;
    }

    public MatrixCell getBlockedElementMatrixCell() {
        return blockedElementMatrixCell;
    }

    public void setBlockedElementMatrixCell(MatrixCell blockedElementMatrixCell) {
        this.blockedElementMatrixCell = blockedElementMatrixCell;
    }

    public HelpRequestPopUp getHelpRequestPopUp() {
        return helpRequestPopUp;
    }
}
