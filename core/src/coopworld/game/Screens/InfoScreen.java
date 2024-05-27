package coopworld.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import coopworld.game.Buttons.BackButton;
import coopworld.game.CooperativeGame;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;

public class InfoScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    final CooperativeGame game;
    private Texture texture;

    public InfoScreen(CooperativeGame game) {
        this.game = game;
        texture = GraphicTools.getTexture("manage_zone/info_bg.png");
        viewport = new FitViewport(Constants.VIEWPORT_WIDTH,
                Constants.VIEWPORT_HEIGHT);
        stage = new Stage(viewport, game.spriteBatch);
        stage.addActor(getTextField());

        BackButton backButton = new BackButton(this, 0, 0.87f,
                stage, game, (float)0.08, (float)0.08);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        /* Draw screen. */
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.spriteBatch.begin();
        game.spriteBatch.draw(texture, 0, 0, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        game.spriteBatch.end();
        stage.act();
        stage.draw();
    }

    public String getInfoStr(){
        String infoStr = "";
        infoStr += "App Version: " + Constants.APK_VER + '\n';
        infoStr += "Setting: " + Constants.SETTING_GROUP + '\n';
        infoStr += "Tablet ID: " + Constants.TABLET_CODE + '\n';

        return infoStr;
    }

    public TextArea getTextField(){
        TextArea textfield;
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        Skin skin = new Skin();
        skin.add("textField", GraphicTools.getTexture("feedback/text_box_feedback.png"));
        style.background = skin.getDrawable("textField");

        BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/myFont.fnt"));
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);
        font.getData().setScale(Constants.VIEWPORT_WIDTH * (1.2f / 1280f));
        style.font = font;
        style.fontColor = new Color(19f/255f, 17f/255f, 65f/255f, 1);

        textfield = new TextArea(getInfoStr(), style);
        float ratio = 0.8f;
        /*
        textfield.setSize(style.background.getMinWidth() * 2 * ratio,
                style.background.getMinHeight() * ratio);
        */

        float w = 0.8f * Constants.VIEWPORT_WIDTH;
        float posX = 0.1f * Constants.VIEWPORT_WIDTH, posY = 0.1f * Constants.VIEWPORT_HEIGHT;
        textfield.setSize(w, 0.42f * w);

        textfield.setPosition(posX, posY);

        // todo scaling.
        style.background.setLeftWidth(0.1f * Constants.VIEWPORT_WIDTH);
        style.background.setTopHeight(0.2f * Constants.VIEWPORT_HEIGHT);
        //textfield.debug();

        textfield.setAlignment(Align.center);

        return textfield;
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
}
