package coopworld.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import coopworld.game.CooperativeGame;

/*
 * Class Name: Music
 * Description: this class plays all sounds of game.
 */
public class MusicPlayer {
    public static final MusicPlayer instance =
            new MusicPlayer();
    private Sound diamond, diamond3;
    private Sound jump;
    private Sound levelCompleted;
    private Sound uiSound;
    private Sound iceObsDisappearanceAgree, iceObsDisappearanceIgnore;
    private Sound endGame;
    private Sound notification;
    private Sound coin_fall;
    private Sound screen_shot;
    private Sound user_delete;

    private Music backgroundMusic;

    //public static HashMap<Integer, Music> instructionsSounds;
    //public static HashMap<String, Music> eventsSounds;
    public Music currMusic = null;
    public Music thanksMusic = null, comingMusic = null;

    public static float currentVolume = Constants.REGULAR_VOLUME;

    public boolean soundIsOn;

    /**
     * // singleton: prevent instantiation from other classes.
     *
     */
    private MusicPlayer(){
        soundIsOn = true;
        diamond = CooperativeGame.loader.getAssetManager().get(Constants.COIN_SOUND, Sound.class);
        //diamond3 = CooperativeGame.loader.getAssetManager().get(Constants.SPECIAL_COIN_SOUND, Sound.class);
        levelCompleted = CooperativeGame.loader.getAssetManager().
                get(Constants.LEVEL_COMPLETED_SOUND, Sound.class);
        uiSound = CooperativeGame.loader.getAssetManager().get(Constants.UI_SOUND, Sound.class);
        iceObsDisappearanceAgree = CooperativeGame.loader.getAssetManager().
                get(Constants.ICE_OBSTACLE_DISAPPEAR_TRUE_SOUND, Sound.class);
        iceObsDisappearanceIgnore = CooperativeGame.loader.getAssetManager().
                get(Constants.ICE_OBSTACLE_DISAPPEAR_FALSE_SOUND, Sound.class);
        notification = CooperativeGame.loader.getAssetManager().
                get(Constants.NOTIFICATION_SOUND, Sound.class);
        coin_fall = CooperativeGame.loader.getAssetManager().
                get(Constants.COIN_FALL_SOUND, Sound.class);
        if (CooperativeGame.loader.getAssetManager().isLoaded("sounds/camera_shutter.mp3", Sound.class)){
            screen_shot = CooperativeGame.loader.getAssetManager().get("sounds/camera_shutter.mp3", Sound.class);
        }
        if (CooperativeGame.loader.getAssetManager().isLoaded("sounds/delete_user.mp3", Sound.class)){
            user_delete = CooperativeGame.loader.getAssetManager().get("sounds/delete_user.mp3", Sound.class);
        }
        backgroundMusic = CooperativeGame.loader.getAssetManager().get("sounds/background_music.mp3",
                Music.class);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(Constants.REGULAR_VOLUME);
    }

    public static MusicPlayer getInstance() {
        return instance;
    }

    /**
     * Play "I'm coming" sound.
     */
    public void comingSound(){
        comingMusic.play();
    }

    /**
     * Play levelCompleted sound.
     */
    public void levelCompleted(){
        levelCompleted.play();
    }

    /**
     * Play thanks sound.
     */
    public void thanksSound(){
        CooperativeGame.loader.getAssetManager().get(Paths.THANKS, Music.class).play();
    }

    /**
     * Play ice obstacle disappearing sound.
     */
    public void iceObsDisappearanceAgreeSound(){
        iceObsDisappearanceAgree.play();
    }


    public void iceObsDisappearanceIgnoreSound(){
        iceObsDisappearanceIgnore.play();
    }

    public void playNotificationSound(){
        notification.play();
    }

    /**
     * Play instructions sound.
     */
    public void instructionsSound(String path){
        // if there is string (path).
        if (path != null) {
            currMusic = Gdx.audio.newMusic(Gdx.files.internal(path));
            currMusic.setVolume(.9f);
            currMusic.play();
            // free memory to avoid memory leaks.
            //currMusic.dispose();
        }
    }
    /**
     * Play jump sound.
     */
    public void jumpSound(){
        jump.play();
    }
    /**
     * Play ui sound.
     */
    public void uiSound(){
        uiSound.play(0.2f);
    }
    /**
     * Play diamond sound.
     */
    public void diamondSound(int value){
        diamond.play(0.2f);
        /*
        if (value == 10){
            diamond3.play();
        }
        else{
            diamond.play();
        }
        */
    }

