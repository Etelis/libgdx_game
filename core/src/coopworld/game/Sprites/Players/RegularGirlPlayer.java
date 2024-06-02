package coopworld.game.Sprites.Players;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import coopworld.game.CooperativeGame;
import coopworld.game.CrossPlatform.GeneralUtils;
import coopworld.game.Tools.GraphicTools;

/*
 * Class Name: RegularGirlPlayer
 * Description: extends player represents regular girl player
 */
public class RegularGirlPlayer extends Player {
    /**
     * constructor with the world and the game screen
     * calls the constructor of player.
     *
     * @param w - world of game.
     * @param screen - the game screen.
     *
     */
    public RegularGirlPlayer(World w, Screen screen, GeneralUtils utils) {
        super(utils);
        Init(w, screen);
    }

    /**
     * initialize regular girl player parameters
     *
     * @param w - world of game.
     * @param screen - the game screen.
     * */
    public void Init(World w, Screen screen){
        /* Open pack image og regular girl player. */
        textureAtlasString = "players_animations/human/girl/regular_girl_pack.pack";
        atlas = GraphicTools.getTextureAtlas(textureAtlasString);
        /* Initialize variables. */
        this.world_boy = w;
        this.currState = State.STANDING;
        this.prevState = State.STANDING;
        this.timer = 0;
        this.isRight = true;
         /* Function of mother class : Player. */
        InitPlayer();
        /* This array will help initialize the animations later on. */
        Array<TextureRegion> frames = new Array<TextureRegion>();
        /* Add frames of idle. */
        for(int i = 0; i < 4; i++){
            frames.add(new TextureRegion(this.atlas.findRegion("Girl_Idle"), i * 130, 0, 130, 192));
        }
        /* Pass amount of time between each frame. */
        boyStand = new Animation(0.1f, frames);
        /* Prepare to the next use. */
        frames.clear();
        /* Add frames of walk. */
        for(int i = 0; i < 6; i++){
            frames.add(new TextureRegion(this.atlas.findRegion("Girl_Walk"), i * 130, 0, 130, 192));
        }
        /* Pass amount of time between each frame. */
        boyRun = new Animation(0.1f, frames);
        /* Prepare to the next use. */
        frames.clear();
        /* Add frames of jump. */
        for(int i = 0; i < 3; i++){
            frames.add(new TextureRegion(this.atlas.findRegion("Girl_Jump"), i * 130, 0, 130, 192));
        }
        /* Pass amount of time between each frame. */
        boyJump = new Animation(0.1f, frames);
        /* Prepare to the next use. */
        frames.clear();
        /* Set size. */
        /*
        setBounds(0, 0, 200/CooperativeGame.PPM, 300/CooperativeGame.PPM);
        setRegion(boyStand.getKeyFrame(timer, true));
        */
        playerSprite.setBounds(0, 0, 200/ CooperativeGame.PPM, 300/CooperativeGame.PPM);
        playerSprite.setRegion(boyStand.getKeyFrame(timer, true));
    }

    protected  String getTextureRegionJump()
    {
        return "Girl_Jump";
    }
    protected  String getTextureRegionWalk()
    {
        return "Girl_Walk";
    }
    protected  String getTextureRegionIdle()
    {
        return "Girl_Idle";
    }
}
