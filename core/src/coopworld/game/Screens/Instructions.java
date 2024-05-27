package coopworld.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;
import java.util.Map;

import coopworld.game.Buttons.NextButton;
import coopworld.game.CooperativeGame;
import coopworld.game.Logs.Enums;
import coopworld.game.Logs.LevelParams;
import coopworld.game.Scenes.AnimatedImage;
import coopworld.game.Screens.GameScreens.CombinedInstructions;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;
import coopworld.game.Tools.InputProcessors.InputProcessorGameScreen;
import coopworld.game.Tools.InputProcessors.InputProcessorInstructions;
import coopworld.game.Tools.MusicPlayer;

/*
 * Class Name: InstructionIce
 * implements screen, represents  instruction of dragging ice to basket
 */
public class Instructions implements Screen {
    protected Viewport viewport;
    protected Stage stage;
    protected CooperativeGame game;
    protected Texture texture;
    MusicPlayer musicPlayer;
    protected boolean voiceIsOn;
    protected LevelParams levelParams;
    private boolean createNextButton;
    public HashMap<String, String> viewAndAudio;
    private boolean buttonWasUpdated;
    InputMultiplexer inputMultiplexer;

    /**
     * constructor with the game .
     *
     * @param  game - the game.
     *
     */
    public Instructions(final CooperativeGame game, LevelParams levelParams){
        this.levelParams = levelParams;
        this.createNextButton = false;
        this.viewAndAudio = levelParams.getInstructions().getViewAndAudio();


        this.game = game;
        viewport = new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT,
                new OrthographicCamera());
        stage = new Stage(viewport, ((CooperativeGame) game).spriteBatch);
        this.musicPlayer = MusicPlayer.getInstance();

        this.voiceIsOn = true;
        this.buttonWasUpdated = false;

        /*
        NextButton nextButton = new NextButton(this, (float)0.87, (float)0.1, stage, game,
                (float)0.08, (float)0.08);

        BackButton backButton = new BackButton(this, 0.9f, 0.85f, stage, game, 0.08f, 0.08f);
        */
    }

    @Override
    public void show() {
        /*input from screen*/
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new InputProcessorInstructions(this));
        Gdx.input.setInputProcessor(inputMultiplexer);
        //Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // no music - maybe we need to show instruction (or replace).
        if(!(musicPlayer.isVoicePlaying())) {
            // there is another instruction to show!
            if (viewAndAudio.entrySet().iterator().hasNext()) {
                Map.Entry<String, String> entry = viewAndAudio.entrySet().iterator().next();
                String key = entry.getKey();
                String value = entry.getValue();

                texture = GraphicTools.getTexture(key);

                this.musicPlayer.instructionsSound(value);

                viewAndAudio.remove(key);
            }
        }

        // all instructions were displayed. now add the next button to stage.
        if((!buttonWasUpdated) && viewAndAudio.isEmpty() && (!(musicPlayer.isVoicePlaying()))){
            createNextButton = true;
            buttonWasUpdated = true;
        }

        if(createNextButton){
            createNextButton = false;
            float widthRatio = 0.25f;
            float xPos = 0.02f;
            if(Constants.GAME_LANGUAGE.getDirection()==Enums.LanguageDirection.LTR){
                xPos = 0.73f;
            }
            // hebrew/arabic version.
            else{
                xPos = 0.02f;
            }
            AnimatedImage button = new NextButton(xPos, 0.02f, stage, game, widthRatio,
                    0.95f * widthRatio).getButton();
            // add listener to next button.
            button.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                    musicPlayer.currMusic.dispose();
                    if(CooperativeGame.gameData.getGame_params().getInstructions_method() == Enums.InstructionsMethod.Combined){
                        game.setScreen(new CombinedInstructions(game, levelParams));
                    }

                    // free memory to avoid memory leaks.
                    dispose();
                    return true;
                }
            });
            stage.addActor(button);
        }
        // draw screen.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.spriteBatch.begin();
        game.spriteBatch.draw(texture, 0, 0, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
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
        stage.dispose();
    }

    public CooperativeGame getGame() {
        return game;
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public LevelParams getLevelParams() {
        return levelParams;
    }
}