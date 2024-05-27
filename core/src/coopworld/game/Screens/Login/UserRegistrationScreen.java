package coopworld.game.Screens.Login;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import coopworld.game.Buttons.BackButton;
import coopworld.game.Buttons.NextButton;
import coopworld.game.CooperativeGame;
import coopworld.game.Logs.Enums;
import coopworld.game.Logs.RegistrationData;
import coopworld.game.Logs.User;
import coopworld.game.Scenes.AnimatedImage;
import coopworld.game.Screens.LoadingScreen;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;
import coopworld.game.Tools.IndicationPopUpScreen;
import coopworld.game.Tools.MusicPlayer;
import coopworld.game.Tools.PopUp;

/*
 * Class Name: AboutScreen
 * implements screen, represents the about screen.
 */
public class UserRegistrationScreen implements Screen {
    private Viewport viewport;
    protected Stage stage;
    protected final CooperativeGame game;
    protected Texture texture;
    public MusicPlayer music;
    TextureAtlas playersButton;
    protected Button humanGirlButton, humanBoyButton;
    protected Button virtualGirlButton, virtualBoyButton;

    protected Enums.Gender userGender;
    protected Enums.Gender virtualGender;

    //protected TextField textbox;
    Button.ButtonStyle styleHumanGirlOn, styleHumanGirlOff;
    Button.ButtonStyle styleHumanBoyOn, styleHumanBoyOff;

    Button.ButtonStyle styleVirtualGirlOn, styleVirtualGirlOff;
    Button.ButtonStyle styleVirtualBoyOn, styleVirtualBoyOff;

    AnimatedImage nextButton;
    boolean isNextOn;
    boolean userAlreadyExists;
    PopUp userExistsPopUp;
    boolean userExistsPopUpIsOn;
    boolean lastUserExists;
    Skin skin;

    protected Map<String, String> strategiesMap;

    String lastScreen;

    private boolean userWasCreated;

    Enums.FormScreenState FORM_SCREEN_STATE;

    IndicationPopUpScreen indicationPopUpScreen;

    Enums.LanguageDirection languageDirection;

