package coopworld.game.Networking;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;

import coopworld.game.CooperativeGame;
import coopworld.game.Logs.LevelLog;
import coopworld.game.Logs.User;
import coopworld.game.Networking.ResponseListeners.UserIdListener;
import coopworld.game.Tools.Constants;

import static com.badlogic.gdx.net.HttpRequestHeader.ContentType;


/**
 * Connection with a server
 */
public abstract class Connection {
    protected String prefix;
    protected String ip;
    protected String port;
    protected String urlLevelLog;
    protected String urlLevelStart;
    protected String urlGameData;
    protected String urlUsersData;
    protected String urlGetUser;
    protected String urlRegisterUser;
    protected String urlGetWebsiteStatus;

    protected String text;
    protected SocketHints hints;
    protected boolean debugMode;
    protected long timeoutMs;
    protected CooperativeGame game;

    protected ArrayList<Integer> successfullySentLevels;

    public Connection(CooperativeGame game) {
        this.game = game;
        try {
            // TODO EXPERIMENT SARIT - CHECK IF NEEDED
            this.hints = new SocketHints();
            readConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        read configuration file to get ip and port
         */
    public void readConfig() {
        FileHandle file = Gdx.files.internal("config.txt");
        String text = file.readString();
        String lines[] = text.split("\\r?\\n");
        this.prefix = "http://";
        this.ip = lines[0];
        this.port = lines[1];
        // this.urlGameData = prefix + ip + lines[2];
        this.urlGameData = lines[2];
        this.urlLevelLog = lines[3];
        this.urlLevelStart = lines[4];
        this.urlUsersData = lines[5];
        this.urlGetUser = lines[6];
        this.urlRegisterUser = lines[7];
        this.urlGetWebsiteStatus = lines[8];

        this.debugMode = false;
        this.timeoutMs = 5 * 1000000000L;
        this.successfullySentLevels = new ArrayList<Integer>();
    }

    public void setDebugMode(boolean value) {
        this.debugMode = value;
    }

    /*
    make legal string to send to the Database
    */
    public String makeString(Object before){
        Json json = new Json();
        // serialize all fields, including default values.
        // for example, it makes sure that an int which is 0 will not be ignored in the json string.
        json.setUsePrototypes(false);
        // important for valid json!
        json.setOutputType(JsonWriter.OutputType.json);
        // the string jsonStr represents the GameData object in a json format.
        String jsonStr = json.toJson(before);
        return jsonStr;
    }

    /*
    procedure in end of level implemented different each version (LevelLog)
    */
    public abstract void sendLevelLog(LevelLog levelLog);

    public abstract void sendLevelStart(Object requestObject);


    public void getUser(String userId) {
        UserIdListener userIdListener = new UserIdListener(game);
        String requestContent = null;

        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
        String url;

        url = urlGetUser;
        url += "?val="+ userId;

        httpRequest.setUrl(url);
        httpRequest.setHeader(ContentType, "application/json; charset=utf-8");
        httpRequest.setContent(requestContent);

        Gdx.net.sendHttpRequest(httpRequest, userIdListener);
    }

    public void getWebsiteStatus(Net.HttpResponseListener responseListener) {
        String requestContent = null;

        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
        String url = urlGetWebsiteStatus;

        httpRequest.setUrl(url);
        httpRequest.setHeader(ContentType, "application/json; charset=utf-8");
        httpRequest.setContent(requestContent);

        Gdx.net.sendHttpRequest(httpRequest, responseListener);
    }

    public abstract void sendRegistrationData(Object requestObject);

    public abstract void sendEntranceData(Object requestObject);

    public void addToLevelsList(int levelNum){
        successfullySentLevels.add(levelNum);
    }

    public ArrayList<Integer> getSuccessfullySentLevels() {
        return successfullySentLevels;
    }
}