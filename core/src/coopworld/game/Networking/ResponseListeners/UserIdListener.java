package coopworld.game.Networking.ResponseListeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Json;

import java.io.IOException;

import coopworld.game.CooperativeGame;
import coopworld.game.Logs.User;
import coopworld.game.Screens.DeadEndScreen;
import coopworld.game.Screens.MenuScreen;
import coopworld.game.Screens.StartCodeScreen;
import coopworld.game.Tools.Constants;

public class UserIdListener implements Net.HttpResponseListener {
    protected final CooperativeGame game;

    public UserIdListener(CooperativeGame game) {
        this.game = game;
    }

    @Override
    public void handleHttpResponse(Net.HttpResponse httpResponse) {
        HttpStatus status = httpResponse.getStatus();
        if (status.getStatusCode() == HttpStatus.SC_OK || status.getStatusCode() == HttpStatus.SC_NO_CONTENT) {
            Gdx.app.log("PutUserService", " successful with code:" + status.getStatusCode());
            String response = httpResponse.getResultAsString();
            // user doesn't exists.
            if(response.contains("unidentified")){
                unidentifiedUserAction();
            }
            // valid and new code :) let's start!
            else if (response.contains("user_id")){
                confirmedAction(response);
            }
            else{
                //errors.add("ResponseListener - Start Code response wasn't valid.");
            }
        }
        else{
            String error = "@ responseListenerStartCode-> status " + status.getStatusCode() + ". ";
            if(httpResponse != null){
                if(httpResponse.getResultAsString() != null){
                    error += httpResponse.getResultAsString();
                }
            }
            //errors.add(error);
        }
    }

    public void confirmedAction(String response){
        // TODO COOP2 - CHECK USER STATE

        Json json = new Json();
        User user = json.fromJson(User.class, response);
        user.setGenders();
        CooperativeGame.gameData.setUser(user);

        if (user.getExperimenter().equals("Students-22-School") || (user.getExperimenter().equals("Intervention-Lotem"))){
            Constants.IS_INTERVENTION = true;
            Constants.LEVEL_LENGTH = Constants.LEVEL_LENGTH_INTERVENTION;

        }
        else{
            Constants.IS_INTERVENTION = false;
            Constants.LEVEL_LENGTH = Constants.LEVEL_LENGTH_REGULAR;
        }

        Constants.LEVEL_LENGTH_EXTENDED = Constants.LEVEL_LENGTH + (int)Constants.HELP_REQUESTS_INTERVAL; // in seconds

        if(Constants.MASTER_ACCESS){
            user.reset();
        }

        // TODO 2022 - CHECK THIS!
        if(Constants.ENTRANCE_DATA != null) {
            Constants.ENTRANCE_DATA.setUser_id(Constants.USER_ID_WEB);
            CooperativeGame.conn.sendEntranceData(Constants.ENTRANCE_DATA);
        }

        game.setScreen(new MenuScreen(game)); // already entered user_id in this case.
    }

    public void unidentifiedUserAction(){
        game.setScreen(new DeadEndScreen(game, CooperativeGame.languagePrefix + "screens/invalid_code.png"));
    }

    @Override
    public void failed(Throwable t) {

    }

    @Override
    public void cancelled() {

    }
}
