package coopworld.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import coopworld.game.CooperativeGame;
import coopworld.game.Tools.Constants;

/*
 * Class Name: EndScreen
 */
public class LoadingScreen implements Screen {
    protected Viewport viewport;
    protected Stage stage;
    protected CooperativeGame game;
    protected Texture texture;
    private AssetManager assetManager;

    /**
     * constructor to EndScreen.
     *
     * @param  game - the whole game class.
     *
     */
    public LoadingScreen(final CooperativeGame game){
        CooperativeGame.loader.getAssetManager().load(CooperativeGame.languagePrefix + "screens/loading screen/loading_screen.png", Texture.class);
        CooperativeGame.loader.getAssetManager().finishLoading();

        texture = CooperativeGame.loader.getAssetManager().
                get(CooperativeGame.languagePrefix + "screens/loading screen/loading_screen.png", Texture.class);

        this.game = game;
        this.viewport = new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT,
                new OrthographicCamera());
        this.stage = new Stage(viewport, game.spriteBatch);
        this.assetManager = CooperativeGame.loader.assetManager;

        CooperativeGame.loader.loadNeededAssets();
        // TODO EXPERIMENT SARIT - HOPE IT IS OK :D
        CooperativeGame.loader.getAssetManager().finishLoading();

        /*
        ExitButton exitButton = new ExitButton(0.9f, 0.85f, stage, 0.08f, 0.08f);
        */
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

        // if all the resources have been uploaded - go to levels screen.
        if(assetManager.update()){
            //MusicPlayer.getInstance().initLanguageBasedAudios();
            //game.setScreen(new CombinedInstructions(game, new LevelParams(1)));

            game.setScreen(new LevelsScreen(game));

            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
    }    @Override
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