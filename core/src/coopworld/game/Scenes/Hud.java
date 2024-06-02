package coopworld.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import coopworld.game.CooperativeGame;
import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;
import coopworld.game.Tools.PopUp;

import static coopworld.game.Screens.GameScreens.GameScreen.levelNum;

/*
 * Class Name: Hud
 * implements Disposable, keeps time and score.
 */
public class Hud implements Disposable {
    public FitViewport viewPort;
    public Stage stage;
    TextureAtlas atlas;
    public AnimatedImage animatedImage, scoreAnimation, coinAnimation;
    public Label scoreRealLabel;
    public boolean showTimer;
    boolean scoreAnimationIsOn = true;

    public float fontSize;
    float fontSizeDefault;
    float fontSizeSpecial;

    //Size ratio for watch and scores elements.
    float screenHeight = Constants.VIEWPORT_HEIGHT;
    float screenWidth = Constants.VIEWPORT_WIDTH;

    float hudWidth = (float)0.13 * screenWidth;
    float hudHeight = (float)0.1 * screenHeight;

    float timeScoreAnimation = 0.0f;
    Animation<TextureRegion> animation;

    public Label.LabelStyle currTextStyle;
    private Label.LabelStyle textStyle;
    private Label.LabelStyle textStyleSpecial;
    private Label.LabelStyle textStyleSpecial_decreaseScore;
    BitmapFont font;

    // TIMER //
    private Label stopwatchDisplay;
    private float deltaTime = 0;
    ////////////////////////////

    GameScreen gameScreen;

    boolean scoreIncreased = false, scoreDecreased = false;

    float timer = 0.0f;

    public static PopUp visualScoreDecreasing, visualScoreIncreasingSuper, visualScoreIncreasing;
    AnimationContainer coinDecreasingContainer;

    float visualScoreDecreaseX, visualDecreaseW;

    boolean isInstructionLevel;

    private float lastMeasureTime = 0.31f;
    private float timeUntilTraining = 0.3f;

    public Hud(GameScreen gameScreen){
        this.gameScreen = gameScreen;
        showTimer = CooperativeGame.gameData.getGame_params().isTimer_based();
        if(levelNum == 1){
            showTimer = false;
        }
        // create the watch only if it is not an instruction level, and if it is a a timer
        // oriented level (timerIsOn is true).
        // todo - in the constructor!
        //showTimer = !(isInstructionLevel) && timerIsOn;

        fontSize = Constants.VIEWPORT_WIDTH * (1.5f / 1280f);
        fontSizeDefault = fontSize;
        fontSizeSpecial = 1.3f * fontSize;

        this.font = new BitmapFont(Gdx.files.internal("fonts/myFont.fnt"), false);
        this.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);

