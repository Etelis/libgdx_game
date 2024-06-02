package coopworld.game.Tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import coopworld.game.CooperativeGame;
import coopworld.game.Logs.Enums;

import static coopworld.game.Logs.Enums.InstructionsMethod.Combined;

/**
 * Created by User on 13/01/2017.
 */
public class Loader {
    public static AssetManager assetManager;

    public Loader() {
        assetManager = new AssetManager();
    }

    public static AssetManager getAssetManager() {
        return assetManager;
    }

    public void load(){
        /* Load game sounds. */
        assetManager.load(Constants.COIN_SOUND, Sound.class);
        assetManager.load(Constants.LEVEL_COMPLETED_SOUND, Sound.class);
        assetManager.load(Constants.UI_SOUND, Sound.class);
        assetManager.load(Constants.ICE_OBSTACLE_DISAPPEAR_TRUE_SOUND, Sound.class);
        assetManager.load(Constants.ICE_OBSTACLE_DISAPPEAR_FALSE_SOUND, Sound.class);
        assetManager.load(Constants.COIN_FALL_SOUND, Sound.class);
        assetManager.load(Constants.NOTIFICATION_SOUND, Sound.class);

        assetManager.load("sounds/background_music.mp3", Music.class);

        // Texture loading.
        // gameScreen
        assetManager.load("buttons/arrows.pack", TextureAtlas.class);
        assetManager.load("hud/score_animation.pack", TextureAtlas.class);
        assetManager.load("hud/coin_animation.pack", TextureAtlas.class);

        assetManager.load("pay/girl/ice_cube_throwing.pack", TextureAtlas.class);
        assetManager.load("pay/boy/ice_cube_throwing.pack", TextureAtlas.class);
        assetManager.load("buttons/volume.pack", TextureAtlas.class);

        // levels screen
        //assetManager.load("levels/one_star.pack", TextureAtlas.class);
        //assetManager.load("levels/two_stars.pack", TextureAtlas.class);
        assetManager.load("levels/three_stars.pack", TextureAtlas.class);

        assetManager.load("levels/locked.pack", TextureAtlas.class);

        // buttons
        assetManager.load("buttons/back.pack", TextureAtlas.class);
        assetManager.load("buttons/exit.pack", TextureAtlas.class);
        assetManager.load("buttons/girl/help.pack", TextureAtlas.class);

        // help button for the combined instructions mode.
        if(CooperativeGame.gameData.getGame_params().getInstructions_method() == Combined){
            assetManager.load("buttons/boy/help.pack", TextureAtlas.class);
        }
        assetManager.load("buttons/exit.pack", TextureAtlas.class);
        assetManager.load("login/registration_screen/gender_choose.pack", TextureAtlas.class);

        // players
        //assetManager.load("players_animations/regular_boy_pack.pack", TextureAtlas.class);
        //assetManager.load("players_animations/girl_pack.pack", TextureAtlas.class);

        // textures //
        // gameScreen
        assetManager.load("menu_options/MainMenu.png", Texture.class);
        assetManager.load("hud/coin_score.png", Texture.class);
        assetManager.load("hud/watch.png", Texture.class);
        assetManager.load("menu_options/MainMenu.png", Texture.class);
        assetManager.load(CooperativeGame.languagePrefix + "screens/registration_screen/background.png", Texture.class);
        assetManager.load("login/registration_screen/text_box.png", Texture.class);
        assetManager.load(CooperativeGame.languagePrefix + "screens/invalid_code.png", Texture.class);
        assetManager.load(CooperativeGame.languagePrefix + "buttons/play.png", Texture.class);
        assetManager.load("screens/start_code/text_box_start_code.png", Texture.class);



        assetManager.load(CooperativeGame.languagePrefix + "screens/start_code/start_code_screen.png", Texture.class);
        assetManager.load(CooperativeGame.languagePrefix + "screens/start_code/invalid_start_code.png", Texture.class);
        assetManager.load(CooperativeGame.languagePrefix + "screens/start_code/problem_with_code.png", Texture.class);


        assetManager.load(CooperativeGame.languagePrefix + "stars/3.png", Texture.class);
        assetManager.load(CooperativeGame.languagePrefix + "stars/3_new_score.png", Texture.class);
        assetManager.load(CooperativeGame.languagePrefix + "buttons/continue.png", Texture.class);
        assetManager.load("buttons/back.png", Texture.class);

        assetManager.load("login/user_select_background.png", Texture.class);
        assetManager.load("fonts/myFont.fnt", BitmapFont.class);


        assetManager.load("login/girl_button.png", Texture.class);
        assetManager.load("login/boy_button.png", Texture.class);
        assetManager.load("login/buttons.pack", TextureAtlas.class);

        assetManager.load("buttons/screen_shot.pack", TextureAtlas.class);
        assetManager.load("sounds/camera_shutter.mp3", Sound.class);

        assetManager.finishLoading();
    }

