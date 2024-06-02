package coopworld.game.Tools;

import coopworld.game.CooperativeGame;
import coopworld.game.Screens.GameScreens.GameScreen;

public class CountdownTimer {
    private float totalTime;
    private float elapsedTime;
    private boolean timer_is_running;
    private GameScreen gameScreen;
    private float remainingTime;
    private float timeSinceLastHelpRequest;
    private float helpRequestsIntervalSeconds;

    public CountdownTimer(float totalTimeInSeconds, GameScreen gameScreen) {
        this.totalTime = totalTimeInSeconds;
        this.elapsedTime = 0;
        this.timer_is_running = false;
        this.timeSinceLastHelpRequest = 0;
        this.gameScreen = gameScreen;
        this.helpRequestsIntervalSeconds = CooperativeGame.gameData.
                getGame_params().getHelp_requests_interval();
    }

    public void start() {
        this.timer_is_running = true;
    }

    public void pause() {
        this.timer_is_running = false;
    }

    public void resume() {
        this.timer_is_running = true;
    }

    public void update(float delta) {
        if (timer_is_running) {
            elapsedTime += delta;
            timeSinceLastHelpRequest += delta;
            remainingTime = totalTime - elapsedTime;
            if (remainingTime <= 0) {
                //complete();
                pause(); // Stop the timer
            }
        }
    }

    public float getRemainingTime() {
        remainingTime = totalTime - elapsedTime;
        return Math.max(0, remainingTime);
    }

    public void resetHelpRequestTimeCounting(){
        this.timeSinceLastHelpRequest = 0;
    }

    public boolean isTimePassedToGenerateHelpRequest(){
        if(timeSinceLastHelpRequest >= helpRequestsIntervalSeconds){
            return true;
        }
        return false;
    }

    private void complete() {
        // Logic to execute when the timer completes
        gameScreen.setState(GameScreen.State.LevelEnded);
        //System.out.println("Timer Complete!");
    }

    public String getTimeFormatted() {
        int totalSecondsLeft = (int) Math.ceil(getRemainingTime());
        return Utils.secondsToMMss(totalSecondsLeft);
    }
}
