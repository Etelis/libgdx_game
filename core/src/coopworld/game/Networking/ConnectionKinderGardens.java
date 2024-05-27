package coopworld.game.Networking;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpStatus;

import java.io.IOException;

import coopworld.game.CooperativeGame;
import coopworld.game.Logs.LevelLog;
import coopworld.game.Logs.LevelStart;
import coopworld.game.Logs.User;
import coopworld.game.Tools.Constants;

import static com.badlogic.gdx.net.HttpRequestHeader.ContentType;

/**
 * Connection with a server - for the kinder garden players
 */

public class ConnectionKinderGardens extends Connection {
    public ConnectionKinderGardens(CooperativeGame game) {
        super(game);
    }


    public void sendPostRequest(final String jsonStr, String url, final String filePath) {
        try {
            Net.HttpRequest httpPost = new Net.HttpRequest(Net.HttpMethods.POST);
            httpPost.setUrl(url);
            httpPost.setContent(jsonStr);
            httpPost.setHeader(ContentType, "application/json; charset=utf-8");
            Gdx.net.sendHttpRequest(httpPost, new Net.HttpResponseListener() {
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    HttpStatus status = httpResponse.getStatus();
                    String result = httpResponse.getResultAsString();

                    if (status.getStatusCode() == HttpStatus.SC_OK && result.equals("OK")) {
                        Gdx.app.log("PutUserService", " successful with code:" + status.getStatusCode());
                    } else {
                        Gdx.app.log("PutUserService", "update user name services return code: " + String.valueOf(status.getStatusCode()));
                    }
                }

                public void failed(Throwable t) {
                    String message = t.getMessage();
                    String status = "failed";
                }

                @Override
                public void cancelled() {
                    String status = "failed";
                }
            });
        } catch (final Exception e) {
            String status = "failed";
        }
    }

    public void sendPostRequestLevelCopied(final String jsonStr, final LevelLog levelLog, final String url, final String filePath) {
        try {
            Net.HttpRequest httpPost = new Net.HttpRequest(Net.HttpMethods.POST);
            httpPost.setUrl(url);
            httpPost.setContent(jsonStr);
            httpPost.setHeader(ContentType, "application/json; charset=utf-8");
            Gdx.net.sendHttpRequest(httpPost, new Net.HttpResponseListener() {
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    HttpStatus status = httpResponse.getStatus();
                    String result = httpResponse.getResultAsString();

                    if (status.getStatusCode() == HttpStatus.SC_OK && result.equals("OK")) {
                        addToLevelsList(levelLog.getLevel_num());
                        Gdx.app.log("PutUserService", " successful with code:" + status.getStatusCode());
                    } else {
                        levelLog.addToSendingInfo("status code: " + status.getStatusCode() + "| result: " + result + "#");

                        Gdx.app.log("PutUserService", "update user name services return code: " + String.valueOf(status.getStatusCode()));
                    }
                }

                public void failed(Throwable t) {
                    levelLog.addToSendingInfo("failed. t: " + t.getMessage() + "#");

                    String message = t.getMessage();
                    String status = "failed";
                }

                @Override
                public void cancelled() {
                    levelLog.addToSendingInfo("cancelled#");

                    String status = "failed";
                }
            });
        } catch (final Exception e) {
            levelLog.addToSendingInfo("e: " + e.getMessage() + "#");

            String status = "failed";
        }
    }

    public void sendGameData(Object requestObject) {
        if (!this.debugMode) {
            String gameDataStr = makeString(requestObject);
            String fileName = Constants.PREFIX_FILES_PATH + "/" +
                    CooperativeGame.gameData.getUser().getName()+ "-" +
                    CooperativeGame.gameData.getUser().getHuman_gender() + "/" + "Game_" +
                    CooperativeGame.gameData.getStart_time() + ".json";
            FileHandle file = Gdx.files.external(fileName);
            file.writeString(gameDataStr, false);

            sendPostRequest(gameDataStr, this.urlGameData, fileName);
        }
    }

    public void sendUser(User user) {
        if (!this.debugMode) {
            String userStr = makeString(user);
            String fileName = Constants.PREFIX_FILES_PATH + "/" +
                    CooperativeGame.gameData.getUser().getName()+ "-" +
                    CooperativeGame.gameData.getUser().getHuman_gender() + "/" + "User.json";
            FileHandle file = Gdx.files.external(fileName);
            file.writeString(userStr, false);

            sendPostRequest(userStr, this.urlUsersData, fileName);
        }
    }

    public void sendLevelLog(LevelLog levelLog) {
        if (!this.debugMode) {

            String levelLogStr = makeString(levelLog);
            sendPostRequestLevelCopied(levelLogStr, levelLog, this.urlLevelLog, ""); // ????
        }
    }


    public void sendAgain(){
        FileHandle dirHandle;
        String fileContent;

        // send again all the files that were not sent.
        dirHandle = Gdx.files.external(Constants.PREFIX_FILES_PATH);
        // iterate sub-directories
        for (FileHandle subDirHandle: dirHandle.list()) {
            // iterate files into sub-directories
            for (FileHandle fileHandle: subDirHandle.list()) {
                // files
                if(!(fileHandle.toString().contains("_sent"))) {
                    fileContent = fileHandle.readString();

                    if(fileHandle.toString().contains("Game")){
                        sendPostRequest(fileContent, urlGameData, fileHandle.toString());
                    }
                    else if(fileHandle.toString().contains("Level")){
                        sendPostRequest(fileContent, urlLevelLog, fileHandle.toString());
                    }
                    else if(fileHandle.toString().contains("User")){
                        sendPostRequest(fileContent, urlUsersData, fileHandle.toString());
                    }
                }
            }
        }
    }

    public int getNumOfUnsentFiles() {
        int numOfUnsentFiles = 0;

        FileHandle dirHandle;
        dirHandle = Gdx.files.external(Constants.PREFIX_FILES_PATH);

        for (FileHandle subDirHandle: dirHandle.list()) {
            // iterate files into sub-directories
            for (FileHandle fileHandle: subDirHandle.list()) {
                if(!(fileHandle.toString().contains("_sent"))) {
                    numOfUnsentFiles += 1;
                }
                else{
                    // bug writing?
                }
            }
        }
        return numOfUnsentFiles;
    }

    @Override
    public void sendRegistrationData(Object requestObject) {
        if (!this.debugMode) {
            String registrationDataStr = makeString(requestObject);
            sendPostRequest(registrationDataStr, this.urlRegisterUser, "");
        }
    }

    @Override
    public void sendEntranceData(Object requestObject) {
        if (!this.debugMode) {
            String registrationDataStr = makeString(requestObject);
            sendPostRequest(registrationDataStr, this.urlGameData, "");
        }
    }


    @Override
    public void sendLevelStart(Object requestObject) {
        if (!this.debugMode) {
            String registrationDataStr = makeString(requestObject);
            sendPostRequest(registrationDataStr, this.urlLevelStart, "");
        }
    }
}
