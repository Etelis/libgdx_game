package coopworld.game.Networking.ResponseListeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;

import coopworld.game.CooperativeGame;
import coopworld.game.Screens.DeadEndScreen;
import coopworld.game.Screens.MenuScreen;

public class StartCodeListener implements Net.HttpResponseListener {
    protected final CooperativeGame game;

    public StartCodeListener(CooperativeGame game) {
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
            else if (response.contains("confirmed")){
               confirmedAction();
            }
            // user already exist!
            else if (response.contains("reused")){
                unidentifiedUserAction();
            }
            else{
                problemWithCode();
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

    public void confirmedAction(){
        game.setScreen(new MenuScreen(game));

        /*
        // TODO COOP2 - CHECK USER STATE
        if(CooperativeGame.gameData.getUser().getWas_registered().equals("Yes")){
            //game.setScreen(new LevelsScreen(game));
            game.setScreen(new LoadingScreen(game));
        }
        else{
            game.setScreen(new UserRegistrationScreen(game, "Menu"));
        }
         */
    }

    @Override
    public void failed(Throwable t) {

    }

    @Override
    public void cancelled() {

    }

    public void unidentifiedUserAction(){
        game.setScreen(new DeadEndScreen(game, CooperativeGame.languagePrefix + "screens/start_code/invalid_start_code.png"));
    }

    public void problemWithCode(){
        game.setScreen(new DeadEndScreen(game, CooperativeGame.languagePrefix + "screens/start_code/problem_with_code.png"));
    }
}
