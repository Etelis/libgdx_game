package coopworld.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import coopworld.game.CooperativeGame;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;

/*
 * Class Name: AboutScreen
 * implements screen, represents the about screen.
 */
public class DeadEndScreen implements Screen {
    protected Viewport viewport;
    protected Stage stage;
    protected Texture texture;
    protected SpriteBatch spriteBatch;

    public DeadEndScreen(CooperativeGame game, String backgroundPath) {
        spriteBatch = game.spriteBatch;
        // load image for background.
        //texture = new Texture(backgroundPath);
        texture = GraphicTools.getTexture(backgroundPath);

        viewport = new FitViewport(Constants.VIEWPORT_WIDTH,
                Constants.VIEWPORT_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.spriteBatch);
        int f = 0;
        f++;
        //Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        viewport.apply();
        // white background.
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        spriteBatch.begin();
        spriteBatch.draw(texture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
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
        texture.dispose();
        stage.dispose();
    }
}