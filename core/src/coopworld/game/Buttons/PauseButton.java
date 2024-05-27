package coopworld.game.Buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import coopworld.game.CooperativeGame;
import coopworld.game.Logs.OnOffEvent;
import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;

/*
 * Class Name: PauseButton
 * implements GameButton, represents the pause button.
 */
public class PauseButton extends GameButton{
    private Stage stage;
    final GameScreen gameScreen;

    private TextButton buttonPAUSE;

    final TextButton.TextButtonStyle styleResume = new TextButton.TextButtonStyle();
    final TextButton.TextButtonStyle stylePause = new TextButton.TextButtonStyle();
    private boolean isRunning = true;
    private boolean isOn = true;
    /* Create PAUSE button, when pressed pause the game. */
    @Override
    public void createButton() {
        /* Button atlas image. */
        buttonsAtlas = GraphicTools.getTextureAtlas("buttons/pauseAndResume.pack");

        buttonSkin = new Skin();
        /* Skins for on and off. */
        buttonSkin.addRegions(buttonsAtlas);
        /* Font and style for pause. */
        font = new BitmapFont();
        stylePause.up = buttonSkin.getDrawable("pause");
        stylePause.down = buttonSkin.getDrawable("pause");
        stylePause.font = font;
        /* Font and style for resume. */
        styleResume.up = buttonSkin.getDrawable("resume");
        styleResume.down = buttonSkin.getDrawable("resume");
        styleResume.font = font;
        buttonPAUSE = new TextButton("", stylePause);
        /* Position and size. */
        buttonPAUSE.setPosition(Constants.VIEWPORT_WIDTH * xPosition, Constants.VIEWPORT_HEIGHT *
                yPosition);
        buttonPAUSE.setSize(widthSize * Constants.VIEWPORT_WIDTH, heightSize *
                Constants.VIEWPORT_WIDTH);
        stage.addActor(buttonPAUSE);
        /* Add listener to pause button. */
        buttonPAUSE.addListener(new ClickListener() {
            /*if clicked pause the game,change its state and display resume button*/
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                String timeStamp = ((CooperativeGame) Gdx.app.getApplicationListener()).
                        crossPlatformObjects.getUtils().getTimeStamp();
                gameScreen.ChangeState(true);
                /* from Running to Paused. */
                if (isRunning) {
                    isRunning = false;
                    gameScreen.music.pauseMusic();

                    buttonPAUSE.setStyle(styleResume);
                    gameScreen.getPause_events().add(new OnOffEvent(true, timeStamp));
                }
                /* From Paused to Running. */
                else {
                    isRunning = true;

                    if(gameScreen.music.isSoundIsOn()){ // avoid replay music in mute mode.
                        gameScreen.music.playBackgroundMusic();
                    }

                    buttonPAUSE.setStyle(stylePause);
                    gameScreen.getPause_events().add(new OnOffEvent(false, timeStamp));
                }
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

    public PauseButton(final GameScreen gameScreen, final float xPosition, final float yPosition,
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

    public TextButton getButton() {
        return buttonPAUSE;
    }

    public void remove(){
        buttonPAUSE.remove();
        isOn = false;
    }

    public void add(){
        if(gameScreen.getState() == GameScreen.State.HelpAsk){
            return;
        }
        else{
            stage.addActor(buttonPAUSE);
            isOn = true;
        }
    }

    public boolean isOn() {
        return isOn;
    }
}
