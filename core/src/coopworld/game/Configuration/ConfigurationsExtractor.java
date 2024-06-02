package coopworld.game.Configuration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import coopworld.game.Logs.User;

/**
 * Created by Chen on 01/06/2017.
 */

public class ConfigurationsExtractor {
    public ConfigurationsExtractor(){

    }

    public static DevelopmentConfig getDevelopmentConfig(){
        FileHandle file = Gdx.files.internal("development_config.json");
        String text = file.readString();

        Json jsonObject = new Json();
        jsonObject.setTypeName(null);
        jsonObject.setUsePrototypes(false);
        jsonObject.setIgnoreUnknownFields(true);
        jsonObject.setOutputType(JsonWriter.OutputType.json);

        DevelopmentConfig dc = jsonObject.fromJson(DevelopmentConfig.class, text);
        return dc;
    }
}
