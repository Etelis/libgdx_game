package coopworld.game.desktop;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import coopworld.game.CooperativeGame;
import coopworld.game.CrossPlatform.GeneralUtils;
import coopworld.game.Logs.User;
import coopworld.game.Tools.Utils;

/**
 * Created by Chen on 13/10/2017. - the Android one! share it on core.
 */

public class DesktopUtils implements GeneralUtils {
    public long getNanoTime(){
        return System.nanoTime();
    }

    public String getTimeStamp(){
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
        df.setTimeZone(TimeZone.getTimeZone("Israel"));

        String gmtTime = df.format(new Date());
        return gmtTime;
    }

    @Override
    public String formatDuration(long duration) {
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public String getTimeLength(String startTimeStr, String endTimeStr) {
        long duration = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
            Date startTime = sdf.parse(startTimeStr);
            Date now = sdf.parse(endTimeStr);
            duration = now.getTime() - startTime.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatDuration(duration);

    }

    @Override
    public double getTimeLengthMS(String startTimeStr, String endTimeStr) {
        double duration = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
            Date startTime = sdf.parse(startTimeStr);
            Date now = sdf.parse(endTimeStr);
            duration = now.getTime() - startTime.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return duration;
    }

    @Override
    public double getTimeLengthMinutes(String startTimeStr, String endTimeStr) {
        long duration = 0;
        double minutes = 0f;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
            Date startTime = sdf.parse(startTimeStr);
            Date now = sdf.parse(endTimeStr);
            duration = now.getTime() - startTime.getTime();
            minutes = Utils.round(((duration / 1000f) / 60f), 3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return minutes;
    }

    @Override
    public double getTimeLengthSeconds(String startTimeStr, String endTimeStr) {
        long duration = 0;
        double seconds = 0f;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
            Date startTime = sdf.parse(startTimeStr);
            Date now = sdf.parse(endTimeStr);
            duration = now.getTime() - startTime.getTime();
            seconds = Utils.round((duration / 1000f), 3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return seconds;
    }

    public String getParam(String paramName){
        String value = "Android";
        return value;
    }

    @Override
    public User getUser(String user_id) {
        CooperativeGame.conn.getUser(user_id);
        return null; // TODO COOP2 - CHECK THIS
    }
}