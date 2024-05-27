package coopworld.game;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import coopworld.game.CrossPlatform.GeneralUtils;
import coopworld.game.Logs.User;

/**
 * Created by Chen on 12/10/2017.
 */

public class AndroidUtils implements GeneralUtils{
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

    // TODO COOP2 WEB
    @Override
    public double getTimeLengthMinutes(String startTimeStr, String endTimeStr) {
        return 0;
    }

    @Override
    public double getTimeLengthSeconds(String startTimeStr, String endTimeStr) {
        return 0;
    }

    @Override
    public double getTimeLengthMS(String startTimeStr, String endTimeStr) {
        // TODO COOP2 WEB
        /*
        long duration = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
            Date startTime = sdf.parse(startTimeStr);
            Date now = sdf.parse(endTimeStr);
            duration = now.getTime() - startTime.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Long.toString(duration);

         */
        return 1;
    }

    @Override
    public String getParam(String paramName) {
        return null;
    }

    @Override
    public User getUser(String user_id) {
        CooperativeGame.conn.getUser(user_id);
        return null; // TODO COOP2 - CHECK THIS
    }
}
