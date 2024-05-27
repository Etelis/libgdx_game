package coopworld.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import coopworld.game.CooperativeGame;
import coopworld.game.Logs.Enums;
import coopworld.game.Scenes.AnimatedImage;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;
import coopworld.game.Tools.MusicPlayer;

/*
 * Class Name: EndScreen
 */
public class EndScreen implements Screen {
    protected Viewport viewport;
    protected Stage stage;
    protected CooperativeGame game;
    protected Texture texture;
    MusicPlayer musicPlayer;
    /**
     * constructor to EndScreen.
     *
     * @param  game - the whole game class.
     *
     */
    public EndScreen(final CooperativeGame game, Enums.EndReason reason, int levelsPlayed){
        Enums.Gender userGender = CooperativeGame.gameData.getUser().getHuman_gender();
        Enums.Gender virtualGender = CooperativeGame.gameData.getUser().getVirtual_gender();

        String postfixVirtual, postfixHuman;
        if(userGender == Enums.Gender.Female){
            postfixHuman = "girl.png";
        }
        else{
            postfixHuman = "boy.png";
        }

        if(virtualGender == Enums.Gender.Female){
            postfixVirtual = "girl.png";
        }
        else{
            postfixVirtual = "boy.png";
        }

        if(Constants.IS_PROLIFIC) {
            postfixVirtual = "Prolific.png";
        }
        if(reason == Enums.EndReason.GAME_END){
            String path = CooperativeGame.languagePrefix + "screens/end_screen/EndWinningScreen_" + postfixVirtual;
            texture = GraphicTools.getTexture(path);
        }


        else if(reason == Enums.EndReason.SESSION_END){
            if(Constants.IS_INTERVENTION){
                // stops after 1, 3, 5, 7
                texture = GraphicTools.getTexture(CooperativeGame.languagePrefix + "screens/end_screen/intervention/" + levelsPlayed + "_EndSessionScreen_" + postfixHuman);
            }
            else{
                // stops after 4
                texture = GraphicTools.getTexture(CooperativeGame.languagePrefix + "screens/end_screen/EndSessionScreen_" + postfixHuman);
            }
        }

        this.game = game;
        this.viewport = new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT,
                new OrthographicCamera());
        this.stage = new Stage(viewport, game.spriteBatch);
        this.musicPlayer = MusicPlayer.getInstance();
        this.musicPlayer.playEndGameSound();

        if(reason == Enums.EndReason.GAME_END){
            if(!(Constants.IS_PROLIFIC)) {
                buildHighScoreAnimation();
            }
        }
        if(Constants.IS_PROLIFIC){
            String userIdStr = CooperativeGame.gameData.getUser().user_id;
            int completionCode = Integer.parseInt(userIdStr)/120;
            String completionCodeStr = Integer.toString(completionCode);

            Label completion_code_label = GraphicTools.getLabelByText(completionCodeStr);
            float fontSize = Constants.VIEWPORT_WIDTH * (2f / 1280f);
            completion_code_label.setFontScale(fontSize);
            completion_code_label.setPosition((float) 0.48 * Constants.VIEWPORT_WIDTH,
                    (float) 0.5 * Constants.VIEWPORT_HEIGHT);
            stage.addActor(completion_code_label);
        }

        //ExitButton exitButton = new ExitButton(0.9f, 0.85f, stage, 0.08f, 0.08f);
    }

    @Override
    public void show() {
        // input from screen.
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // draw screen.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.spriteBatch.begin();
        game.spriteBatch.draw(texture, 0, 0, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        game.spriteBatch.end();
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
        stage.dispose();
    }

    private void buildHighScoreAnimation(){
        //Texture Regions.
        // frames array (contains TextureRegions).
        Array<TextureRegion> frames = new Array<TextureRegion>();
        TextureAtlas atlas;
        Animation animation;
        Label scoreRealLabel;
        float width = 0.26f * Constants.VIEWPORT_WIDTH;
        float height = 0.2f * Constants.VIEWPORT_HEIGHT;

        /* stopwatch and display scores */
        Label.LabelStyle textStyle = new Label.LabelStyle();
        textStyle.fontColor = new Color(87f/255f, 103f/255f, 139f/255f, 1);
        BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/myFont.fnt"), false);
        /*
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);
         */

        textStyle.font = font;
        textStyle.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);

        atlas = GraphicTools.getTextureAtlas("hud/coin_animation.pack");

        frames.add(new TextureRegion(atlas.findRegion("10")));
        frames.add(new TextureRegion(atlas.findRegion("20")));
        frames.add(new TextureRegion(atlas.findRegion("30")));


        Enums.LanguageDirection direction;
        if(Constants.GAME_LANGUAGE.getDirection() == Enums.LanguageDirection.LTR){
            direction = Enums.LanguageDirection.LTR;
        }
        else{
            direction = Enums.LanguageDirection.RTL;
        }

        float xPosAnimation;
        if(direction == Enums.LanguageDirection.LTR){
            xPosAnimation = (float) 0.59 * Constants.VIEWPORT_WIDTH;
        }
        else{
            xPosAnimation = (float) 0.19 * Constants.VIEWPORT_WIDTH;
        }

        animation = new Animation(0.3f, frames);
        AnimatedImage animatedImage = new AnimatedImage(animation);
        animatedImage.setBounds(xPosAnimation, (float) 0.35 * Constants.VIEWPORT_HEIGHT, width, height);
        stage.addActor(animatedImage);

        scoreRealLabel = new Label(Integer.toString(CooperativeGame.gameData.getUser().getHigh_score()),
                textStyle);

        float xPosLabel;
        if(direction == Enums.LanguageDirection.LTR){
            xPosLabel = (float) 0.72 * Constants.VIEWPORT_WIDTH;
        }
        else{
            xPosLabel = (float) 0.32 * Constants.VIEWPORT_WIDTH;
        }
        scoreRealLabel.setPosition(xPosLabel, (float) 0.42 * Constants.VIEWPORT_HEIGHT);
        scoreRealLabel.setFontScale(Constants.VIEWPORT_WIDTH * (1.8f / 1280f));

        stage.addActor(scoreRealLabel);
    }
}