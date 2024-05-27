package coopworld.game.Logs.Managers;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

import coopworld.game.CooperativeGame;
import coopworld.game.Logs.ElementCollectionList;
import coopworld.game.Logs.HelpRequests.HelpRequest;
import coopworld.game.Logs.LevelLog;
import coopworld.game.Logs.OnOffEvent;
import coopworld.game.Logs.Scores;
import coopworld.game.Logs.TotalElementsAmount;
import coopworld.game.Screens.GameScreens.CombinedInstructions;
import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Tools.FpsInfo;

public class GameDataUpdater {
    GameScreen gameScreen;

    public GameDataUpdater(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void UpdateGameData(ArrayList<OnOffEvent> mute_events, ArrayList<OnOffEvent> pause_events,
                               ArrayList<HelpRequest> helpRequests, int numOfStars,
                               String virtualStrategy, FpsInfo fpsInfo, ElementCollectionList elementCollectionList){
        LevelLog levelLog = gameScreen.getLevelLog();
        levelLog.setMute_events(mute_events);
        levelLog.setPause_events(pause_events);
        levelLog.setFps_info(fpsInfo);

        levelLog.setHelp_requests(helpRequests);
        levelLog.setVirtual_strategy(virtualStrategy);
        levelLog.setUser_name(CooperativeGame.gameData.getUser().getName());
        levelLog.setPlayer_gender(CooperativeGame.gameData.getUser().getHuman_gender().toString());
        levelLog.setVirtual_gender(CooperativeGame.gameData.getUser().getVirtual_gender().toString());

        // Primitive values.
        levelLog.setEnd_time(((CooperativeGame) Gdx.app.getApplicationListener()).
                crossPlatformObjects.getUtils().getTimeStamp());
        levelLog.setLevel_num(gameScreen.levelNum);
        levelLog.setNum_of_stars(numOfStars);

        // Scores - human and virtual player.
        Scores scores = new Scores();
        scores.setHuman_player_score(gameScreen.humanScore);
        scores.setVirtual_player_score(gameScreen.virtualScore);
        levelLog.setScores(scores);

        levelLog.setElementCollectionList(elementCollectionList);

        if(CooperativeGame.gameData.getGame_params().isResetsScore()) {
            gameScreen.resetScores();
        }
        // score should not be reset from level to level.
        else {
            // reset score after the first (instructional) level.
            if(gameScreen instanceof CombinedInstructions){
                gameScreen.resetScores();
            }
            // "regular" level (not the first/instructional) - accumulate score.
            else{
                CooperativeGame.gameData.getCumulativeScores().increaseHumanScore(gameScreen.humanScore);
                CooperativeGame.gameData.getCumulativeScores().increaseVirtualScore(gameScreen.virtualScore);
            }
        }

        // Total elements amount.
        TotalElementsAmount totalElementsAmount = new TotalElementsAmount();
        totalElementsAmount.setTotal_coins_displayed(gameScreen.displayedCoins);
        totalElementsAmount.setTotal_virtual_coins_displayed(gameScreen.displayedVirtualCoins);
        totalElementsAmount.setTotal_nature_obstacles_displayed(gameScreen.displayedNatureObstacles);
        totalElementsAmount.setTotal_ice_obstacles_displayed(gameScreen.displayedIceObstacles);
        levelLog.setTotal_elements_amount(totalElementsAmount);

        // Update high score if needed - in GameData object.
        if(gameScreen.humanScore > CooperativeGame.gameData.getUser().getHigh_score()){
            gameScreen.newScore = true;
            levelLog.setNew_record(true);
        }
        else{
            levelLog.setNew_record(false);
        }

        gameScreen.setLevelLog(levelLog);
        // TODO GWT
        CooperativeGame.conn.sendLevelLog(levelLog);
    }
}
