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
 * Class Name: IceGirlPlayer
 * Description: extends player represents ice girl player
 */
public class IceGirlPlayer extends Player {
    /**
     * constructor with the world and the game screen
     * calls the constructor of player.
     *
     * @param w - world of game.
     * @param screen - the game screen.
     *
     */
    public IceGirlPlayer(World w, Screen screen, GeneralUtils utils) {
        super(utils);
        Init(w, screen);
    }

    /**
     * initialize ice girl player parameters
     *
     * @param w - world of game.
     * @param screen - the game screen.
     * */
    public void Init(World w, Screen screen){
        //System.out.println("(IceGirlPlayer) Init() from " + this.getClass().getSimpleName());
        textureAtlasString = "players_animations/virtual/girl/girl_pack_combined.pack";
        atlas = GraphicTools.getTextureAtlas(textureAtlasString);

        /* Initialize variables*/
        this.world_boy = w;
        this.currState = State.STANDING;
        this.prevState = State.STANDING;
        this.timer = 0;
        this.isRight = true;
         /* Function of mother class : Player. */
        InitPlayer();
        /* This array will help initialize the animations later on.*/
        Array<TextureRegion> frames = new Array<TextureRegion>();
        /* Add frames of jump. */
        for(int i = 0; i < 6; i++){
            frames.add(new TextureRegion(this.atlas.findRegion("Girl_Jump"), i * 130, 0, 130, 192));
        }
        /* Pass amount of time between each frame*/
        boyJump = new Animation(0.1f, frames);
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
        /* Add frames of Idle. */
        for(int i = 0; i < 3; i++){
            frames.add(new TextureRegion(this.atlas.findRegion("Girl_Idle"), i * 130, 0, 130, 192));
        }
       /* Pass amount of time between each frame*/
        boyStand = new Animation(0.1f, frames);
        /* Prepare to the next use. */
        frames.clear();
        /* Set size. */
        playerSprite.setBounds(0, 0, 200/ CooperativeGame.PPM, 300/CooperativeGame.PPM);
        playerSprite.setRegion(boyStand.getKeyFrame(timer, true));
        /*
        setBounds(0, 0, 200/ coopworld.game.CooperativeGame.PPM, 300/ coopworld.game.CooperativeGame.PPM);
        setRegion(boyStand.getKeyFrame(timer, true));
        */
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
