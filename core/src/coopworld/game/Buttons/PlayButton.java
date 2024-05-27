package coopworld.game.Buttons;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import coopworld.game.CooperativeGame;
import coopworld.game.Logs.Enums;
import coopworld.game.Screens.EndScreen;
import coopworld.game.Screens.LevelsScreen;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;

/*
 * Class Name: BackButton
 * implements GameButton, represents the back button.
 */
public class PlayButton extends GameButton {
    /* Images are used as skins of the BACK button. */
    final CooperativeGame game;
    /* Create BACK button, when pressed go to menu screen. */

    @Override
    public void createButton() {
        Image buttonImg = GraphicTools.getButtonPNG(CooperativeGame.languagePrefix + "buttons/continue.png");
        /* Set position and size. */
        buttonImg.setPosition(Constants.VIEWPORT_WIDTH * xPosition, Constants.VIEWPORT_HEIGHT *
                yPosition);
        buttonImg.setWidth(widthSize * Constants.VIEWPORT_WIDTH);
        buttonImg.setHeight(GraphicTools.getHeight(buttonImg, buttonImg.getWidth()));

        stage.addActor(buttonImg);

        // add listener to play button.
        buttonImg.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                int levelsPlayed = CooperativeGame.gameData.getUser().getLevels_played();

                // after the last level - go to a special end screen.
                // todo - change from lastLevel to currLevel!

                if(Constants.IS_PROLIFIC){
                    if(levelsPlayed < CooperativeGame.levelsPath.size()){
                        game.setScreen(new LevelsScreen(game));
                    }
                    else{
                        game.setScreen(new EndScreen(game, Enums.EndReason.GAME_END, levelsPlayed));
                    }
                }
                else{
                    // TODO SARIT - CHECK THIS + duplicated code!
                    if(!Constants.IS_INTERVENTION){
                        if((levelsPlayed == Constants.LEVELS_IN_SESSION)){
                            game.setScreen(new EndScreen(game, Enums.EndReason.SESSION_END, levelsPlayed));
                        }
                        else if(levelsPlayed == CooperativeGame.levelsPath.size()){
                            game.setScreen(new EndScreen(game, Enums.EndReason.GAME_END, levelsPlayed));
                        }
                        // not the last level - go to Levels Screen.
                        else {
                            game.setScreen(new LevelsScreen(game));
                        }
                    }

                    // intervention group
                    else{
                        if(Constants.INTERVENTION_LEVELS_IN_SESSIONS.contains(CooperativeGame.gameData.getUser().getLevels_played())){
                            game.setScreen(new EndScreen(game, Enums.EndReason.SESSION_END, levelsPlayed));
                        }
                        else if(levelsPlayed == Constants.INTERVENTION_TOTAL_LEVELS){
                            game.setScreen(new EndScreen(game, Enums.EndReason.GAME_END, levelsPlayed));
                        }
                        // not the last level - go to Levels Screen.
                        else {
                            game.setScreen(new LevelsScreen(game));
                        }
                    }
                }


                return true;
            }
        });
        stage.addActor(buttonImg);
    }

    /**
     * constructor with gameScreen, x and y coordinates of button, stage ,game
     * width and height of button.
     * @param  xPosition - x coordinate of back button
     * @param  yPosition - y coordinate of back button
     * @param stage - a stage
     * @param game - the game
     * @param widthSize - width size of button
     *
     */
    public PlayButton(float xPosition, float yPosition, Stage stage,
                      final CooperativeGame game, float widthSize){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.stage = stage;
        this.game = game;
        this.widthSize = widthSize;
        this.heightSize = heightSize;
        /* Create back button. */
        createButton();
    }
}