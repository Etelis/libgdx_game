package coopworld.game.Tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import coopworld.game.CrossPlatform.GeneralUtils;

public class PopUp extends Image {
    private long secToDisplay;
    private long startTime;
    private boolean finished;
    private GeneralUtils utils;
    private boolean combined = false;
    Texture texture;
    private boolean minimalTimeHasPassed = false;
    // X seconds - just after this time the listener will do its action
    // (ignores accidental clicks after a little time).
    private long minimalTime;

    public PopUp(String popUpPath, float secToDisplay, float xPos, float yPos, float width,
                 float height, GeneralUtils utils, boolean combined, long minimalDisplayTime) {
        if(minimalDisplayTime == 0){
            this.minimalTime = (long)(1000000000L * 1f);
        }
        else{
            this.minimalTime = (1000000000L * minimalDisplayTime);
        }

        if(secToDisplay == -1){
            this.secToDisplay = -1;
        }
        else{
            this.secToDisplay = (long)(1000000000L * secToDisplay);
        }
        this.finished = false;
        this.utils = utils;

        Skin skin = new Skin();

        texture = GraphicTools.getTexture(popUpPath);

        if(combined){
            float scale = (float)texture.getHeight()/(float)texture.getWidth();
            height = width * scale;
        }

        skin.add("pop_up", texture);

        this.setDrawable(skin, "pop_up");
        if(height == -1){
            height = Utils.getHeight(this, width);
        }
        this.setBounds(xPos, yPos, width, height);
    }

    @Override
    public void act(float delta)
    {
        if(secToDisplay != -1){
            if(utils.getNanoTime() - startTime >= secToDisplay){
                this.finished = true;
                this.remove();
            }
        }

        if(!minimalTimeHasPassed){
            if(checkIfMinimalTimePassed()){
                minimalTimeHasPassed = true;
            }
        }

        super.act(delta);
    }

    // important to call it in GameScreen just before the popUp displaying.
    public void init(){
        this.startTime = utils.getNanoTime();
        this.finished = false;
    }

    public boolean finished(){
        return this.finished;
    }

    public void setFinished(boolean b){
        this.finished = b;
    }

    public boolean checkIfMinimalTimePassed(){
        if(utils.getNanoTime() - startTime >= minimalTime){
            return true;
        }
        return false;
    }

    public boolean isMinimalTimePassed() {
        return minimalTimeHasPassed;
    }
}