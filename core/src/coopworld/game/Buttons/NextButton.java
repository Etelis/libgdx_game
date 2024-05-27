package coopworld.game.Buttons;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

import coopworld.game.CooperativeGame;
import coopworld.game.Scenes.AnimatedImage;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;

/*
 * Class Name: NextButton
 * implements GameButton, represents the Next button.
 */
public class NextButton extends GameButton {
    /* Images are used as skins of the BACK button. */
    private Skin buttonSkin;
    private TextButton button;
    private BitmapFont font;
    final CooperativeGame game;
    /* Create Next button, when pressed go to menu screen. */
    @Override
    public void createButton() {

    }

    /**
     * constructor with gameScreen, x and y coordinates of button, stage ,game
     * width and height of button.
     * @param  xPosition - x coordinate of back button
     * @param  yPosition - y coordinate of back button
     * @param stage - a stage
     * @param game - the game
     * @param widthSize - width size of button
     * @param heightSize  -  height size of button
     *
     */
    public NextButton(float xPosition, float yPosition, Stage stage,
                      final CooperativeGame game, float widthSize, float heightSize){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.stage = stage;
        this.game = game;
        this.widthSize = widthSize;
        this.heightSize = heightSize;
    }

    public AnimatedImage getButton(){
        /* Button atlas image. */
        buttonsAtlas = GraphicTools.getTextureAtlas(CooperativeGame.languagePrefix +
                "buttons/next.pack");

        Array<TextureRegion> frames = new Array<TextureRegion>();

        frames.add(new TextureRegion(buttonsAtlas.findRegion("1")));
        frames.add(new TextureRegion(buttonsAtlas.findRegion("2"))); // TODO ARABIC - NO NEED
        frames.add(new TextureRegion(buttonsAtlas.findRegion("3"))); // TODO ARABIC - NO NEED

        Animation animation = new Animation(0.3f, frames);
        AnimatedImage animatedImage = new AnimatedImage(animation);

        animatedImage.setBounds(xPosition * Constants.VIEWPORT_WIDTH,
                yPosition * Constants.VIEWPORT_HEIGHT, widthSize * Constants.VIEWPORT_WIDTH,
                heightSize * Constants.VIEWPORT_WIDTH);

        return animatedImage;
    }
}