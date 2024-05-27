package coopworld.game.Scenes;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.DelayedRemovalArray;

import coopworld.game.Screens.GameScreens.GameScreen;

public class BasicAnimatedImage extends Image
{
    protected Animation<TextureRegion> animation = null;
    private float stateTime = 0;
    private boolean finished = false;
    private int times;
    private int currIter;
    private GameScreen gameScreen;
    private boolean isIceThrowing;

    public BasicAnimatedImage(Animation<TextureRegion> animation, int times, boolean isIceThrowing, GameScreen gameScreen) {
        super(animation.getKeyFrame(0));
        this.animation = animation;
        resetAnimation();
        this.times = times;
        this.currIter = 0;
        this.gameScreen = gameScreen;
        this.isIceThrowing = isIceThrowing;
    }

    @Override
    public void act(float delta)
    {
        if(animation.isAnimationFinished(stateTime)){
            //System.out.println("ANI - finished, iteration: " + currIter);
            currIter++;
            if(currIter == times){
                //System.out.println("ANI - removed, iteration: " + currIter);
                remove();
                currIter = 0;

                if(isIceThrowing){
                    gameScreen.humanAskState = GameScreen.HumanAskState.HelpRequestAccepted;
                }
                DelayedRemovalArray<EventListener> listeners = getListeners();
                int size = listeners.size;
                for(EventListener eventListener : getListeners()){
                    eventListener.handle(new Event());
                }
                return;
            }
            else{
                resetAnimation();
            }
        }
        ((TextureRegionDrawable) getDrawable()).setRegion(animation.getKeyFrame(stateTime += delta, false));
        super.act(delta);
    }

    public void resetAnimation(){
        this.stateTime = 0;
    }
}