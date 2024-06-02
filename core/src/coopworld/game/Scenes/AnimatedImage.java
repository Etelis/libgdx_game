package coopworld.game.Scenes;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimatedImage extends Image
{
    protected Animation<TextureRegion> animation = null;
    private float stateTime = 0;
    private boolean finished = false;

    public AnimatedImage(Animation<TextureRegion> animation) {
        super(animation.getKeyFrame(0));
        this.animation = animation;
    }

    @Override
    public void act(float delta)
    {
        if(animation.isAnimationFinished(stateTime)){
            finished = true;
        }
        ((TextureRegionDrawable) getDrawable()).setRegion(animation.getKeyFrame(stateTime += delta, true));
        super.act(delta);
    }

    public boolean hasFinished(){
        return finished;
    }

    public void resetAnimation(){
        this.stateTime = 0;
        this.finished = false;
    }
}