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
 *  Class Name: VolumeButton
 *  implements GameButton, represents the Volume Button button,enable to control
  * the volume.
 */
public class VolumeButton extends GameButton{
    final GameScreen gameScreen;

    private boolean volumeOn = true;
    public TextButton volumeButton;
    final TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
    final TextButton.TextButtonStyle styleMute = new TextButton.TextButtonStyle();

    private boolean soundIsOn;
    //private TextButton button;

    /* Create volume button. */
    @Override
    public void createButton() {
        /* Button atlas image. */
        buttonsAtlas = GraphicTools.getTextureAtlas("buttons/volume.pack");

        buttonSkin = new Skin();
        /* Skins for on and off */
        buttonSkin.addRegions(buttonsAtlas);
        /* Font */
        font = new BitmapFont();
         /* Button properties */
        style.up = buttonSkin.getDrawable("volume");
        style.down = buttonSkin.getDrawable("volume");
        style.font = font;

        styleMute.up = buttonSkin.getDrawable("volume_mute");
        styleMute.down = buttonSkin.getDrawable("volume_mute");
        styleMute.font = font;

        final TextButton.TextButtonStyle startStyle;
        if(soundIsOn){
            startStyle = style;
        }
        else{
            startStyle = styleMute;
        }

        volumeButton = new TextButton("", startStyle);
        volumeButton.setPosition(xPosition * Constants.VIEWPORT_WIDTH,
                yPosition * Constants.VIEWPORT_HEIGHT);
        volumeButton.setSize(widthSize * Constants.VIEWPORT_WIDTH,
                heightSize * Constants.VIEWPORT_WIDTH);

        /* Add volume button to stage*/
        stage.addActor(volumeButton);
        volumeButton.addListener(new ClickListener() {
            // when clicked - set volume off if volume was on, otherwise set it on.
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                String timeStamp = ((CooperativeGame) Gdx.app.getApplicationListener()).
                        crossPlatformObjects.getUtils().getTimeStamp();
                // switch sound status
                if(gameScreen.music.switchSoundMode()){ // now sound is on
                    gameScreen.getMute_events().add(new OnOffEvent(true, timeStamp));
                    volumeButton.setStyle(style);
                }
                else{ // now sound is mute
                    gameScreen.getMute_events().add(new OnOffEvent(false, timeStamp));
                    volumeButton.setStyle(styleMute);
                }
                return true;
            }
        });
    }

    /* Return volume button. */
    public TextButton getButton(){
        return this.volumeButton;
    }
    /**
     * Constructor with x and y coordinates of button, stage and game
     * @param  xPosition - x coordinate of back button
     * @param  yPosition - y coordinate of back button
     * @param stage - a stage
     *
     */

    public VolumeButton(final GameScreen gameScreen, final float xPosition, final float
            yPosition, Stage stage, final float widthSize,
                        final float heightSize, boolean soundIsOn){

        this.stage = stage;
        this.gameScreen = gameScreen;
        this.widthSize = widthSize;
        this.heightSize = heightSize;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.soundIsOn = soundIsOn;
        /* Create PAUSE button. */
        createButton();
    }
}
