package coopworld.game.Tools;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

import coopworld.game.Logs.Enums;
import coopworld.game.Logs.GameLanguage;

public class Utils {
    public static int getComp(int s){
        return (s+202)/108;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        if (Double.isNaN(value)) {
            return -1;
        }
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        Double n = bd.doubleValue();
        return n;
    }

    public static String arrayListToArr(ArrayList<?> arr){
        String arrStr = arr.toString();
        arrStr = arrStr.replace(" ", "");
        arrStr = arrStr.replace("[", "");
        arrStr = arrStr.replace("]", "");

        return arrStr;
    }

    public static int getMapValuesSum(HashMap<Integer, Integer> map){
        int sum = 0;
        for (int f : map.values()) {
            sum += f;
        }
        return sum;
    }

    /*
    make legal string to send to the Database
    */
    public static String makeString(Object before){
        Json json = new Json();
        // serialize all fields, including default values.
        // for example, it makes sure that an int which is 0 will not be ignored in the json string.
        json.setUsePrototypes(false);
        // important for valid json!
        json.setOutputType(JsonWriter.OutputType.json);
        // the string jsonStr represents the RawData object in a json format.
        String jsonStr = json.toJson(before);
        return jsonStr;
    }

    public static float getHeight(Image img, float width){
        return (width * img.getDrawable().getMinHeight()) / img.getDrawable().getMinWidth();
    }

    public static String secondsToMMss(long seconds){
        long mm = seconds / 60;
        long ss = seconds % 60;
        StringBuilder sb = new StringBuilder();
        sb.append(timeToStr(mm)).append(":").append(timeToStr(ss));

        String time = sb.toString();
        return time;
    }

    public static String timeToStr(long timeUnit){
        String str = timeUnit + "";
        if(str.length() == 1){
            str = "0" + str;
        }
        return str;
    }

    public static GameLanguage getGameLanguage(String language){
        Enums.Language languageToUse = null;
        Enums.LanguageDirection directionToUse = null;

        if(language.equals("heb")){
            languageToUse = Enums.Language.Hebrew;
            directionToUse = Enums.LanguageDirection.RTL;
        }
        else if(language.equals("eng")){
            languageToUse = Enums.Language.English;
            directionToUse = Enums.LanguageDirection.LTR;
        }
        else if(language.equals("ar")){
            languageToUse = Enums.Language.Arabic;
            directionToUse = Enums.LanguageDirection.RTL;
        }
        else if(language.equals("mk")){
            languageToUse = Enums.Language.Macedonian;
            directionToUse = Enums.LanguageDirection.LTR;
        }
        else{
            return null;
        }

        GameLanguage gameLanguage = new GameLanguage(languageToUse, directionToUse);
        return gameLanguage;
    }
}
