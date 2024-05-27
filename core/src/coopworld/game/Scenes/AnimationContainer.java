package coopworld.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;

import coopworld.game.CooperativeGame;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;

/**
 * Created by Chen on 12/12/2017.
 */

public class AnimationContainer extends Actor {
    Animation<TextureRegion> animation;
    int iters;
    double period;
    Stage stage;
    ArrayList<BasicAnimatedImage> animationArr;

    private long intervalSeconds;
    private long lastUpdateTime;
    private long currentTime;

    private int currIndex = 0;
    public AnimationContainer(Animation<TextureRegion> animation, int iters, double period, Stage stage) {
        this.animation = animation;
        this.iters = iters;
        this.period = period;
        this.stage = stage;

        this.intervalSeconds = (long)(1000000000L * this.period);
        this.lastUpdateTime = (long)(1000000000L * (this.period + 1));
        this.currentTime = 0;

        animationArr = new ArrayList<BasicAnimatedImage>();
        for(int i = 0; i < iters; i++){
            // TODO - NEED TO BE IN HUD CLASS.
            BasicAnimatedImage bai = new BasicAnimatedImage(animation, 1, false, null);
            bai.setPosition(0f * Constants.VIEWPORT_WIDTH,
                    0.73f * Constants.VIEWPORT_HEIGHT);
            bai.setWidth(0.12f * Constants.VIEWPORT_WIDTH);
            bai.setHeight(GraphicTools.getHeight(bai, bai.getWidth()));
            //bai.setScale(0.75f);
            /*
            bai.setPosition((float) 0.03 * Constants.VIEWPORT_WIDTH,
                    (float) 0.89 * Constants.VIEWPORT_HEIGHT);
            */
            animationArr.add(bai);
        }
    }

    @Override
    public void act(float delta) {
        if(isTimePassed()){
            if(currIndex == animationArr.size()){
                this.remove();
                currIndex = 0;
                return;
            }
            // continue animate!
            else{
                lastUpdateTime = ((CooperativeGame)Gdx.app.getApplicationListener()).crossPlatformObjects.getUtils().getNanoTime();
                BasicAnimatedImage actor = animationArr.get(currIndex);
                actor.resetAnimation();
                stage.addActor(actor);
                //actor.toBack();
                currIndex++;
            }
        }
        super.act(delta);
    }

    public boolean isTimePassed(){
        currentTime = ((CooperativeGame)Gdx.app.getApplicationListener()).crossPlatformObjects.getUtils().getNanoTime();
        if (currentTime - lastUpdateTime >= intervalSeconds) {
            return true;
        }
        return false;
    }
}