    public void loadPlayersPackFiles(Enums.Player playerType, Enums.Gender playerGender) {
        String prefix = "players_animations";
        String postfix = "";

        String typeStr = "", genderStr = "";

        // player type
        if(playerType == Enums.Player.Human){
            typeStr = "human";
            postfix = "regular_";
        }
        else if(playerType == Enums.Player.Virtual){
            typeStr = "virtual";
        }

        // player gender
        if(playerGender == Enums.Gender.Male){
            genderStr = "boy";
        }
        else if(playerGender == Enums.Gender.Female){
            genderStr = "girl";
        }


        // TODO MAKE IT CLEARER AND FREE MEMORY!
        if(playerType == Enums.Player.Virtual){
            String[] relevantPacks = {"combined", "orange"};
            for(String relevantPack : relevantPacks){
                String path = prefix + "/" + typeStr + "/" + genderStr + "/" + postfix + genderStr + "_pack" + "_" + relevantPack + ".pack";
                assetManager.load(path, TextureAtlas.class);
            }
        }
        else{ // HUMAN
            String path = prefix + "/" + typeStr + "/" + genderStr + "/" + postfix + genderStr + "_pack.pack";
            assetManager.load(path, TextureAtlas.class);
        }
    }

    public void updateVisualInstructionsPaths(Enums.Gender virtualGender, Enums.Gender playerGender){
        String postfixPng;
        String postfixMp3;
        String postfixLevels;

        switch (playerGender) {
            case Female:
                postfixPng = "_girl.png";
                postfixMp3 = "_girl.mp3";
                postfixLevels = "Girl.pack";
                break;

            case Male:
                postfixPng = "_boy.png";
                postfixMp3 = "_boy.mp3";
                postfixLevels = "Boy.pack";
                break;

            default:
                postfixPng = "_boy.png";
                postfixMp3 = "_boy.mp3";
                postfixLevels = "Boy.pack";
                break;
        }
        // in the English version - no difference between boy and girl (sounds).
        if(Constants.GAME_LANGUAGE.getLanguage() == Enums.Language.English){
            postfixMp3 = ".mp3";
        }

        Paths.PLAYER_LEVELS_PACK = "levels/currNumbers" + postfixLevels;

        Paths.FIRST_INST_VIEW += postfixPng;
        Paths.FIRST_INST_VIEW = CooperativeGame.languagePrefix + Paths.FIRST_INST_VIEW;

        Paths.FIRST_INST_VOICE += postfixMp3;
        Paths.FIRST_INST_VOICE = CooperativeGame.languagePrefix + Paths.FIRST_INST_VOICE;

        loadVisualInstructionsPaths();
    }

    public void updatePaths(Enums.Gender virtualGender, Enums.Gender playerGender){
        String postfixPack;

        //todo - loading logic. gender vs. language.
        if(Constants.GAME_LANGUAGE.getLanguage() == Enums.Language.English){
            postfixPack = ".pack";
        }
        // HEBREW - here the human player gender is not relevant.
        else{
            switch (playerGender) {
                case Female:
                    postfixPack = "_girl.pack";
                    break;

                case Male:
                    postfixPack = "_boy.pack";
                    break;

                default:
                    postfixPack = "_boy.pack";
                    break;
            }
        }

        Paths.CAN_YOU_HELP_ME_BUBBLE_PACK += postfixPack;
        Paths.CAN_YOU_HELP_ME_BUBBLE_PACK = CooperativeGame.languagePrefix + Paths.CAN_YOU_HELP_ME_BUBBLE_PACK;

        assetManager.load(Paths.CAN_YOU_HELP_ME_BUBBLE_PACK, TextureAtlas.class);

        // TODO - WAS DELETED - CONSIDER HEBREW!
        //assetManager.load(Paths.VIRTUAL_COST_AUDIO, Music.class);

        //assetManager.finishLoading();
    }

