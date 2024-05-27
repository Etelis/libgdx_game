package coopworld.game.CrossPlatform;

import coopworld.game.Logs.User;


public interface GeneralUtils {
    long getNanoTime();
    String getTimeStamp();
    String formatDuration(long duration);
    String getTimeLength(String startTimeStr, String endTimeStr);
    double getTimeLengthMinutes(String startTimeStr, String endTimeStr);
    double getTimeLengthSeconds(String startTimeStr, String endTimeStr);
    double getTimeLengthMS(String startTimeStr, String endTimeStr);
    String getParam(String paramName);


    User getUser(String user_id);

}

