package coopworld.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import coopworld.game.CooperativeGame;
import coopworld.game.Logs.Enums;
import coopworld.game.Logs.LevelParams;
import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;
import coopworld.game.Tools.MusicPlayer;

/*
 * Class Name: LevelsScreen
 * implements screen, represents levels view screen.
 */
public class LevelsScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    static CooperativeGame game = null;
    private Texture bgTexture;
    private float screenWidth  = Constants.VIEWPORT_WIDTH;
    private float screenHeight = Constants.VIEWPORT_HEIGHT;

    private Table table;
    TextureAtlas threeStarsAtlas, currLevelsAtlas, lockedAtlas;
    Skin threeStarsSkin, currLevelsSkin, lockedSkin;

    public static int currLevel;
    private MusicPlayer music;
    /**
     * Constructor with the game.
     *
     * @param  game - the game.
     *
     */
    public LevelsScreen(final CooperativeGame game) {
        this.currLevel = CooperativeGame.gameData.getUser().getLevels_played() + 1;
        this.table = new Table();

        // Load image for background.
        bgTexture = GraphicTools.getTexture(CooperativeGame.languagePrefix + "screens/levels_screen/level_select.png");

        this.game = game;
        viewport = new FitViewport(Constants.VIEWPORT_WIDTH,
                Constants.VIEWPORT_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((CooperativeGame) game).spriteBatch);
        this.music = MusicPlayer.getInstance();
        // show the cup with high score only if the configuration takes it into account.
        if(CooperativeGame.gameData.getGame_params().isShow_high_score()){
            addHighScoreIfNeeded();
        }

        threeStarsAtlas = GraphicTools.getTextureAtlas("levels/three_stars.pack");
        if(CooperativeGame.gameData.getUser().getHuman_gender() == Enums.Gender.Female) {
            currLevelsAtlas = GraphicTools.getTextureAtlas("levels/currNumbersGirl.pack");
        }
        else{
            currLevelsAtlas = GraphicTools.getTextureAtlas("levels/currNumbersBoy.pack");
        }
        lockedAtlas = GraphicTools.getTextureAtlas("levels/locked.pack");

        threeStarsSkin = new Skin();
        threeStarsSkin.addRegions(threeStarsAtlas);
        currLevelsSkin = new Skin();
        currLevelsSkin.addRegions(currLevelsAtlas);
        lockedSkin = new Skin();
        lockedSkin.addRegions(lockedAtlas);


        table.setFillParent(true);
        // TODO - PSY
        table.left().bottom();
        InsertButtonToTable(1);
        InsertButtonToTable(2);
        InsertButtonToTable(3);
        InsertButtonToTable(4);
        table.row();
        InsertButtonToTable(5);
        InsertButtonToTable(6);
        InsertButtonToTable(7);
        if(!(Constants.IS_INTERVENTION)){
            InsertButtonToTable(8);
        }
        table.row();

        table.setPosition(0.18f * Constants.VIEWPORT_WIDTH, 0.24f * Constants.VIEWPORT_HEIGHT);

        stage.addActor(table);

        // if it is a continual game - show the user name.
        if(CooperativeGame.gameData.getGame_params().getContinuity()
                == Enums.GameContinuity.Continual) {
            //stage.addActor(GraphicTools.getUserNameLabel());
        }

        // draw the unique user_id.
        stage.addActor(GraphicTools.getUserIdLabel());

        //table.debug();      // Turn on all debug lines (table, cell, and widget).
    }

    public void InsertButtonToTable(final int num){
        // button properties - set image of button and pressed button.
        TextButton.TextButtonStyle styleNum = new TextButton.TextButtonStyle();
        // determine what levelPic to show. For currLevel - with the boy icon,
        // for past levels - blue, for next levels - gray.

        if(num < this.currLevel){
            styleNum.up = threeStarsSkin.getDrawable(Integer.toString(num));
            styleNum.down = threeStarsSkin.getDrawable(Integer.toString(num));
        }
        else if(num == this.currLevel) {
            styleNum.up = currLevelsSkin.getDrawable(Integer.toString(num));
            styleNum.down = currLevelsSkin.getDrawable(Integer.toString(num));
        }
        // --- gray ---
        else if(num > this.currLevel){
            styleNum.up = lockedSkin.getDrawable("locked");
            styleNum.down = lockedSkin.getDrawable("locked");
        }

        // button text and style.
        BitmapFont font = new BitmapFont();
        styleNum.font = font;
        final TextButton numButton = new TextButton("", styleNum);

        // add listener to the button.
        numButton.addListener(new ChangeListener() {
            /*if pressed go to setting screen*/
            public void changed(ChangeEvent event, Actor actor) {
                LevelParams levelParams = new LevelParams(num);
                //if it is not a locked level - show instructions if needed and start playing!
                // show instructions if needed.

                // CHEAT - allow execution of locked levels (for debug, observation etc.)
                if(Constants.MASTER_ACCESS){ // TODO 2021 - CHANGED
                    setLevelListener(levelParams);
                    return;
                }

                // always true.
                if(num == currLevel){
                    setLevelListener(levelParams);
                    return;
                }

                // backward playing is allowed. for research - not so good...
                if(CooperativeGame.gameData.getGame_params().isBackward_play_enabled()){
                    // allow playing only if it is not a locked level.
                    if(num < currLevel){
                        setLevelListener(levelParams);
                        return;
                    }
                }
            }
        });
        table.add(numButton).width(0.16f * screenWidth).height(0.22f * screenHeight);
    }

    // set the relevant listener to the level. i.e. pressing level 1 button will execute level 1 etc.
    public void setLevelListener(LevelParams levelParams){
        // play pressing sound.
        MusicPlayer.getInstance().uiSound();
        if(levelParams.getInstructions() != null){
            dispose();
            game.setScreen(new Instructions(game, levelParams));
        }
        else{
            dispose();
            switch(levelParams.getLevel_num()) {
                default:
                    game.setScreen(new GameScreen(game, levelParams));
                    break;
            }
        }
    }
    @Override
    public void show() {
        // et input from screen.
        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void render(float delta) {
        /* Draw screen. */
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.spriteBatch.begin();
        game.spriteBatch.draw(bgTexture, 0, 0, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        game.spriteBatch.end();
        stage.act();
        stage.draw();
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

    public void addHighScoreIfNeeded(){
        // no score! Probably it is the first level.
        if(CooperativeGame.gameData.getUser().getHigh_score() <= 0){
            return;
        }
        // score should be shown.
        else{
            //Size ratio for watch and scores elements.
            float mult = 1;
            float width = (float)0.21 * screenWidth;
            float height = (float)0.9 * width;
            Skin skin = new Skin();
            Texture texture = GraphicTools.getTexture(CooperativeGame.languagePrefix + "hud/high_score_show.png");

            skin.add("coin_score", texture);
            Image coinImage = new Image(skin, "coin_score");
            float xPosScoreImage = 0f * screenWidth, yPosScoreImage = 0.68f * screenHeight;

            /*
            float cc = yPosScoreImage + height;
            if((Constants.VIEWPORT_HEIGHT) * 1.03f < yPosScoreImage + height) {
                height = 0.95f * (Constants.VIEWPORT_HEIGHT - yPosScoreImage);
            }
             */
            coinImage.setBounds(xPosScoreImage, yPosScoreImage, width, height);
            stage.addActor(coinImage);

            Label.LabelStyle textStyle = new Label.LabelStyle();
            textStyle.fontColor = new Color(23f/255f, 20f/255f, 86f/255f, 0.7f);
            BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/myFont.fnt"), false);
            font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                    Texture.TextureFilter.Linear);
            textStyle.font = font;

            Label scoreRealLabel = new Label(Integer.toString(CooperativeGame.gameData.getUser().getHigh_score()),
                    textStyle);
            scoreRealLabel.setPosition(0.1f * screenWidth, 0.855f * screenHeight);
            scoreRealLabel.setFontScale(Constants.VIEWPORT_WIDTH * (1.6f / 1280f));
            stage.addActor(scoreRealLabel);
        }
    }
}