package coopworld.game.Buttons;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * GameButton
 * Interface for back, pause,resume and volume buttons.
 */
public abstract class GameButton {
    protected float xPosition;
    protected float yPosition;
    protected float widthSize;
    protected float heightSize;
    protected Stage stage;
    /* Image of buttons. */
    protected TextureAtlas buttonsAtlas;
    /* Images are used as skins of the BACK button. */
    protected Skin buttonSkin;
    protected TextButton button;
    protected BitmapFont font;

    /* Create button. */
    abstract void createButton();
}
