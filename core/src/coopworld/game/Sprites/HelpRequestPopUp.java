package coopworld.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import coopworld.game.CooperativeGame;
import coopworld.game.Logs.Enums;
import coopworld.game.Scenes.AnimatedImage;
import coopworld.game.Screens.GameScreens.CombinedInstructions;
import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;

public class HelpRequestPopUp {
    GameScreen gameScreen;
    public Group helpRequestPopUp;
    // position in y axis for Yes and No buttons.
    private float yButtonsPosition = 0.37f;
    // sizes of yes and no buttons.
    float width = 0.22f * Constants.VIEWPORT_WIDTH;
    float height = 0.5f * width;
    private boolean humanRespone;
    private Vector2 rightAnswerPos, leftAnswerPos;
    private Actor noActor, yesActor;

    public HelpRequestPopUp(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        rightAnswerPos = new Vector2(0.5f * Constants.VIEWPORT_WIDTH, yButtonsPosition * Constants.VIEWPORT_HEIGHT);
        leftAnswerPos = new Vector2(0.28f * Constants.VIEWPORT_WIDTH, yButtonsPosition * Constants.VIEWPORT_HEIGHT);

        helpRequestPopUp = new Group();
        noActor = createNoActor(getRegularButtonImage(false, false));
        yesActor = createYesActor(getRegularButtonImage(true, false));

        helpRequestPopUp.addActor(getPupUpActor());
        helpRequestPopUp.addActor(yesActor);
        helpRequestPopUp.addActor(noActor);
    }

    public Actor getPupUpActor(){
        Skin skin = new Skin();
        float width = 0.55f * Constants.VIEWPORT_WIDTH;
        float height = 0.65f * width;

        // TODO GWT - CHANGED! TO / INSTEAD OF //
        //Texture popUp = new Texture(CooperativeGame.languagePrefix + "/pop-ups/help_decision_pop_up/PopUp.png");
        // TODO - support gender change.
        String virtualPlayerGenderReference;

        if(CooperativeGame.gameData.getUser().getVirtual_gender() == Enums.Gender.Male){
            virtualPlayerGenderReference = "virtual_boy";
        }
        else{
            virtualPlayerGenderReference = "virtual_girl";
        }

        Texture popUp = GraphicTools.getTexture(CooperativeGame.languagePrefix +
                "pop-ups/help_decision_pop_up/" + virtualPlayerGenderReference + "/PopUp.png");

        skin.add("popUp", popUp);
        Image coinImage = new Image(skin, "popUp");
        coinImage.setBounds(0.22f * Constants.VIEWPORT_WIDTH, 0.21f * Constants.VIEWPORT_HEIGHT,
                width, height);

        return coinImage;
    }

