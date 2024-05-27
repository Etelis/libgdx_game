package coopworld.game.Networking.ResponseListeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;

import coopworld.game.CooperativeGame;
import coopworld.game.Logs.Enums;
import coopworld.game.Screens.DeadEndScreen;
import coopworld.game.Screens.MenuScreen;
import coopworld.game.Screens.StartCodeScreen;
import coopworld.game.Tools.Constants;

public class WebsiteStatusListener implements Net.HttpResponseListener {
    protected final CooperativeGame game;

    public WebsiteStatusListener(CooperativeGame game) {
        this.game = game;
    }

    @Override
    public void handleHttpResponse(Net.HttpResponse httpResponse) {
        HttpStatus status = httpResponse.getStatus();
        if (status.getStatusCode() == HttpStatus.SC_OK || status.getStatusCode() == HttpStatus.SC_NO_CONTENT) {
            Gdx.app.log("PutUserService", " successful with code:" + status.getStatusCode());
            String response = httpResponse.getResultAsString();

            if(response.contains("TOO_MANY_CON")){
                tooManyConnections();
            }
            else if(response.contains("ON")){
                serverIsOn();
            }
            else if (response.contains("OFF")){
                serverIsOff();
            }
            else if (response.contains("status_error")){
                problemWithStatus();
            }
            else{
                problemWithStatus();
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

    @Override
    public void failed(Throwable t) {

    }

    @Override
    public void cancelled() {

    }

    public void tooManyConnections(){
        game.setScreen(new DeadEndScreen(game, CooperativeGame.languagePrefix + "screens/website_status/too_many_connections.png"));
    }

    public void serverIsOn(){
        // no user_id in the link - ask to it in a dedicated screen.
        game.setScreen(new StartCodeScreen(game));
    }

    public void serverIsOff(){
        game.setScreen(new DeadEndScreen(game, CooperativeGame.languagePrefix + "screens/website_status/server_is_off.png"));
    }

    public void problemWithStatus(){
        game.setScreen(new DeadEndScreen(game, CooperativeGame.languagePrefix + "screens/website_status/status_error.png"));
    }
}
