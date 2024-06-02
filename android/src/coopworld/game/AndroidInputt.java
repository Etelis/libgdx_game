package coopworld.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import coopworld.game.CrossPlatform.Input;
import coopworld.game.Sprites.Players.Player;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;

public class AndroidInputt extends Input {
    private TextureAtlas buttonsAtlasUP, buttonsAtlasLEFT, buttonsAtlasRIGHT; /* Button's images. */
    private TextButton buttonUP, buttonLEFT, buttonRIGHT;
    float posWidth, posHeight;
    /* Images are used as skins of the button. */
    private Skin buttonSkinUP, buttonSkinLEFT, buttonSkinRIGHT;
    ClickListener listenerRight, listenerLeft, listenerUp;
    float buttonsSize = (float) 0.11;
    Stage stage;

    public void init(Player player, Stage stage){
        this.player = player;
        this.stage = stage;
        createArrowsButtons();
    }

    @Override
    public void arrowMovement() {
        /* Stop the player when he is on ground and no presses on left/right. */
        if ((!listenerRight.isPressed() && !listenerLeft.isPressed()) &&
                player.b2body_boy.getLinearVelocity().y == 0) {
            player.b2body_boy.setLinearVelocity(new Vector2(0f, 0));
        }

        // x axis movement:
        // player move to the right.
        if (buttonRIGHT.isPressed()) {
            player.right();
        }
        // player move to the left.
        if (listenerLeft.isPressed()) {
            player.left();
        }
        // y axis movement:
        // player jump.
        if (listenerUp.isPressed()) {
            player.up();
        }
    }

    /* Create movement buttons. */
    public void createArrowsButtons(){
        BitmapFont font = new BitmapFont();
        /* Button atlas image. */
        buttonsAtlasUP = GraphicTools.getTextureAtlas("buttons/arrows.pack");

        buttonSkinUP = new Skin();
        /* Skins for on and off. */
        buttonSkinUP.addRegions(buttonsAtlasUP);
        /* Create button. */
        TextButton.TextButtonStyle styleUP = new TextButton.TextButtonStyle();
        styleUP.up = buttonSkinUP.getDrawable("UP");
        styleUP.down = buttonSkinUP.getDrawable("UP_UP");
        styleUP.font = font;
        buttonUP = new TextButton("", styleUP);

        /* Button position. */
        posWidth = (float) (0.03);
        posHeight = (float) (0.03);
        buttonUP.setPosition(posWidth * Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT * posHeight);
        buttonUP.setSize(buttonsSize * Constants.VIEWPORT_WIDTH, buttonsSize * Constants.VIEWPORT_WIDTH);

        /* Add the button to stage. */
        stage.addActor(buttonUP);
        /* Create listener to the button. */
        listenerUp = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                //System.out.println("UP TOUCH WAS DETECTED!");
            }
        };
        buttonUP.addListener(listenerUp);

        /* Button atlas image. */
        buttonsAtlasLEFT = GraphicTools.getTextureAtlas("buttons/arrows.pack");

        buttonSkinLEFT = new Skin();
        buttonSkinLEFT.addRegions(buttonsAtlasLEFT);
        /* Create button. */
        TextButton.TextButtonStyle styleLEFT = new TextButton.TextButtonStyle();
        /* Button properties */
        styleLEFT.up = buttonSkinLEFT.getDrawable("LEFT");
        styleLEFT.down = buttonSkinLEFT.getDrawable("LEFT_UP");
        styleLEFT.font = font;
        buttonLEFT = new TextButton("", styleLEFT);
        /* Button position. */
        posWidth = (float) (0.72);
        posHeight = (float) (0.03);
        buttonLEFT.setPosition(Constants.VIEWPORT_WIDTH * posWidth, Constants.VIEWPORT_HEIGHT * posHeight);
        buttonLEFT.setSize(buttonsSize * Constants.VIEWPORT_WIDTH, buttonsSize * Constants.VIEWPORT_WIDTH);
        /* Add button to stage. */
        stage.addActor(buttonLEFT);
        /* Create listener to the button. */
        listenerLeft = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                //System.out.println("LEFT TOUCH WAS DETECTED!");
            }
        };
        buttonLEFT.addListener(listenerLeft);

        /* Create button. */
        buttonsAtlasRIGHT = GraphicTools.getTextureAtlas("buttons/arrows.pack");

        buttonSkinRIGHT = new Skin();
        buttonSkinRIGHT.addRegions(buttonsAtlasRIGHT);
        /* Button properties. */
        TextButton.TextButtonStyle styleRIGHT = new TextButton.TextButtonStyle();
        styleRIGHT.up = buttonSkinRIGHT.getDrawable("RIGHT");
        styleRIGHT.down = buttonSkinRIGHT.getDrawable("RIGHT_UP");
        styleRIGHT.font = font;
        buttonRIGHT = new TextButton("", styleRIGHT);
        /* Button position. */
        posWidth = (float) (0.87);
        posHeight = (float) (0.03);
        buttonRIGHT.setPosition(Constants.VIEWPORT_WIDTH * posWidth, Constants.VIEWPORT_HEIGHT * posHeight);
        buttonRIGHT.setSize(buttonsSize * Constants.VIEWPORT_WIDTH, buttonsSize * Constants.VIEWPORT_WIDTH);
        /* Add the button to stage. */
        stage.addActor(buttonRIGHT);
        /* Create listener. */
        listenerRight = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                //System.out.println("RIGHT TOUCH WAS DETECTED!");
            }
        };

        buttonRIGHT.addListener(listenerRight);
    }
}
