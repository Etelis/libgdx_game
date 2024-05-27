package coopworld.game.Tools;

/**
 * Created by User on 01/03/2017.
 */
public class Paths {
    public static String FIRST_INST_VIEW = "";
    public static String FIRST_INST_VOICE = "";

    public static String CAN_YOU_HELP_ME_BUBBLE_PACK = "";

    // human gender is relevant
    public static String CAN_YOU_HELP_ME_STANDARD_AUDIO = "";
    //public static String CAN_YOU_HELP_ME_INSTRUCTIONS_AUDIO = "";
    // human gender is not relevant
    public static String THANKS = "";
    public static String HELP_ACCEPT = "";
    public static String HELP_IGNORE = "";

    public static String PLAYER_LEVELS_PACK = "";

    public static void init(){
        FIRST_INST_VIEW = "instructions/1";
        FIRST_INST_VOICE = "instructions_sounds/1";

        CAN_YOU_HELP_ME_BUBBLE_PACK = "bubbles/can_you_help_me";

        // AUDIOS
        // human gender is relevant
        CAN_YOU_HELP_ME_STANDARD_AUDIO = "can you help me/help_me_standard";
        //CAN_YOU_HELP_ME_INSTRUCTIONS_AUDIO = "can you help me/help_me_instructions";

        // human gender is not relevant
        THANKS = "thanks";
        HELP_ACCEPT = "virtual_help_accept";
        HELP_IGNORE = "virtual_help_ignore";
    }
}
