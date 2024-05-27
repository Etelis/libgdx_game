package coopworld.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Arrays;

import coopworld.game.Buttons.NextButton;
import coopworld.game.CooperativeGame;
import coopworld.game.Logs.Enums;
import coopworld.game.Networking.ResponseListeners.StartCodeListener;
import coopworld.game.Scenes.AnimatedImage;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;
import coopworld.game.Tools.InputProcessors.InputProcessorKeys;

/*
 * Class Name: AboutScreen
 * implements screen, represents the about screen.
 */
public class StartCodeScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private Texture texture;
    //SpriteBatch spriteBatch;
    TextField textBox;

    private final CooperativeGame game;

    AnimatedImage continueButton;
    String startCodeInserted;

    private boolean isNextOn;
    private Table table;
    Skin skin;
    TextureAtlas atlas;
    InputMultiplexer inputMultiplexer;
    InputProcessorKeys inputProcessorKeys;

    float numButtonW;
    float numButtonH;

    Image refreshButton;

    public StartCodeScreen(final CooperativeGame game) {
        //spriteBatch = new SpriteBatch();
        this.game = game;

        // load image for background.
        texture = GraphicTools.getTexture(CooperativeGame.languagePrefix + "screens/start_code/start_code_screen.png");
        viewport = new FitViewport(Constants.VIEWPORT_WIDTH,
                Constants.VIEWPORT_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.spriteBatch);
        //KeyBoardGame.gameData.setStart_code(KeyBoardGame.gameData.getUser().getUser_id());

        //stage.addActor(GraphicTools.getLabelByText(Constants.APK_VER)); // TODO - APK NUM

        isNextOn = false;

        textBox = getTextField();
        stage.addActor(textBox);

        refreshButton = getRefreshButton();
        stage.addActor(refreshButton);

        inputProcessorKeys = new InputProcessorKeys(textBox);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(inputProcessorKeys);

        continueButton = getNextButton();

        atlas = GraphicTools.getTextureAtlas("digits/digits_pics.pack");
        skin = new Skin();
        skin.addRegions(atlas);

        numButtonW = 0.07f * Constants.VIEWPORT_WIDTH;
        numButtonH = 0.9f * numButtonW;

        this.table = new Table();
        table.setFillParent(true);
        table.setPosition(0.4f * Constants.VIEWPORT_WIDTH, 0.03f * Constants.VIEWPORT_HEIGHT);

        table.left().bottom();

        for(int i = 1; i <= 9; i++) {
            if((i-1) % 3 == 0){
                table.row();
            }
            InsertButtonToTable(i + "");
        }
        table.row();
        InsertButtonToTable("-1");
        InsertButtonToTable("0");

        stage.addActor(table);

        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void InsertButtonToTable(final String number){
        // button properties - set image of button and pressed button.
        TextButton.TextButtonStyle styleNum = new TextButton.TextButtonStyle();
        if(!(number.equals("-1"))){
            styleNum.up = skin.getDrawable(number + "_off");
            styleNum.down = skin.getDrawable(number + "_on");
        }
        else{
            styleNum.up = skin.getDrawable("2_off");
            styleNum.down = skin.getDrawable("2_on");
        }

        // button text and style.
        BitmapFont font = new BitmapFont();
        styleNum.font = font;
        final TextButton numButton = new TextButton("", styleNum);

        if(!(number.equals("-1"))){
            numButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    textBox.setText(textBox.getText() + number);
                }
            });
            table.add(numButton).width(numButtonW).height(numButtonH).pad(0f);
        }
        else{
            table.add(numButton).width(0f).height(0f).pad(0f);
        }
    }

    public AnimatedImage getNextButton() {
        float widthRatio = 0.18f;
        float xPosition;
        if(Constants.GAME_LANGUAGE.getDirection() == Enums.LanguageDirection.RTL){
            xPosition = 0.1f;
        }
        else{ // LTR (English)
            xPosition = 0.8f;
        }
        AnimatedImage button = new NextButton(xPosition, 0f, stage, game, widthRatio,
                0.95f * widthRatio).getButton();

        // add listener to next button.
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                startCodeInserted = textBox.getText().replace("\n", "");

                // uniform link
                Constants.USER_ID_WEB = startCodeInserted;

                StartCodeListener startCodeListener = new StartCodeListener(game);

                if(Arrays.asList(Constants.MASTER_CODES).contains(startCodeInserted)){
                    // Deprecated behavior: Previously, the system did not allow children to use the master code (which might happen if they observed the experimenter using it). This is no longer the case.
                    //startCodeListener.unidentifiedUserAction();
                    Constants.MASTER_ACCESS = true;
                    ((CooperativeGame) Gdx.app.getApplicationListener()).crossPlatformObjects.getUtils().getUser(startCodeInserted);
                }
                else{ // uniform link
                    //dispose();
                    // no user_id in the link - ask to it in a dedicated screen.
                    ((CooperativeGame) Gdx.app.getApplicationListener()).crossPlatformObjects.getUtils().getUser(startCodeInserted);
                }
            }
        });
        return button;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        viewport.apply();
        update(delta);
        // white background.
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1, 1, 1, 1);

        //handleKeyboard();


        game.spriteBatch.begin();
        game.spriteBatch.draw(texture, 0, 0, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        game.spriteBatch.end();
        stage.act();
        stage.draw();
    }

    public void update(float delta) {
        if(textBox.getText() != ""){
            if(!isNextOn){
                stage.addActor(continueButton);
                isNextOn = true;
            }
        }
        // no full data inserted.
        else{
            if(isNextOn){
                continueButton.remove();
                isNextOn = false;
            }
        }
    }

    public Image getRefreshButton(){
        refreshButton = GraphicTools.getButtonPNG("buttons/back.png");
        refreshButton.setSize(0.08f * Constants.VIEWPORT_WIDTH, 0.08f * Constants.VIEWPORT_WIDTH);
        refreshButton.setPosition((0.36f + 0.3f) * Constants.VIEWPORT_WIDTH, 0.48f * Constants.VIEWPORT_HEIGHT);
        refreshButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                textBox.setText("");
            }
        });

        return refreshButton;
    }


    public TextField getTextField(){
        TextField textfield;
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        Skin skin = new Skin();
        skin.add("textField", GraphicTools.getTexture("screens/start_code/text_box_start_code.png"));
        style.background = skin.getDrawable("textField");

        BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/myFont.fnt"));
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);
        font.getData().setScale(Constants.VIEWPORT_WIDTH * (1.2f / 1280f));
        style.font = font;
        style.fontColor = new Color(Color.BLUE);

        textfield = new TextField("", style);
        float posX = 0.36f * Constants.VIEWPORT_WIDTH, posY = 0.47f * Constants.VIEWPORT_HEIGHT;

        textfield.setSize(0.3f * Constants.VIEWPORT_WIDTH, 0.15f * Constants.VIEWPORT_HEIGHT);

        textfield.setPosition(posX, posY);

        style.background.setLeftWidth(textfield.getWidth() * 0.2f);
        style.background.setTopHeight(textfield.getHeight() * 0.2f);
        //textfield.debug();

        textfield.setAlignment(Align.left);

        return textfield;
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