    public Actor createYesActor(Actor buttonActor){
        Vector2 answerPos;
        if(Constants.GAME_LANGUAGE.getDirection() == Enums.LanguageDirection.LTR){
            answerPos = leftAnswerPos;
        }
        // HEBREW.
        else{
            answerPos = rightAnswerPos;
        }
        buttonActor.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                yesActionListener();
                return true;
            }
        });

        buttonActor.setBounds(answerPos.x, answerPos.y, width, height);
        return buttonActor;
    }

    public Actor createNoActor(Actor buttonActor){
        Vector2 answerPos;
        if(Constants.GAME_LANGUAGE.getDirection()==Enums.LanguageDirection.LTR){
            answerPos = rightAnswerPos;
        }
        // HEBREW.
        else{
            answerPos = leftAnswerPos;
        }

        buttonActor.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                noActionListener();
                return true;
            }
        });

        buttonActor.setBounds(answerPos.x, answerPos.y, width, height);
        return buttonActor;
    }

    public boolean getHumanResponse(){
        return this.humanRespone;
    }

    public Texture getYesAnimation(){
        Texture popUp = GraphicTools.getTexture(CooperativeGame.languagePrefix + "pop-ups/help_decision_pop_up/Yes.png");
        return popUp;
    }

    // force human to answer YES when asked for help.
    public void forceYes(){
        // change the YES button to glimmer (animation)
        TextureAtlas atlas = null;
        String path = CooperativeGame.languagePrefix + "pop-ups/help_decision_pop_up/yes_animation.pack";
        atlas = GraphicTools.getTextureAtlas(path);

        Animation<TextureRegion> animation;
        animation = new Animation<TextureRegion>(0.066f,
                atlas.findRegions("Yes"), Animation.PlayMode.LOOP);

        AnimatedImage yesAnimation = new AnimatedImage(animation);
        // change the YES listener to change the state (instructional inner state).
        yesAnimation.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                yesActionListener();
                ((CombinedInstructions)gameScreen).setGeneralVirtualAsking(CombinedInstructions.GeneralVirtualAsking.After);
                // ADD SOME TEXT... EXPLANATION.
                return true;
            }
        });
        // change actor - this causes the appropriate textual popup to appear.
        helpRequestPopUp.removeActor(yesActor);
        this.yesActor = createYesActor(yesAnimation);
        helpRequestPopUp.addActor(this.yesActor);

        helpRequestPopUp.removeActor(noActor);
        this.noActor = createNoActor(getRegularButtonImage(false, true));
        noActor.getListeners().clear();
        helpRequestPopUp.addActor(this.noActor);

        //CooperativeGame.languagePrefix + "pop-ups/help_decision_pop_up/No.png"

        // no listener for no button. it is turned off.

    }


    // force human to answer NO when asked for help.
    public void forceNo(){
        yesActor.getListeners().clear();
        // change the YES button to glimmer (animation)
        TextureAtlas atlas = null;
        String path = CooperativeGame.languagePrefix + "pop-ups/help_decision_pop_up/no_animation.pack";
        atlas = GraphicTools.getTextureAtlas(path);

        Animation<TextureRegion> animation;
        animation = new Animation<TextureRegion>(0.066f,
                atlas.findRegions("No"), Animation.PlayMode.LOOP);

        AnimatedImage noAnimation = new AnimatedImage(animation);
        // change the YES listener to change the state (instructional inner state).
        noAnimation.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                noActionListener();
                ((CombinedInstructions)gameScreen).setGeneralVirtualAsking(CombinedInstructions.GeneralVirtualAsking.After);
                // ADD SOME TEXT... EXPLANATION.
                return true;
            }
        });
        // change actor - this causes the appropriate textual popup to appear.
        helpRequestPopUp.removeActor(noActor);
        this.noActor = createNoActor(noAnimation);
        helpRequestPopUp.addActor(this.noActor);

        helpRequestPopUp.removeActor(yesActor);
        this.yesActor = createYesActor(getRegularButtonImage(true, true));
        yesActor.getListeners().clear();
        helpRequestPopUp.addActor(this.yesActor);

        //CooperativeGame.languagePrefix + "pop-ups/help_decision_pop_up/No.png"

        // no listener for yes button. it is turned off.
    }

    public void yesActionListener(){
        humanRespone = true;
        gameScreen.virtualAskState = GameScreen.VirtualAskState.HandleHumanRespone;
    }

    public void noActionListener(){
        humanRespone = false;
        gameScreen.virtualAskState = GameScreen.VirtualAskState.HandleHumanRespone;
    }

    public Image getRegularButtonImage(boolean isYes, boolean isBw){
        Skin skin = new Skin();
        String path;

        // YES button.
        if(isYes){
            if(isBw){
                path = CooperativeGame.languagePrefix + "pop-ups/help_decision_pop_up/Yes_bw.png";
            }
            else{
                path = CooperativeGame.languagePrefix + "pop-ups/help_decision_pop_up/Yes.png";
            }
        }
        // NO button.
        else{
            if(isBw){
                path = CooperativeGame.languagePrefix + "pop-ups/help_decision_pop_up/No_bw.png";
            }
            else{
                path = CooperativeGame.languagePrefix + "pop-ups/help_decision_pop_up/No.png";
            }
        }

        // TODO GWT - CHANGED! TO / INSTEAD OF //
        Texture popUp = GraphicTools.getTexture(path);
        skin.add("popUp", popUp);
        return new Image(skin, "popUp");
    }

    public Actor getNoActor() {
        return noActor;
    }

    public Actor getYesActor() {
        return yesActor;
    }
}
