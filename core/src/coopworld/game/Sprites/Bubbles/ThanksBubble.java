package coopworld.game.Sprites.Bubbles;

import coopworld.game.CooperativeGame;

/**
 * Created by User on 14/03/2017.
 */
public class ThanksBubble extends Bubble{
    public ThanksBubble(boolean isInstructionLevel) {
        this.isInstructionLevel = isInstructionLevel;
        widthRatio = 0.1f;
        heightRatio = 0.1f;

        rightXoffset = 0.1f;
        leftXoffset = 0.55f;

        this.path = CooperativeGame.languagePrefix + "bubbles/thanks/thanks.pack";
        buildBubble();
    }
}