    public void coinFallSound(){
        coin_fall.play();
    }

    public void screenShotSound(){
        // TODO - CHECK FOR NULL.
        if(screen_shot != null){
            screen_shot.play();
        }
    }


    public void userDeleteSound(){
        // TODO - CHECK FOR NULL.
        if(user_delete != null){
            user_delete.play();
        }
    }
    /**
     * Play background sound.
     */
    public void playBackgroundMusic(){
        backgroundMusic.play();
    }
    /**
     * Play background sound.
     */
    public boolean isBackGroundMusicPlaying(){
        return backgroundMusic.isPlaying();
    }
    /**
     * Check if instructions sound is on.
     */
    public boolean isVoicePlaying(){
        if(currMusic != null) {
            return currMusic.isPlaying();
        }
        return false;
    }
    /**
     * Pause background sound.
     */
    public void pauseMusic(){
        backgroundMusic.pause();
    }


    /**
     * end game sound.
     */
    public void playEndGameSound(){
        endGame = Gdx.audio.newSound(Gdx.files.internal(Constants.END_SCREEN_SOUND));
        endGame.play();
    }

    /**
     * Pause background sound.
     */
    public void stopMusic(){
        // instructions.
        if(currMusic != null) {
            currMusic.stop();
            currMusic.dispose();
        }
        backgroundMusic.stop();
    }

    public Music getCanYouHelpMe(boolean isInstructional) {
        return CooperativeGame.loader.getAssetManager().get(Paths.CAN_YOU_HELP_ME_STANDARD_AUDIO, Music.class);
        // TODO EXPERIMENTS SARIT - CONSIDER WHERE TO LOAD IT.
        //CooperativeGame.loader.getAssetManager().get(Paths.CAN_YOU_HELP_ME_AUDIO, Music.class).setVolume(Constants.SPEAKING_VOLUME);
    }

    public Music getVirtualAccept() {
        CooperativeGame.loader.getAssetManager().
                get(Paths.HELP_ACCEPT, Music.class).setVolume(Constants.SPEAKING_VOLUME);
        return CooperativeGame.loader.getAssetManager().
                get(Paths.HELP_ACCEPT, Music.class);
        /*
        currMusic = CooperativeGame.loader.getAssetManager().
                get("instructions_sounds/virtual_help_accept.mp3", Music.class);
        return currMusic;
        */
    }

    public Music getVirtualIgnore() {
        CooperativeGame.loader.getAssetManager().
                get(Paths.HELP_IGNORE, Music.class).setVolume(Constants.SPEAKING_VOLUME);
        return CooperativeGame.loader.getAssetManager().
                get(Paths.HELP_IGNORE, Music.class);
    }

    public void toneDownBackgroundMusic(){
        if(currentVolume > Constants.LOW_VOLUME){
            currentVolume = Constants.LOW_VOLUME;
            backgroundMusic.setVolume(currentVolume);
        }
    }

    public void toneUpBackgroundMusic(){
        if(currentVolume < Constants.REGULAR_VOLUME){
            currentVolume = Constants.REGULAR_VOLUME;
            backgroundMusic.setVolume(currentVolume);
        }
    }

    public void playVirtualAcceptAndCost(){
        this.getVirtualAccept().setVolume(Constants.SPEAKING_VOLUME);
        this.getVirtualAccept().play();
    }

    /*
    public void initLanguageBasedAudios(){
        if(CooperativeGame.virtualPlayerGender == null){
            CooperativeGame.virtualPlayerGender = "boy/";
        }
        thanksMusic = CooperativeGame.loader.getAssetManager().get(Paths.THANKS, Music.class);
    }
    */

    public Sound getUser_delete(){
        return user_delete;
    }

    public boolean isSoundIsOn() {
        return soundIsOn;
    }

    // switch sound mode and return the new mode.
    public boolean switchSoundMode(){
        soundIsOn = !soundIsOn;
        if(soundIsOn){
            playBackgroundMusic();
            //backgroundMusic.setVolume(Constants.REGULAR_VOLUME);
        }
        else{
            pauseMusic();
            //backgroundMusic.setVolume(0f);
        }
        return soundIsOn;
    }
}