    public void loadVisualInstructionsPaths(){
        // visual instructions (the screen before the level).
        assetManager.load(Paths.FIRST_INST_VIEW, Texture.class);

        assetManager.load(Paths.PLAYER_LEVELS_PACK, TextureAtlas.class);


        // no need for it. redundant information.
        //assetManager.load(Paths.FIFTH_INST_VIEW, Texture.class);

        //assetManager.finishLoading();
        // no need to load the audio element (will be done in MusicPlayer).
    }

    // can you help me audio. JUST IN ENGLISH?
    public void loadAudios(Enums.Gender virtualGender, Enums.Gender playerGender){
        String postfix = ".mp3";

        String fileNameToLoad;

        String virtualGenderReference = "";
        // set virtual gender
        if(virtualGender == Enums.Gender.Female){
            virtualGenderReference = "virtual_girl";
        }
        else if(virtualGender == Enums.Gender.Male){
            virtualGenderReference = "virtual_boy";
        }
        String humanGenderReference = "";

        if(Constants.GAME_LANGUAGE.getLanguage() == Enums.Language.Hebrew || Constants.GAME_LANGUAGE.getLanguage() == Enums.Language.Arabic) {
            // set human gender
            if (playerGender == Enums.Gender.Female) {
                humanGenderReference = "_girl";
            } else if (playerGender == Enums.Gender.Male) {
                humanGenderReference = "_boy";
            }
        }

        // human gender is relevant
        /*
        Paths.CAN_YOU_HELP_ME_INSTRUCTIONS_AUDIO = CooperativeGame.languagePrefix + "instructions_sounds/" +
                virtualGenderReference + "/" + Paths.CAN_YOU_HELP_ME_INSTRUCTIONS_AUDIO + "_" + humanGenderReference + postfix;
         */
        Paths.CAN_YOU_HELP_ME_STANDARD_AUDIO = CooperativeGame.languagePrefix + "instructions_sounds/" +
                virtualGenderReference + "/" + Paths.CAN_YOU_HELP_ME_STANDARD_AUDIO + humanGenderReference + postfix;

        String[] specificHumanGender = {Paths.CAN_YOU_HELP_ME_STANDARD_AUDIO};
        //String[] specificHumanGender = {Paths.CAN_YOU_HELP_ME_INSTRUCTIONS_AUDIO, Paths.CAN_YOU_HELP_ME_STANDARD_AUDIO};
        for(String fileName : specificHumanGender){
            assetManager.load(fileName, Music.class);
        }

        // human gender is not relevant
        Paths.THANKS = CooperativeGame.languagePrefix + "instructions_sounds/" +
                virtualGenderReference + "/" + Paths.THANKS + postfix;
        Paths.HELP_ACCEPT = CooperativeGame.languagePrefix + "instructions_sounds/" +
                virtualGenderReference + "/" + Paths.HELP_ACCEPT + postfix;
        Paths.HELP_IGNORE = CooperativeGame.languagePrefix + "instructions_sounds/" +
                virtualGenderReference + "/" + Paths.HELP_IGNORE + postfix;
        String[] noSpecificHumanGender = {Paths.THANKS, Paths.HELP_ACCEPT, Paths.HELP_IGNORE};
        for(String fileName : noSpecificHumanGender){
            assetManager.load(fileName, Music.class);
        }
    }

    public void loadAssetsByGender(Enums.Gender virtualGender, Enums.Gender humanGender){
        updateVisualInstructionsPaths(virtualGender, humanGender);
        updatePaths(virtualGender, humanGender);
        loadAudios(virtualGender, humanGender);

        loadPlayersPackFiles(Enums.Player.Human, humanGender);
        loadPlayersPackFiles(Enums.Player.Virtual, virtualGender);

        assetManager.finishLoading();
    }

