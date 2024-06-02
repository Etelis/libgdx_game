package coopworld.game.Tools.InputProcessors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;


/**
 * Created by Chen on 18/10/2018.
 */

public class InputProcessorKeys implements InputProcessor {
    TextField textBox;

    public InputProcessorKeys(TextField textBox) {
        this.textBox = textBox;
    }

    @Override
    public boolean keyDown(int keycode) {
        if((keycode <= Input.Keys.NUM_9) && (keycode >= Input.Keys.NUM_0)){
            textBox.setText(textBox.getText() + String.valueOf(keycode - 7));
        }
        else if(keycode == Input.Keys.BACKSPACE){
            if(textBox.getText().length() > 0){
                textBox.setText(textBox.getText().substring(0, textBox.getText().length() - 1));
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
