package coopworld.game.Logs;

import com.badlogic.gdx.Gdx;

import coopworld.game.CooperativeGame;
import coopworld.game.CrossPlatform.GeneralUtils;
import coopworld.game.Tools.Utils;

public class TotalTime {
    double minutes;
    double seconds;
    double milliseconds;

    public TotalTime(String startTime, String endTime) {
        GeneralUtils generalUtils = ((CooperativeGame) Gdx.app.getApplicationListener()).crossPlatformObjects.getUtils();
        double minutes = generalUtils.getTimeLengthMinutes(startTime, endTime);
        double seconds = generalUtils.getTimeLengthSeconds(startTime, endTime);
        double milliseconds = generalUtils.getTimeLengthMS(startTime, endTime);

        this.minutes = minutes;
        this.seconds = seconds;
        this.milliseconds = milliseconds;
    }

    public TotalTime(double minutes, double seconds, double milliseconds) {
        this.minutes = minutes;
        this.seconds = seconds;
        this.milliseconds = milliseconds;
    }

    public void plus(TotalTime another){
        // TODO - remove code duplication.
        this.minutes += another.minutes;
        this.minutes = Utils.round(this.minutes, 3);

        this.seconds += another.seconds;
        this.seconds = Utils.round(this.seconds, 3);

        this.milliseconds += another.milliseconds;
        this.milliseconds = Utils.round(this.milliseconds, 3);
    }

    public double getMinutes() {
        return minutes;
    }
}