package coopworld.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import coopworld.game.CooperativeGame;
import coopworld.game.CrossPlatform.GeneralUtils;

public class IndicationPopUpScreen extends Group {
    private String popUpPath;
    private String blurryScreenPath = "screens/blurry_screen.png";
    private GeneralUtils utils;

    private long msToDisplay;
    private long startTime;

    private boolean minimalTimeHasPassed;

    public IndicationPopUpScreen(String popUpPath) {
        this.popUpPath = popUpPath;
        this.utils = ((CooperativeGame) Gdx.app.getApplicationListener()).
                crossPlatformObjects.getUtils();
        startTime = utils.getNanoTime();
        this.msToDisplay = (long)(1000000000L * 1.2f);
        this.minimalTimeHasPassed = false;

        // generate blurry screen and popups.
        float screenWidth = Constants.VIEWPORT_WIDTH;
        float screenHeight = Constants.VIEWPORT_HEIGHT;
        Image screenImg = GraphicTools.getButtonPNG(blurryScreenPath);
        screenImg.setPosition(0, 0);
        screenImg.setSize(0, 0);
        screenImg.setSize(screenWidth, screenHeight);
        this.addActor(screenImg);

        float popUpWidth = 0.5f * screenWidth;
        Image popupImg = GraphicTools.getButtonPNG(popUpPath);
        popupImg.setWidth(popUpWidth);
        float popUpHeight = Utils.getHeight(popupImg, popUpWidth);
        popupImg.setHeight(popUpHeight);
        popupImg.setPosition((screenWidth/2) - (popUpWidth/2), (screenHeight/2) - (popUpHeight/2));
        this.addActor(popupImg);
    }

    @Override
    public void act(float delta)
    {
        if(!minimalTimeHasPassed){
            if(checkIfMinimalTimePassed()){
                minimalTimeHasPassed = true;

                for(EventListener eventListener : getListeners()){
                    eventListener.handle(new ChangeListener.ChangeEvent());
                }
            }
        }

        super.act(delta);
    }

    public boolean checkIfMinimalTimePassed(){
        if(utils.getNanoTime() - startTime >= msToDisplay){
            return true;
        }
        return false;
    }
}