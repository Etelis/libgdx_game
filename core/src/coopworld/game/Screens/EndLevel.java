package coopworld.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.IOException;

import coopworld.game.Buttons.PlayButton;
import coopworld.game.CooperativeGame;
import coopworld.game.Logs.LevelLog;
import coopworld.game.Logs.OnOffEvent;
import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;
import coopworld.game.Tools.InputProcessors.InputProcessorGameScreen;
import coopworld.game.Tools.InputProcessors.InputProcessorLevelLogSending;
import coopworld.game.Tools.MusicPlayer;

/**
 * Created by User on 20/12/2016.
 */
public class EndLevel implements Screen
{
    private Viewport viewport;
    private Stage stage;
    final CooperativeGame game;
    private Texture texture;
    int score;
    int calledLevel;
    public MusicPlayer music;

    private float screenWidth  = Constants.VIEWPORT_WIDTH;
    private float screenHeight = Constants.VIEWPORT_HEIGHT;

    long startTime = 0;
    private float waitingTime = 5f;

    private boolean buttonIsOn;

    private LevelLog levelLog;

    private int sendingTries;

    private Image noWifiButton;
    private boolean wifiButtonIsOn;
    private boolean startPhase;
    private boolean cheatPressed;
    InputMultiplexer inputMultiplexer;

    private Image wifiMessageImg;

    public EndLevel(final CooperativeGame game, String bgPath, final int calledLevel, int score,
                    int numOfStars, LevelLog levelLog){
        /* Set screen background. */
        /* Load image for background. */
        startTime = System.currentTimeMillis();
        buttonIsOn = false;

        texture = GraphicTools.getTexture(bgPath);
        noWifiButton = GraphicTools.getNoWiFi();
        wifiButtonIsOn = false;
        setWifiButtonListener();
        startPhase = true;
        this.cheatPressed = false;

        this.game = game;
        this.score = score;
        this.calledLevel = calledLevel; // TODO 22 - needed?
        this.levelLog = levelLog;
        this.sendingTries = 0;

        viewport = new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT,
                new OrthographicCamera());
        stage = new Stage(viewport, game.spriteBatch);

        stage.addActor(levelsView());
        stage.addActor(showScoreAsText(0.48f, 0.36f, Constants.VIEWPORT_WIDTH * (1.8f / 1280f)));

        wifiMessageImg = getWifiErrorMessage();

        //float ratio = 1.2f;
        //PlayButton playButton = new PlayButton(0.42f, 0.16f, stage, game, ratio * 0.1216f);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new InputProcessorLevelLogSending(this));
        Gdx.input.setInputProcessor(inputMultiplexer);

        music = MusicPlayer.getInstance();
        this.music.levelCompleted();

