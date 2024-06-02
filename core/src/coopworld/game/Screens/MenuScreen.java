package coopworld.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import coopworld.game.CooperativeGame;
import coopworld.game.Screens.Login.UserRegistrationScreen;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;
import coopworld.game.Tools.MusicPlayer;

/*
 * Class Name: MenuScreen
 * implements screen, represents the menu screen with play, setting and about
 * options.
 */
public class MenuScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    final CooperativeGame game;
    private Texture texture;
    private MusicPlayer music;
    /* For position and size of buttons. */
    private float posWidth, posHeight, sizeWidth, sizeHeight;
    /* Create play button. */
    public void CreatePlayButton(){
        Texture texture = null;
        Skin skin = new Skin();

        texture = GraphicTools.getTexture(CooperativeGame.languagePrefix + "buttons/play.png");

        skin.add("play_button", texture);
        Image playButton = new Image(skin, "play_button");

        posWidth = 0.29f;
        posHeight = 0.03f;
        sizeWidth = 0.42f ;
        sizeHeight = 0.58f ;
        playButton.setPosition(posWidth * Constants.VIEWPORT_WIDTH, posHeight * Constants.VIEWPORT_HEIGHT);
        playButton.setSize(sizeWidth * Constants.VIEWPORT_WIDTH, sizeHeight * Constants.VIEWPORT_HEIGHT);

        playButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            music.uiSound();


            // TODO COOP2 - CHECK USER STATE
            if(CooperativeGame.gameData.getUser().getWas_registered().equals("Yes")){
                //game.setScreen(new LevelsScreen(game));
                game.setScreen(new LoadingScreen(game));
            }
            else {
                game.setScreen(new UserRegistrationScreen(game, "Menu"));
            }

            //game.setScreen(new StartCodeScreen(game)); // 3.6.21 changed.

            /*
            if(music.getUser_delete() != null){
                music.getUser_delete().dispose();
            }

            Gdx.input.getTextInput(checkCode(), "Please enter a valid code", "", "");
            */
            dispose();
            return true;
            }
        });
        /* Add play button to stage. */
        stage.addActor(playButton);

        //SettingsButton settingsButton = new SettingsButton(this, 0.03f,0.85f, stage, game, 0.08f, 0.08f);

        /*
        ExitButton exitButton = new ExitButton((float)0.9, (float)0.85, stage, (float)0.08,
                (float)0.08);
        */
        // PostButton should be created (in Menu) just if there are files to send.
        // ***** filesExists ***** should be true if there are files to send,
        // and false otherwise. (now it is false just for compilation).

        /*
        //TODO GWT - isFilesExists() - NOT ALLOWED IN HTML!
        boolean filesExists = Connection.isFilesExists();
        // create PostButton if needed.
        if(filesExists) {
            PostButton postButton = new PostButton((float) 0.03, (float) 0.85, stage, (float) 0.08,
                    (float) 0.08);
        }
        */
    }

    /**
     * Constructor with the game.
     *
     * @param  game - the game.
     *
     */
    public MenuScreen(CooperativeGame game){
        texture = GraphicTools.getTexture("menu_options/MainMenu.png");

        this.game = game;
        viewport = new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT,
                new OrthographicCamera());

        /*
        screenWidth = viewport.getWorldWidth();
        Constants.VIEWPORT_WIDTH = screenWidth;
        screenHeight = viewport.getWorldHeight();
        Constants.VIEWPORT_HEIGHT = screenHeight;
        */

        stage = new Stage(viewport, game.spriteBatch);
        music = MusicPlayer.getInstance();
        // create the play button.
        CreatePlayButton();

        //CooperativeGame.gameData.setUser_id(CooperativeGame.gameData.getUser().getUser_id());

        // create development mode icon if needed. It means that data will not be stored/sent.
        if(!CooperativeGame.developmentConfig.getData_storing()){
            //this.stage.addActor(getDevelopmentIcon());
        }

        // Visual indication of the version number.
        //this.stage.addActor(GraphicTools.getVerNumLabel(Constants.APK_VER_PSYCHOLOGISTS_REAL));

        // add user id num to stage (will show it on screen).
        // stage.addActor(getUserIdLabel());

        // draw the unique user_id.
        stage.addActor(GraphicTools.getUserIdLabel());
    }

    @Override
    public void show() {
        /* Get input from screen (for pressing buttons). */
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.spriteBatch.begin();
        game.spriteBatch.draw(texture, 0, 0, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        game.spriteBatch.end();
        stage.act();
        stage.draw();
    }
    // show this in development mode to represent that files will not be written (and sent).
    public Image getDevelopmentIcon(){
        Texture texture;
        Skin skin = new Skin();
        float hudWidth = 0.08f * Constants.VIEWPORT_WIDTH;

        /* Load image for background. */
        texture = GraphicTools.getTexture("menu_options/development_mode.png");

        skin.add("devImage", texture);
        Image devImage = new Image(skin, "devImage");
        devImage.setBounds(0.02f * Constants.VIEWPORT_WIDTH, 0.02f * Constants.VIEWPORT_HEIGHT,
                hudWidth, hudWidth);
        return devImage;
    }

    @Override
    public void resize(int width, int height) {
    }
    @Override
    public void pause() {
    }
    @Override
    public void resume() {
    }
    @Override
    public void hide() {
    }
    @Override
    public void dispose() {
        stage.dispose();
    }
}