    /**
     * Constructor with the game.
     *
     * @param  game - the game.
     *
     */
    public UserRegistrationScreen(CooperativeGame game, String lastScreen) {
        userGender = null;
        isNextOn = false;
        userExistsPopUpIsOn = false;
        userAlreadyExists = false;
        lastUserExists = false;
        nextButton = null;
        FORM_SCREEN_STATE = Enums.FormScreenState.In_Progress;

        strategiesMap = Constants.strategiesMap;

        playersButton = GraphicTools.getTextureAtlas("login/registration_screen/gender_choose.pack");

        skin = new Skin();
        skin.addRegions(playersButton);

        //////////////////////////////
        /* Button text and style. */
        // HUMAN
        styleHumanGirlOn = new Button.ButtonStyle();
        styleHumanGirlOn.up = skin.getDrawable("human_girl_on");

        styleHumanGirlOff = new Button.ButtonStyle();
        styleHumanGirlOff.up = skin.getDrawable("human_girl_off");

        styleHumanBoyOn = new Button.ButtonStyle();
        styleHumanBoyOn.up = skin.getDrawable("human_boy_on");

        styleHumanBoyOff = new Button.ButtonStyle();
        styleHumanBoyOff.up = skin.getDrawable("human_boy_off");

        //VIRTUAL
        styleVirtualGirlOn = new Button.ButtonStyle();
        styleVirtualGirlOn.up = skin.getDrawable("virtual_girl_on");

        styleVirtualGirlOff = new Button.ButtonStyle();
        styleVirtualGirlOff.up = skin.getDrawable("virtual_girl_off");

        styleVirtualBoyOn = new Button.ButtonStyle();
        styleVirtualBoyOn.up = skin.getDrawable("virtual_boy_on");

        styleVirtualBoyOff = new Button.ButtonStyle();
        styleVirtualBoyOff.up = skin.getDrawable("virtual_boy_off");

        //////////////////////////////

        initTexture();

        this.game = game;
        viewport = new FitViewport(Constants.VIEWPORT_WIDTH,
                Constants.VIEWPORT_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.spriteBatch);

        Image bg = new Image(texture);
        bg.setPosition(0, 0);
        bg.setSize(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        stage.addActor(bg);

        //textbox = getTextField();
        // HUMAN
        humanGirlButton = getPlayerButton(Enums.Gender.Female, Enums.Player.Human);
        humanBoyButton = getPlayerButton(Enums.Gender.Male, Enums.Player.Human);
        // VIRTUAL
        virtualGirlButton = getPlayerButton(Enums.Gender.Female, Enums.Player.Virtual);
        virtualBoyButton = getPlayerButton(Enums.Gender.Male, Enums.Player.Virtual);

        //stage.addActor(textbox);
        //stage.addActor(GraphicTools.getUserNameLabel());

        stage.addActor(humanGirlButton);
        stage.addActor(humanBoyButton);
        stage.addActor(virtualGirlButton);
        stage.addActor(virtualBoyButton);

        this.lastScreen = lastScreen;

        userWasCreated = false;

        // input from screen.
        Gdx.input.setInputProcessor(stage);

        languageDirection = Constants.GAME_LANGUAGE.getDirection();
        /* Create back button. */
        float xPos;
        if(languageDirection == Enums.LanguageDirection.RTL){
            xPos = 0;
        }
        else{
            xPos = 0.92f;
        }
        BackButton backButton = new BackButton(this, xPos, 0.87f,
                stage, game, (float)0.08, (float)0.08);

        // draw the unique user_id.
        stage.addActor(GraphicTools.getUserIdLabel());
    }


    public void initTexture(){
        // load image for background.
        texture = GraphicTools.getTexture(CooperativeGame.languagePrefix + "screens/registration_screen/background.png");
    }

    public Button getPlayerButton(final Enums.Gender gender, Enums.Player playerType) {
        Button.ButtonStyle style = null;
        // initialize position
        float yPos = 0;
        if(playerType == Enums.Player.Human){
            yPos = 0.45f;
        }
        else if(playerType == Enums.Player.Virtual){
            yPos = 0.14f;
        }

        if(playerType == Enums.Player.Human){
            if (gender == Enums.Gender.Female) {
                style = styleHumanGirlOff;
            } else if (gender == Enums.Gender.Male) {
                style = styleHumanBoyOff;
            }
        }
        else if(playerType == Enums.Player.Virtual){
            if (gender == Enums.Gender.Female) {
                style = styleVirtualGirlOff;
            } else if (gender == Enums.Gender.Male) {
                style = styleVirtualBoyOff;
            }
        }

        Button button = new Button(style);

        float ratio = 0.7f;
        // no matter if up or down here.
        button.setSize(style.up.getMinWidth() * ratio,
                style.up.getMinHeight() * ratio);

        if (gender == Enums.Gender.Female) {
            button.setPosition(0.51f * Constants.VIEWPORT_WIDTH, yPos * Constants.VIEWPORT_HEIGHT);
        } else if (gender == Enums.Gender.Male) {
            button.setPosition(0.29f * Constants.VIEWPORT_WIDTH, yPos * Constants.VIEWPORT_HEIGHT);
        }

        if(playerType == Enums.Player.Human){
            if(gender == Enums.Gender.Female) {
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                    userGender = Enums.Gender.Female;

                    humanGirlButton.setStyle(styleHumanGirlOn);
                    humanBoyButton.setStyle(styleHumanBoyOff);
                    int i = 9;
                    }
                });
            }
            else if(gender == Enums.Gender.Male){
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                    userGender = Enums.Gender.Male;

                    humanBoyButton.setStyle(styleHumanBoyOn);
                    humanGirlButton.setStyle(styleHumanGirlOff);
                    }
                });
            }
        }
        else if(playerType == Enums.Player.Virtual){
            if(gender == Enums.Gender.Female) {
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                    virtualGender = Enums.Gender.Female;

                    virtualGirlButton.setStyle(styleVirtualGirlOn);
                    virtualBoyButton.setStyle(styleVirtualBoyOff);
                    }
                });
            }
            else if(gender == Enums.Gender.Male){
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                    virtualGender = Enums.Gender.Male;

                    virtualBoyButton.setStyle(styleVirtualBoyOn);
                    virtualGirlButton.setStyle(styleVirtualGirlOff);
                    }
                });
            }
        }

        return button;
    }

    public AnimatedImage getNextButton() {
        float widthRatio = 0.25f;
        float xPos;
        if(languageDirection == Enums.LanguageDirection.RTL){
            xPos = 0.005f;
        }
        else{
            xPos = 0.77f;
        }
        AnimatedImage button = new NextButton(xPos, 0.005f, stage, game, widthRatio,
                0.95f * widthRatio).getButton();
        // add listener to next button.
        button.addListener(getNextListener());
        return button;
    }

    public ClickListener getNextListener(){
        ClickListener nextListener = new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                //User user = new User();
                //user.initContinualUser(userName, userGender, virtualGender);
                userWasCreated = true;
                FORM_SCREEN_STATE = Enums.FormScreenState.Next_Clicked;

                nextButton.remove();

                indicationPopUpScreen = new IndicationPopUpScreen(CooperativeGame.languagePrefix + "screens/registration_screen/user_creation_succeeded.png");

                indicationPopUpScreen.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        FORM_SCREEN_STATE = Enums.FormScreenState.End;
                    }
                });
                stage.addActor(indicationPopUpScreen);

                RegistrationData registrationData = new RegistrationData();
                registrationData.setHuman_gender(userGender);
                registrationData.setVirtual_gender(virtualGender);
                registrationData.setUser_id(CooperativeGame.gameData.getUser().getUser_id());
                String registrationTime = ((CooperativeGame) Gdx.app.getApplicationListener()).
                        crossPlatformObjects.getUtils().getTimeStamp();
                registrationData.setRegistration_time(registrationTime);

                CooperativeGame.conn.sendRegistrationData(registrationData);

                User user = CooperativeGame.gameData.getUser();
                user.setHuman_gender(userGender.toString());
                user.setVirtual_gender(virtualGender.toString());
                user.setGenders();
                user.setRegistration_time(registrationTime);
                user.setWas_registered("Yes");
                CooperativeGame.gameData.setUser(user);

                return true;
                }
            };
        return nextListener;
    }

    public String capitalize(String name){
        if(name == ""){
            return name;
        }

        String newName;
        // capitalize first character, and the rest - small.
        newName = name.toLowerCase();
        newName = name.substring(0, 1).toUpperCase() + name.substring(1);

        return newName;
    }

    // write details (if they are fine).
    //

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        update(delta);
        /* Draw screen. */
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.spriteBatch.begin();
        //game.spriteBatch.draw(texture, 0, 0, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        game.spriteBatch.end();
        stage.act();
        stage.draw();
    }

    public void update(float delta) {
        // todo - delete text??
        /*
        // get text and capitalize first character, and the rest - small.
        userName = textbox.getText();
        userName = capitalize(userName);
        textbox.setText(userName);
         */

        switch(FORM_SCREEN_STATE){
            case In_Progress:
                if((userGender != null) && (virtualGender != null)){
                    // if user already exists - do not make it possible to generate a new user.
                    // TODO COOP2 WEB CHECK - EXPERIMENT SARIT
                    userAlreadyExists = false;

                    // last was OK, now it is exists.
                    if(userAlreadyExists && !lastUserExists){
                        userExistsPopUp = getUserExistsPopUp();
                        stage.addActor(userExistsPopUp);
                        userExistsPopUpIsOn = true;
                        removeNextButton();
                    }
                    // last was exists, now it is OK.
                    else if(!userAlreadyExists && lastUserExists){
                        userAlreadyExists = false;
                        userExistsPopUp.remove();
                        userExistsPopUpIsOn = false;
                    }

                    if(!userAlreadyExists){
                        if(!isNextOn){
                            nextButton = getNextButton();
                            stage.addActor(nextButton);
                            isNextOn = true;
                        }
                    }

                    lastUserExists = userAlreadyExists;
                }
                // no full data inserted.
                else{
                    removeNextButton();
                }
                break;

            case End:
                indicationPopUpScreen.remove();
                String lastScreen = getLastScreen();

                dispose();
                game.setScreen(new LoadingScreen(game));
                //stage.dispose();
                break;

            default:
                break;
        }
    }

    public void removeNextButton(){
        if(isNextOn){
            if(nextButton != null) {
                // TODO - CHECK IT! THE BUTTON IS STILL THERE....
                nextButton.remove();
                ///nextButton.addAction(Actions.removeActor(nextButton));
                isNextOn = false;
            }
        }
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
        texture.dispose();
        stage.dispose();
    }

    public PopUp getUserExistsPopUp(){
        float width, xPos, yPos, duration;
        String path;

        path = CooperativeGame.languagePrefix + "screens/registration_screen/user_exists.png";
        duration = -1;

        xPos = 0f * Constants.VIEWPORT_WIDTH;
        yPos = 0f * Constants.VIEWPORT_HEIGHT;

        width = 0.35f * Constants.VIEWPORT_WIDTH;

        PopUp popUp = new PopUp(path, duration, xPos, yPos, width, 0.28f * width,
                ((CooperativeGame) Gdx.app.getApplicationListener()).
                        crossPlatformObjects.getUtils(), false, 0);
        popUp.init();
        return popUp;
    }

    public String getLastScreen(){
        return lastScreen;
    }
}