        // "cheat" level - do not save...
        // TODO COOP2 - WHAT IS THIS?
        if(CooperativeGame.gameData.getUser().getLevels_played() + 1 < calledLevel){
            return;
        }
    }

    public Label showScoreAsText(float x, float y, float fontSize){
        BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/myFont.fnt"));
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);
        Label.LabelStyle textStyle = new Label.LabelStyle();;
        textStyle.fontColor = new Color(87f/255f, 103f/255f, 139f/255f, 1);
        textStyle.font = font;
        int score_to_show;
        if(!(CooperativeGame.gameData.getGame_params().isResetsScore()) && calledLevel != 1){
            score_to_show = CooperativeGame.gameData.getCumulativeScores().getHuman_player_score();
        }
        else{
            score_to_show = this.score;
        }
        Label scoreRealLabel = new Label(Integer.toString(score_to_show), textStyle);
        scoreRealLabel.setPosition(x * Constants.VIEWPORT_WIDTH, y * Constants.VIEWPORT_HEIGHT);
        scoreRealLabel.setFontScale(fontSize);
        scoreRealLabel.setColor(Color.WHITE);

        return scoreRealLabel;
    }

    // show the "level completed" and stars view.
    public Image levelsView(){
        String path = CooperativeGame.languagePrefix + "stars/3";

        if(GameScreen.newScore && CooperativeGame.gameData.getGame_params().isShow_high_score()){
            path += "_new_score.png";
        }
        else{
            path += ".png";
        }

        Image img = GraphicTools.getButtonPNG(path);

        float xPos = 0.25f * Constants.VIEWPORT_WIDTH;
        float yPos =  0.02f * Constants.VIEWPORT_HEIGHT;
        float scale = 0.57f;
        float widthSize = (0.87f) * scale * Constants.VIEWPORT_WIDTH;
        float heightSize = scale * Constants.VIEWPORT_WIDTH;
        img.setPosition(xPos, yPos);
        img.setSize(widthSize, heightSize);

        return img;
    }

    @Override
    public void show() {
        /* Get input from screen. */
        //Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        /*
        if(!buttonIsOn){
            if(timeHasPassed()){
                float ratio = 1.2f;
                PlayButton playButton = new PlayButton(0.42f, 0.16f, stage, game, ratio * 0.1216f);
                buttonIsOn = true;
            }
        }
         */

        if(cheatPressed && !buttonIsOn){
            showContinueButton();
        }

        if(!buttonIsOn){
            if(startPhase){
                if(timeHasPassed()){
                    // if the level log was sent successfully.
                    if((CooperativeGame.conn.getSuccessfullySentLevels()).contains(calledLevel)){
                        showContinueButton();
                    }
                    // if the level log was NOT sent successfully.
                    else{
                        startTime = System.currentTimeMillis();
                        showWifiButton();
                    }
                }
            }
        }

        /* Draw screen. */
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.spriteBatch.begin();
        game.spriteBatch.draw(texture, 0, 0, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        game.spriteBatch.end();
        stage.draw();

    }

    public void showContinueButton(){
        float ratio = 1.2f;
        PlayButton playButton = new PlayButton(0.42f, 0.16f, stage, game, ratio * 0.1216f);
        buttonIsOn = true;
    }

    public void showWifiButton(){
        stage.addActor(noWifiButton);
        stage.addActor(wifiMessageImg);

        wifiButtonIsOn = true;

        startPhase = false;
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

    }

    public boolean timeHasPassed(){
        // check how much time has pass from the last virtual player ask.
        float passedTime = (int) ((System.currentTimeMillis() - startTime) / 1000);
        if(passedTime >= waitingTime){
            return true;
        }
        return false;
    }

    public void setWifiButtonListener(){
        noWifiButton.addListener(new ClickListener() {
            // when clicked - set volume off if volume was on, otherwise set it on.
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                // send level log and update relevant tries details.
                levelLog.setTries(levelLog.getTries() + 1);
                CooperativeGame.conn.sendLevelLog(levelLog);

                // remove the wifi button and message.
                noWifiButton.remove();
                wifiMessageImg.remove();
                wifiButtonIsOn = false;



                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        if((CooperativeGame.conn.getSuccessfullySentLevels()).contains(calledLevel)){
                            showContinueButton();
                        }
                        // if the level log was NOT sent successfully.
                        else{
                            showWifiButton();
                        }
                    }
                }, waitingTime);


                return true;
            }
        });
    }

    public void setCheatPressed(boolean cheatPressed) {
        this.cheatPressed = cheatPressed;
    }


    public Image getWifiErrorMessage(){
        Image wifiMessage = GraphicTools.getButtonPNG("wifi_obligation/wifi_message.png");
        wifiMessage.setPosition(0.33f * Constants.VIEWPORT_WIDTH, 0.12f * Constants.VIEWPORT_HEIGHT);
        wifiMessage.setWidth(0.33f * Constants.VIEWPORT_WIDTH);
        wifiMessage.setHeight(GraphicTools.getHeight(wifiMessage, wifiMessage.getWidth()));

        return wifiMessage;
    }
}
