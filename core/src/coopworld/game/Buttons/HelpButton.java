package coopworld.game.Buttons;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;

/*
 * Class Name: PauseButton
 * implements GameButton, represents the pause button.
 */
public class HelpButton extends GameButton{
    public TextButton button;
    final GameScreen gameScreen;
    @Override
    public void createButton() {
        String path = "buttons/" + gameScreen.virtualGenderPath + "help.pack";
        buttonsAtlas = GraphicTools.getTextureAtlas(path);

        buttonSkin = new Skin();
        /* Skins for on and off. */
        buttonSkin.addRegions(buttonsAtlas);
        /* Font and style for pause. */
        font = new BitmapFont();
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = buttonSkin.getDrawable("help");
        style.down = buttonSkin.getDrawable("help");
        style.font = font;
        button = new TextButton("", style);

        /* Position and size. */
        float posWidth = (float) (0.82);
        float posHeight = (float) (0.68);
        button.setPosition(Constants.VIEWPORT_WIDTH * xPosition, Constants.VIEWPORT_HEIGHT *
                yPosition);
        button.setSize(widthSize * Constants.VIEWPORT_WIDTH, heightSize *
                Constants.VIEWPORT_WIDTH);

        //Constants.VIEWPORT_WIDTH
        //button.debug();
        stage.addActor(button);
        /* Add listener to pause button. */
        button.addListener(new ClickListener() {
            /*if clicked pause the game,change its state and display resume button*/
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            //if(gameScreen.getState() != GameScreen.State.Paused){
                // todo - CHECK!
                gameScreen.humanAsksForHelp++;
                gameScreen.waitForHelpButtonTouch = false;
                gameScreen.ChangeStateHelp();
                gameScreen.helpButton.remove();
            //}
            return true;
            }
        });
    }
    /**
     * Constructor with the game,background,string of regular/ice world image. *
     * @param  gameScreen - GameScreen object.
     * @param  xPosition - x coordinate of back button
     * @param  yPosition - y coordinate of back button
     * @param stage - a stage
     * @param widthSize - width size of button
     * @param heightSize  -  height size of button
     *
     */

    public HelpButton(final GameScreen gameScreen, final float xPosition, final float yPosition,
                       Stage stage, final float widthSize, final float heightSize){
        this.stage = stage;
        this.gameScreen = gameScreen;
        this.widthSize = widthSize;
        this.heightSize = heightSize;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        /* Create PAUSE button. */
        createButton();
    }
}