    // the play button that need to be loaded according to the language.
    public void loadInitialLanguageBased(){
        // menuScreen - "PLAY"
        assetManager.load(CooperativeGame.languagePrefix + "buttons/play.png", Texture.class);
        assetManager.finishLoading();
    }

    public void languageBasedLoad(){
        // Texture loading.
        // gameScreen
        assetManager.load(CooperativeGame.languagePrefix + "buttons/next.pack", TextureAtlas.class);
        // menuScreen - "PLAY"
        //assetManager.load(CooperativeGame.languagePrefix + "buttons/play.png", Texture.class);

        // bubbles TODO - JUST IN WALK VERSION.
        //assetManager.load(CooperativeGame.languagePrefix + "bubbles/come to help/come to help.pack", TextureAtlas.class);

        assetManager.load(CooperativeGame.languagePrefix + "screens/levels_screen/level_select.png", Texture.class);

        // end level
        //assetManager.load(CooperativeGame.languagePrefix + "stars/stars.pack", TextureAtlas.class);

        //assetManager.load(CooperativeGame.languagePrefix + "buttons/continue.pack", TextureAtlas.class);

        assetManager.load(CooperativeGame.languagePrefix + "hud/high_score_show.png", Texture.class);

        assetManager.load(CooperativeGame.languagePrefix + "bubbles/thanks/thanks.pack", TextureAtlas.class);

        assetManager.load(CooperativeGame.languagePrefix + "pop-ups/virtual_response/girl/accept_help.png", Texture.class);
        assetManager.load(CooperativeGame.languagePrefix + "pop-ups/virtual_response/girl/ignore_help.png", Texture.class);
        assetManager.load(CooperativeGame.languagePrefix + "pop-ups/virtual_response/boy/accept_help.png", Texture.class);
        assetManager.load(CooperativeGame.languagePrefix + "pop-ups/virtual_response/boy/ignore_help.png", Texture.class);

        //assetManager.load("eng_res/" + "screens/end_screen/EndWinningScreen_Male.png", Texture.class);
        // TODO COOP 2 - NOT NEEDED TO LOAD BOTH GENDERS
        assetManager.load("heb_res/" + "screens/end_screen/EndWinningScreen_girl.png", Texture.class);
        assetManager.load("heb_res/" + "screens/end_screen/EndWinningScreen_boy.png", Texture.class);
        assetManager.load("heb_res/" + "screens/end_screen/EndSessionScreen_girl.png", Texture.class);
        assetManager.load("heb_res/" + "screens/end_screen/EndSessionScreen_boy.png", Texture.class);

        // TODO - CONDITIONAL
        assetManager.load("heb_res/" + "screens/end_screen/intervention/1_EndSessionScreen_girl.png", Texture.class);
        assetManager.load("heb_res/" + "screens/end_screen/intervention/1_EndSessionScreen_boy.png", Texture.class);
        assetManager.load("heb_res/" + "screens/end_screen/intervention/3_EndSessionScreen_girl.png", Texture.class);
        assetManager.load("heb_res/" + "screens/end_screen/intervention/3_EndSessionScreen_boy.png", Texture.class);
        assetManager.load("heb_res/" + "screens/end_screen/intervention/5_EndSessionScreen_girl.png", Texture.class);
        assetManager.load("heb_res/" + "screens/end_screen/intervention/5_EndSessionScreen_boy.png", Texture.class);

        CooperativeGame.loader.getAssetManager().load("Tiled/background.png", Texture.class);

        //assetManager.load(CooperativeGame.languagePrefix + "screens/end_screen/EndWinningScreen_Female.png", Texture.class);

        //assetManager.load(CooperativeGame.languagePrefix + "pop-ups/special_coin_explanation.png", Texture.class);

        //assetManager.finishLoading();
    }

    public void loadContinualVersion(){
        assetManager.load("buttons/screen_shot.pack", TextureAtlas.class);

    }

    public void loadNeededAssets(){
        // LANGUAGE BASED.
        // Load needed assets.
        languageBasedLoad();

        loadAssetsByGender(CooperativeGame.gameData.getUser().getVirtual_gender(), CooperativeGame.gameData.getUser().getHuman_gender());
    }
}