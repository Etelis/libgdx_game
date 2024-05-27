package coopworld.game.Sprites.Bubbles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;

/**
 * Created by User on 14/03/2017.
 */
public abstract class Bubble {
    private TextureAtlas atlas;
    private Skin skin;
    private TextButton.TextButtonStyle right;
    private TextButton.TextButtonStyle left;
    private TextButton button;
    private BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/myFont.fnt"), false);
    private Vector2 bubblePositions = new Vector2();
    private Vector2 tempPlayerPos = new Vector2();
    protected boolean isInstructionLevel;

    float rightXoffset;
    float leftXoffset;

    float widthRatio = 0.1f;
    float heightRatio = 0.1f;

    String path;

    public TextButton buildBubble(){
        String prefix;
        prefix = "standard";
        /*
        if(isInstructionLevel){

            prefix = "instructions";
        }
        else{
            prefix = "standard";
        }
         */
        String rightString = prefix + "_right";
        String leftString = prefix + "_left";

        atlas = GraphicTools.getTextureAtlas(path);

        skin = new Skin();
        skin.addRegions(atlas);

        /* Button properties. */
        right = new TextButton.TextButtonStyle();
        right.up = skin.getDrawable(rightString);
        right.down = skin.getDrawable(rightString);
        right.font = font;

        left = new TextButton.TextButtonStyle();
        left.up = skin.getDrawable(leftString);
        left.down = skin.getDrawable(leftString);
        left.font = font;

        if(GameScreen.getVirtualPlayer().isRight()){
            button = new TextButton("", right)
            {
                @Override
                public void act(float delta)
                {
                    updateBubblePosition();
                }
            };
        }
        else{
            button = new TextButton("", left)
            {
                @Override
                public void act(float delta)
                {
                    updateBubblePosition();
                }
            };
        }

        /* Button position. */
        button.setSize(1.18f * widthRatio * Constants.VIEWPORT_WIDTH,
                heightRatio * Constants.VIEWPORT_WIDTH);

        return button;
    }

    public void updateBubblePosition(){
        float widthSize = Constants.VIEWPORT_WIDTH * (float) (2 * widthRatio * 1.28);
        float heightSize = Constants.VIEWPORT_WIDTH * (float) (2 * widthRatio);

        tempPlayerPos.set(GameScreen.getVirtualPlayer().b2body_boy.getPosition().x,
                GameScreen.getVirtualPlayer().b2body_boy.getPosition().y);
        tempPlayerPos = GameScreen.getViewPort().project(tempPlayerPos);

        // todo - make all thanks method in separated class and check setStyle methodology.
        if(GameScreen.getVirtualPlayer().isRight()){
            button.setStyle(right);
            bubblePositions.set(tempPlayerPos.x + (float) (rightXoffset * widthSize),
                    tempPlayerPos.y + (float) (0.1 * heightSize));
        }
        else {
            button.setStyle(left);
            bubblePositions.set(tempPlayerPos.x - (float) (leftXoffset * widthSize),
                    tempPlayerPos.y + (float) (0.1 * heightSize));
        }
        button.setX(bubblePositions.x);
        button.setY(bubblePositions.y);
    }

    public TextButton getBubble(){
        return this.button;
    }

}
