package coopworld.game.Sprites.Bubbles;

import coopworld.game.Tools.Paths;

/**
 * Created by User on 14/03/2017.
 */
public class CanYouHelpMeBubble extends Bubble{
    public CanYouHelpMeBubble(boolean isInstructionLevel) {
        this.isInstructionLevel = isInstructionLevel;
        widthRatio = 0.13f;
        heightRatio = 0.13f;

        rightXoffset = 0.06f;
        leftXoffset = 0.50f;
        /*
        // in instruction mode - the bubbles should be bigger.
        if(isInstructionLevel){
            widthRatio = 0.2f;
            heightRatio = 0.2f;

            rightXoffset = 0;
            leftXoffset = 0.50f;
        }
        else{
            widthRatio = 0.13f;
            heightRatio = 0.13f;

            rightXoffset = 0.05f;
            leftXoffset = 0.50f;
        }
         */
        this.path = Paths.CAN_YOU_HELP_ME_BUBBLE_PACK;
        buildBubble();
    }
}