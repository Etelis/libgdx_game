package coopworld.game.Tools.InputProcessors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import coopworld.game.CooperativeGame;
import coopworld.game.CrossPlatform.GeneralUtils;
import coopworld.game.Logs.Enums;
import coopworld.game.Screens.GameScreens.CombinedInstructions;
import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Screens.Instructions;

import static coopworld.game.Screens.GameScreens.GameScreen.State.LevelEnded;


/**
 * Created by Chen on 18/10/2018.
 */

public class InputProcessorInstructions implements InputProcessor {
    String letters;
    long lastPressTime;
    long expiration;
    Instructions instructionsScreen;
    GeneralUtils utils;

    public InputProcessorInstructions(Instructions instructionsScreen) {
        this.utils = ((CooperativeGame) Gdx.app.getApplicationListener()).
                crossPlatformObjects.getUtils();
        this.letters = "";
        this.expiration = (long)(1000000000L * 1); // conversion is needed for ** numbers.
        this.instructionsScreen = instructionsScreen;
    }

    @Override
    public boolean keyDown(int keycode) {
        lastPressTime = utils.getNanoTime();
        letters += Input.Keys.toString(keycode).toLowerCase();

        if (letters.endsWith("ccccc") && !isExpirationPassed()) {
            instructionsScreen.getMusicPlayer().currMusic.dispose();
            if(CooperativeGame.gameData.getGame_params().getInstructions_method() == Enums.InstructionsMethod.Combined){
                instructionsScreen.getGame().setScreen(new CombinedInstructions(instructionsScreen.getGame(),
                        instructionsScreen.getLevelParams()));
            }

            // free memory to avoid memory leaks.
            instructionsScreen.dispose();
            //instructionsScreen.getLevelLog().setCheat_used(true);
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

    public boolean isExpirationPassed(){
        if(utils.getNanoTime() - lastPressTime >= expiration){
            return true;
        }
        return false;
    }
}