        this.viewPort = new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT,
                new OrthographicCamera());
        /* Stage is like a box. */
        this.stage = new Stage();
        Table table = new Table();
        /* Put the table in the top of our stage.*/
        table.top();
        /* The table = the size of the stage. */
        table.setFillParent(true);

        /* stopwatch and display scores */
        textStyle = new Label.LabelStyle();
        textStyle.fontColor = new Color(87f/255f, 103f/255f, 139f/255f, 1);
        textStyle.font = font;

        textStyleSpecial = new Label.LabelStyle();
        textStyleSpecial.fontColor = new Color(190f/255f, 70f/255f, 58f/255f, 1);
        textStyleSpecial.font = font;

        textStyleSpecial_decreaseScore = new Label.LabelStyle();
        textStyleSpecial_decreaseScore.fontColor = new Color(39f/255f, 84f/255f, 91f/255f, 1);
        textStyleSpecial_decreaseScore.font = font;

        currTextStyle = textStyle;
        fontSize = Constants.VIEWPORT_WIDTH * (1.5f / 1280f);
        fontSizeDefault = fontSize;
        fontSizeSpecial = 1.3f * fontSize;

        buildScoreAnimation();

        if(showTimer){
            CreateStopwatch();
        }
        visualDecreaseW = 0.05f * Constants.VIEWPORT_WIDTH;
        visualScoreDecreaseX = 0.11f * Constants.VIEWPORT_WIDTH;

        // todo - doesnt relevant. no watch now.
        /*
        if(isInstructionLevel){
            visualScoreDecreaseX = 0.11f * Constants.VIEWPORT_WIDTH;
        }
        else{
            visualScoreDecreaseX = 0.25f * Constants.VIEWPORT_WIDTH;
        }
        */
        //help cost for human player.
        int helpCost = CooperativeGame.gameData.getGame_params().getReciprocityValues().getHelp_providing_cost_human();
        // pop up to visual explanation of score decreasing (-1).
        visualScoreDecreasing = new PopUp("pay/-" + helpCost +".png", 1.5f,
                visualScoreDecreaseX, 0.81f * Constants.VIEWPORT_HEIGHT,
                visualDecreaseW, visualDecreaseW, ((CooperativeGame)Gdx.app.getApplicationListener()).
                crossPlatformObjects.getUtils(), false, 0);


        int specialCoinValue = CooperativeGame.gameData.getGame_params().getReciprocityValues().getLocked_virtual_coin_value();
        // pop up to visual explanation of score increasing as a result of blocked coin collecting
        // (+3).
        visualScoreIncreasingSuper = new PopUp("pay/+" + specialCoinValue +".png", 1.5f,
                visualScoreDecreaseX, 0.81f * Constants.VIEWPORT_HEIGHT,
                visualDecreaseW * 1.2f, visualDecreaseW * 1.2f, ((CooperativeGame)Gdx.app.getApplicationListener()).
                crossPlatformObjects.getUtils(), false, 0);

        visualScoreIncreasing = new PopUp("pay/+1.png", 0.5f,
                visualScoreDecreaseX, 0.81f * Constants.VIEWPORT_HEIGHT,
                visualDecreaseW, visualDecreaseW, ((CooperativeGame)Gdx.app.getApplicationListener()).
                crossPlatformObjects.getUtils(), false, 0);


        String path = "hud/bucket_coin.pack";
        atlas = GraphicTools.getTextureAtlas(path);

        Animation coinsDecreasingAnimation = new Animation<TextureRegion>(0.12f,
                atlas.findRegions("bucket_coin"), Animation.PlayMode.NORMAL);
        coinDecreasingContainer = new AnimationContainer(coinsDecreasingAnimation,
                CooperativeGame.gameData.getGame_params().getReciprocityValues().
                getHelp_providing_cost_human(), 0.4f, stage);
        // 0.4 is better to avoid the coin goes behind the bucket

        //stage.addActor(coinsDecreasing);

        Gdx.input.setInputProcessor(stage);
    }

    private void buildScoreAnimation(){
        //Texture Regions.
        // frames arrFay (contains TextureRegions).
        Array<TextureRegion> frames = new Array<TextureRegion>();

        //Texture Regions.
        // open pack image og ice girl player.
        atlas = GraphicTools.getTextureAtlas("hud/coin_animation.pack");

        frames.add(new TextureRegion(atlas.findRegion("10")));
        frames.add(new TextureRegion(atlas.findRegion("20")));
        frames.add(new TextureRegion(atlas.findRegion("30")));

        coinAnimation = new AnimatedImage(new Animation(0.3f, frames));

        animatedImage = coinAnimation;
        // todo - 0 instead of this.humanScore.
        scoreRealLabel = new Label(Integer.toString(gameScreen.getHumanScore()), currTextStyle);

        animatedImage.setBounds((float) 0.03 * screenWidth, (float) 0.89 * screenHeight,
                    hudWidth, hudHeight);
        coinAnimation.setBounds((float) 0.03 * screenWidth, (float) 0.89 * screenHeight,
                    hudWidth, hudHeight);
        scoreRealLabel.setPosition((float) 0.09 * screenWidth, (float) 0.92 * screenHeight);

        stage.addActor(animatedImage);
        scoreRealLabel.setFontScale(fontSizeDefault);
        scoreRealLabel.setColor(Color.WHITE);
        if(CooperativeGame.gameData.getGame_params().isShow_human_score()){
            stage.addActor(scoreRealLabel);
        }
    }

    public void updateScoreStyle(){
        scoreRealLabel.setStyle(currTextStyle);
        scoreRealLabel.setFontScale(fontSize);
    }

    public void increaseHumanScore(int value){
        if (value == 3){
            visualScoreIncreasingSuper.init();
            stage.addActor(visualScoreIncreasingSuper);
        }
        /*
        if (value == 1){
            visualScoreIncreasing.remove();
            visualScoreIncreasing.init();
            stage.addActor(visualScoreIncreasing);
        }
        */

        updateScoreStyle();
        scoreRealLabel.setText(Integer.toString(gameScreen.getHumanScore()));
        scoreIncreased = true;
        stage.draw();
    }

    public void showIncrease(int value){
        switch(value){
            case 1:
                visualScoreIncreasing.remove();
                visualScoreIncreasing.init();
                stage.addActor(visualScoreIncreasing);
                break;
            // todo - validness.
            default:
                visualScoreIncreasingSuper.init();
                stage.addActor(visualScoreIncreasingSuper);
        }

        updateScoreStyle();
        scoreRealLabel.setText(Integer.toString(gameScreen.getHumanScore()));
        scoreIncreased = true;
        stage.draw();
    }

    public void decreaseHumanScore(){
        updateScoreStyle();
        scoreRealLabel.setText(Integer.toString(gameScreen.getHumanScore()));
        scoreDecreased = true;
        visualScoreDecreasing.init();
        stage.addActor(coinDecreasingContainer);
        stage.addActor(visualScoreDecreasing);

        stage.draw();
    }

    private void CreateStopwatch() {
        Skin skin = new Skin();
        Texture texture = GraphicTools.getTexture("hud/blank_label.png");

        skin.add("blank_label", texture);
        Image watchImage = new Image(skin, "blank_label");

        watchImage.setBounds((float) 0.43 * screenWidth, (float) 0.89 * screenHeight,
                hudWidth, hudHeight);
        // Change remaining time to 5 - for presentation. Remember to change it in kids play.
        if(CooperativeGame.developmentConfig.getCheats().getClock()) {
            watchImage.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    // todo - CHEAT!
                    //cheat = true;
                    return true;
                }
            });
        }
        stage.addActor(watchImage);

        // textStyle = new Label.LabelStyle(new BitmapFont(new FileHandle("fonts/trebuchet.fnt")),Color.CORAL);
        stopwatchDisplay = new Label("00:00", textStyle);
        stopwatchDisplay.setPosition((float) 0.46 * screenWidth, (float) 0.915 * screenHeight);
        stopwatchDisplay.setFontScale(fontSize);
        stopwatchDisplay.setColor(Color.WHITE);
        stage.addActor(stopwatchDisplay);
    }

    public void updateStopWatchDisplay(int remainingTime){
        if(stopwatchDisplay != null) {
            stopwatchDisplay.setText(Integer.toString(remainingTime));
        }
    }

    public void updateStopWatchDisplay(String remainingTimeMinutesSeconds){
        if(stopwatchDisplay != null) {
            stopwatchDisplay.setText(remainingTimeMinutesSeconds);
        }
    }

    public void setIsInstructionLevel(boolean isInstructionLevel){
        this.isInstructionLevel = isInstructionLevel;

    }

    /* We get the dt from GameScreen, (from its update method too). */
    public void update(float dt){
        // todo - SUPPORT IT!
        if(scoreIncreased){
            timer += dt;
            // show text animation.
            if(timer < 0.8f){
                currTextStyle = textStyleSpecial;
                fontSize = fontSizeSpecial;
            }
            // time to text animation has passed. back to default values.
            else{
                currTextStyle = textStyle;
                scoreIncreased = false;
                fontSize = fontSizeDefault;
                timer = 0.0f;
            }
        }
        if(scoreDecreased){
            timer += dt;
            // show text animation.
            if(timer < 0.8f){
                currTextStyle = textStyleSpecial_decreaseScore;
                fontSize = 0.75f * fontSizeSpecial;
            }
            // time to text animation has passed. back to default values.
            else{
                currTextStyle = textStyle;
                scoreDecreased = false;
                fontSize = fontSizeDefault;
                timer = 0.0f;
            }
        }
    }

    public void startCoinAnimation(){
        animatedImage.setVisible(false);
        stage.addActor(coinAnimation);
        scoreRealLabel.toFront();
    }

    public Stage getStage(){
        return stage;
    }

    /* Dispose stage. */
    @Override
    public void dispose() {
        stage.dispose();
    }

    public String getWatchRemainingTime(){
        if(showTimer){
            return stopwatchDisplay.getText().toString();
        }
        else{
            return "NA";
        }
    }
}