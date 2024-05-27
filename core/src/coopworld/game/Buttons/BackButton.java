package coopworld.game.Buttons;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import coopworld.game.CooperativeGame;
import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Screens.Instructions;
import coopworld.game.Screens.LevelsScreen;
import coopworld.game.Screens.Login.UserRegistrationScreen;
import coopworld.game.Screens.MenuScreen;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;
import coopworld.game.Tools.MusicPlayer;

/*
 * Class Name: BackButton
 * implements GameButton, represents the back button.
 */
public class BackButton extends GameButton {
    final CooperativeGame game;
    private final Screen screen;
    public MusicPlayer music;

    /* Create BACK button, when pressed go to menu screen. */
    @Override
    public void createButton() {
        /* Button atlas image. */
        buttonsAtlas = GraphicTools.getTextureAtlas("buttons/back.pack");

        buttonSkin = new Skin();
        /* Skins for on and off. */
        buttonSkin.addRegions(buttonsAtlas);
        /* Font */
        font = new BitmapFont();
        music = MusicPlayer.getInstance();
        /* Button properties. */
        TextButton.TextButtonStyle styleBACK = new TextButton.TextButtonStyle();
        styleBACK.up = buttonSkin.getDrawable("back");
        styleBACK.down = buttonSkin.getDrawable("back");
        /* Button text and style. */
        styleBACK.font = font;
        button = new TextButton("", styleBACK);
        /* Set position and size. */
        button.setPosition(Constants.VIEWPORT_WIDTH * xPosition, Constants.VIEWPORT_HEIGHT *
                yPosition);
        button.setSize(widthSize * Constants.VIEWPORT_WIDTH, heightSize *
                Constants.VIEWPORT_WIDTH);
        stage.addActor(button);

        /* Add listener to BACK button. */
        button.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if(screen != null) {
                    music.uiSound();
                    //screen.dispose();
                    if(screen instanceof GameScreen || screen instanceof Instructions) {
                        MusicPlayer.getInstance().stopMusic();
                        game.setScreen(new LevelsScreen(game));
                    }
                    else if((screen instanceof Instructions)
                            ||(screen instanceof LevelsScreen)) {
                        game.setScreen(new MenuScreen(game));
                    }
                    else if(screen instanceof UserRegistrationScreen){
                        String lastScreen = ((UserRegistrationScreen)(screen)).getLastScreen();
                        if(lastScreen.equals("Menu")){
                            game.setScreen(new MenuScreen(game));
                        }
                        // shouldn't occur... TODO 2024 CHECK! why?
                        else{
                            game.setScreen(new MenuScreen(game));
                        }
                    }
                }
                return true;
            }
        });
    }

    /**
     * constructor with gameScreen, x and y coordinates of button, stage ,game
     * width and height of button.
     * @param  screen - screen
     * @param  xPosition - x coordinate of back button
     * @param  yPosition - y coordinate of back button
     * @param stage - a stage
     * @param game - the game
     * @param widthSize - width size of button
     * @param heightSize  -  height size of button
     *
     */
    public BackButton(final Screen screen, float xPosition, float yPosition, Stage stage,
                      final CooperativeGame game, float widthSize, float heightSize){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.stage = stage;
        this.game = game;
        this.screen = screen;
        this.widthSize = widthSize;
        this.heightSize = heightSize;
        /* Create back button. */
        createButton();
    }
}