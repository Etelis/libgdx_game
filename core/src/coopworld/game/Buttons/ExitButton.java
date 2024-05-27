package coopworld.game.Buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;
import coopworld.game.Tools.MusicPlayer;


/*
 * Class Name: ExitButton
 * implements GameButton, represents the exit button.
 */
public class ExitButton extends GameButton {
    private MusicPlayer music;

    @Override
    public void createButton() {
        /* Button atlas image. */
        buttonsAtlas = GraphicTools.getTextureAtlas("buttons/exit.pack");

        buttonSkin = new Skin();
        /* Skins for on and off. */
        buttonSkin.addRegions(buttonsAtlas);
        /* Font */
        font = new BitmapFont();
        /* Button properties. */
        TextButton.TextButtonStyle styleBACK = new TextButton.TextButtonStyle();
        styleBACK.up = buttonSkin.getDrawable("exit");
        styleBACK.down = buttonSkin.getDrawable("exit");
        /* Button text and style. */
        styleBACK.font = font;
        button = new TextButton("", styleBACK);
        /* Set position and size. */
        button.setPosition(Constants.VIEWPORT_WIDTH * xPosition, Constants.VIEWPORT_HEIGHT *
                yPosition);
        button.setSize(widthSize * Constants.VIEWPORT_WIDTH, heightSize *
                Constants.VIEWPORT_WIDTH);
        stage.addActor(button);
        // add listener to exit button.

        button.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                // todo server
                music.uiSound();
                Gdx.app.exit();
                //TODO COOP2 WEB GWT - REMOVED EXIT. ERROR: Line 65: The method exit(int) is undefined for the type System
                //System.exit(0);
                return true;
            }
        });
    }

    /**
     * Constructor with gameScreen, x and y coordinates of button, stage ,game
     * width and height of button.
     * @param  xPosition - x coordinate of back button
     * @param  yPosition - y coordinate of back button
     * @param stage - a stage
     * @param widthSize - width size of button
     * @param heightSize  -  height size of button
     *
     */
    public ExitButton(float xPosition, float yPosition, Stage stage, float widthSize, float heightSize){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.stage = stage;
        this.widthSize = widthSize;
        this.heightSize = heightSize;
        this.music = MusicPlayer.getInstance();
        /* Create back button. */
        createButton();
    }
}