package coopworld.game.client;

import com.badlogic.gdx.utils.TimeUtils;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;

import java.util.Date;

import coopworld.game.CooperativeGame;
import coopworld.game.CrossPlatform.GeneralUtils;
import coopworld.game.Logs.User;
import coopworld.game.Networking.ResponseListeners.StartCodeListener;
import coopworld.game.Networking.ResponseListeners.UserIdListener;
import coopworld.game.Tools.Utils;

/**
 * Created by Chen on 13/10/2017.
 */

public class WebUtils implements GeneralUtils {
    public long getNanoTime(){
        return new TimeUtils().nanoTime();
    }

    public String getTimeStamp(){
        DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss.SSS");
        //final int STANDARD_OFFSET = -120; // UTC+2, winter
        final int DST_OFFSET = -180; // UTC+3, summer

        return fmt.format(new Date(), TimeZone.createTimeZone(DST_OFFSET));
    }

    public String formatDuration(long duration){
        long hours = msToHours(duration);
        long minutes = msToMinutes(duration) % 60;
        long seconds = msToSeconds(duration) % 60;
        return format(hours, minutes, seconds);
    }

    public String getTimeLength(String startTimeStr, String endTimeStr) {
        long duration;
        DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss.SSS");

        Date startTime = fmt.parse(startTimeStr);
        Date now = fmt.parse(endTimeStr);
        duration = now.getTime() - startTime.getTime();

        return formatDuration(duration);
    }

    public String getTimeLengthReduced(String totalTime, String timeLeft) {
        long duration;
        DateTimeFormat fmt = DateTimeFormat.getFormat("mm:ss");

        Date startTime = fmt.parse(totalTime);
        Date now = fmt.parse(timeLeft);
        duration = now.getTime() - startTime.getTime();

        return formatDuration(duration);
    }

    public double getTimeLengthMS(String startTimeStr, String endTimeStr) {
        double duration;
        DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss.SSS");
        Date startTime = fmt.parse(startTimeStr);
        Date now = fmt.parse(endTimeStr);
        duration = now.getTime() - startTime.getTime();
        return duration;
    }

    public double getTimeLengthSeconds(String startTimeStr, String endTimeStr) {
        long duration;
        double seconds = 0f;
        DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss.SSS");
        Date startTime = fmt.parse(startTimeStr);
        Date now = fmt.parse(endTimeStr);
        duration = now.getTime() - startTime.getTime();
        seconds = Utils.round((duration / 1000f), 3);
        return seconds;
    }

    public double getTimeLengthMinutes(String startTimeStr, String endTimeStr) {
        long duration;
        double minutes = 0f;
        DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss.SSS");
        Date startTime = fmt.parse(startTimeStr);
        Date now = fmt.parse(endTimeStr);
        duration = now.getTime() - startTime.getTime();
        minutes = Utils.round(((duration / (1000f)) / 60f), 3);
        return minutes;
    }
    public String format(long hours, long minutes, long seconds){
        StringBuilder sb = new StringBuilder();
        sb.append(timeToStr(hours)).append(":").append(timeToStr(minutes)).append(":").append(timeToStr(seconds));
        String time = sb.toString();
        return time;
    }

    String timeToStr(long timeUnit){
        String str = timeUnit + "";
        if(str.length() == 1){
            str = "0" + str;
        }
        return str;
    }

    // Handy constants for conversion methods
    static final long C0 = 1L;
    static final long C1 = C0 * 1000L;
    static final long C2 = C1 * 1000L;
    static final long C3 = C2 * 1000L;
    static final long C4 = C3 * 60L;
    static final long C5 = C4 * 60L;
    static final long C6 = C5 * 24L;

    static final long MAX = Long.MAX_VALUE;

    public long msToHours(long d) {
        return d / (C5 / C2);
    }

    public long msToMinutes(long d) {
        return d / (C4 / C2);
    }

    public long msToSeconds(long d) {
        return d / (C3 / C2);
    }

    public String getParam(String paramName){
        String value = com.google.gwt.user.client.Window.Location.getParameter(paramName);
        return value;
    }

    @Override
    public User getUser(String user_id) {
        CooperativeGame.conn.getUser(user_id);
        return null; // TODO COOP2 - CHECK THIS
    }
}