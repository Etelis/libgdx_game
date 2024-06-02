package coopworld.game.Sprites.Players;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;

import coopworld.game.CooperativeGame;
import coopworld.game.CrossPlatform.GeneralUtils;
import coopworld.game.Logs.Enums;
import coopworld.game.Logs.VirtualPlayerConfig;
import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Sprites.Players.Personalities.Responsible;
import coopworld.game.Tools.AiTools.DataStructures.Node;
import coopworld.game.Tools.AiTools.MatrixCell;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.GraphicTools;
import coopworld.game.Tools.RequestUtils.VirtualPlayerRequestUtils;

/**
 * Created by Eva on 9/21/2016.
 */
public abstract class VirtualPlayer extends Player implements Responsible  {
    protected boolean isFollowingRealPlayer;
    protected boolean isWaitingForPlayer;
    protected Queue<Node> callsQueue;
    protected Player p;
    protected VirtualPlayerRequestUtils virtualPlayerRequestUtils;
    protected VirtualPlayerConfig config;

    public VirtualPlayer(GeneralUtils utils, World w, Screen screen, Player p)
    {
        super(utils);
        this.p = p;
        Init(w,screen);
        this.isFollowingRealPlayer = false;
        this.isWaitingForPlayer = false;
        this.callsQueue = new Queue<Node>();
        this.virtualPlayerRequestUtils = new VirtualPlayerRequestUtils();
    }

    public void Init(World w, Screen screen){
        //System.out.println("(VirtualPlayer) Init() from " + this.getClass().getSimpleName());

        /* Open pack image of ice boy player. */
        atlas = GraphicTools.getTextureAtlas(getTextureAtlasString());

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
       /* Add frames of jump. */
        for(int i = 0; i < 6; i++){
            frames.add(new TextureRegion(this.atlas.findRegion(getTextureRegionJump()), i * 130, 0, 130, 192));
        }
        /* Pass amount of time between each frame. */
        boyJump = new Animation(0.1f, frames);
        /* Prepare to the next use. */
        frames.clear();
        /* Add frames of walk. */
        for(int i = 0; i < 6; i++){
            frames.add(new TextureRegion(this.atlas.findRegion(getTextureRegionWalk()), i * 130, 0, 130, 192));
        }
       /* Pass amount of time between each frame. */
        boyRun = new Animation(0.1f, frames);
        /* Prepare to the next use. */
        frames.clear();
        /* Add frames of Idle. */
        for(int i = 0; i < 3; i++){
            frames.add(new TextureRegion(this.atlas.findRegion(getTextureRegionIdle()), i * 130, 0, 130, 192));
        }
        /* Pass amount of time between each frame. */
        boyStand = new Animation(0.1f, frames);
        /* Prepare to the next use. */
        frames.clear();
        /* Set size. */
        //setBounds(0, 0, 200/CooperativeGame.PPM, 300/CooperativeGame.PPM);
        //setRegion(boyStand.getKeyFrame(timer, true));
        playerSprite.setBounds(0, 0, 200/ CooperativeGame.PPM, 300/CooperativeGame.PPM);
        playerSprite.setRegion(boyStand.getKeyFrame(timer, true));
    }


    public void addNodeToQueue(Node callNode)
    {
        this.callsQueue.addLast(callNode);
    }

    public boolean isCallQueueEmpty()
    {
        return (this.callsQueue.size == 0);
    }

    public abstract Node answerCall();

    public void dequeueNodeFromQueue(int row,int col)
    {
        int i = 0;
        for ( Node n : this.callsQueue)
        {
            MatrixCell matrixCell = n.getMatrixCell();
            if ((matrixCell.getX() == col) && (matrixCell.getY() == row))
            {
                this.callsQueue.removeIndex(i);
                return;
            }
            i++;
        }
    }
    /*public void goToCoin() {
         float x = this.playerSprite.getX();
        //float y = this.playerSprite.getY();
        if (this.virtualCoins.getCount() > this.coinsCounter) {
            MapObject m = this.virtualCoins.get(this.coinsCounter);
            {
                Rectangle rect = ((RectangleMapObject) m).getRectangle();
                float coinX = rect.getX();
                float coinY = rect.getX();
                if (x < coinX) {
                    //go right to get coin
                    if(this.b2body_boy.getLinearVelocity().x < 7) {
                        this.b2body_boy.applyLinearImpulse(new Vector2(0.1f, 0), this.b2body_boy.getWorldCenter(), true);
                        if (this.playerSprite.getX() >= coinX) { // coin reached
                            this.b2body_boy.setLinearVelocity(new Vector2(0f, 0));
                            coinsCounter++;
                        }
                    }
                } else {
                    //go left to get coin
                    if(this.b2body_boy.getLinearVelocity().x > -7) {
                        this.b2body_boy.applyLinearImpulse(new Vector2(0.1f, 0), this.b2body_boy.getWorldCenter(), true);
                        if (this.playerSprite.getX() <= coinX) { // coin reached
                            this.b2body_boy.setLinearVelocity(new Vector2(0f, 0));
                            coinsCounter++;
                        }
                    }
                }
            }

        }
    }

    */


    public void followRealPlayer()
    {
        this.isFollowingRealPlayer = true;
    }

    public void stopFollowingRealPlayer()
    {
        this.isFollowingRealPlayer = false;
    }

    public boolean isFollowingRealPlayer()
    {
        return this.isFollowingRealPlayer;
    }
    /**
     * Those are all ice girl player needed files/configurations
     */
    public String getTextureAtlasString()
    {
        return p.getTextureAtlasString();
    }

    protected  String getTextureRegionJump()
    {
        return p.getTextureRegionJump();
    }
    protected  String getTextureRegionWalk()
    {
        return p.getTextureRegionWalk();
    }
    protected  String getTextureRegionIdle()
    {
        return p.getTextureRegionIdle();
    }

    /**
     * Default first location in 1,1 - derived class might override this to change
     * their first location if needed
     * @param bdef body def to set position
     *             ??????????????????????? +6 from tiled ????????????????
     */
    @Override
    public void SetFirstLocation(BodyDef bdef)
    {
        bdef.position.set(12, 1);
    }



    /**
     * defines player location and fixtures
     */
    @Override
    protected void InitPlayer() {
        //System.out.println("(VirtualPlayer) InitPlayer() from " + this.getClass().getSimpleName());

        int radius = 64;
        int xEdge = radius + 1;


        BodyDef bdef = new BodyDef();
        /*set position of player, dynamic position*/
        /* The first place of the player. */
        SetFirstLocation(bdef);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body_boy = world_boy.createBody(bdef);

        /*HEAD*/
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        /* It is the distance between the player and the ground. */
        circleShape.setRadius(radius / CooperativeGame.PPM);
        /* Category means - what is this fixture?. */
        fixtureDef.filter.categoryBits = Constants.boyBit;
        /* What the player can collide with. */
        declareCollisions(fixtureDef);
        fixtureDef.shape = circleShape;
        b2body_boy.createFixture(fixtureDef);
        /* a line between two different points. */
        EdgeShape boyHead = new EdgeShape();

        /* Define this line. */
        boyHead.set(new Vector2(-xEdge/CooperativeGame.PPM, 170/CooperativeGame.PPM),
                new Vector2(xEdge/CooperativeGame.PPM, 170/CooperativeGame.PPM));
        fixtureDef.shape = boyHead;
        /* a sensor does not collide with anything, it just let us the ability
        to ask for a query and get data. */
        fixtureDef.isSensor = true;
        b2body_boy.createFixture(fixtureDef).setUserData("VIRTUAL_BOY_HEAD");


        /*LEGS*/
        FixtureDef fdefLegs = new FixtureDef();
        CircleShape shapeLegs = new CircleShape();
        /* It is the distance between the player and the ground*/
        //shapeLegs.setRadius(64 / CooperativeGame.PPM);
        /* Category means - what is this fixture?*/
        fdefLegs.filter.categoryBits = Constants.boyBit;
        /* What the player can collide with. */
        declareCollisions(fdefLegs);
        /*a line between two different points*/
        EdgeShape boyLegs = new EdgeShape();
        /*Define this line*/
        boyLegs.set(new Vector2(-xEdge/CooperativeGame.PPM, -75/CooperativeGame.PPM),
                new Vector2(xEdge/CooperativeGame.PPM, -75/CooperativeGame.PPM));
        fdefLegs.shape = boyLegs;
        /*a sensor does not collide with anything, it just let us the ability
        to ask for a query and get data*/
        fdefLegs.isSensor = true;
        b2body_boy.createFixture(fdefLegs).setUserData("VIRTUAL_BOY_LEGS");


        /*RIGHT*/
        FixtureDef fdefRight = new FixtureDef();
        CircleShape shapeRight = new CircleShape();
        // category means - what is this fixture?
        fdefRight.filter.categoryBits = Constants.boyBit;
        /* What the player can collide with. */
        declareCollisions(fdefRight);
        /* a line between two different points. */
        EdgeShape boyRight = new EdgeShape();
        /* Define this line*/
        boyRight.set(new Vector2(xEdge/CooperativeGame.PPM, -75/CooperativeGame.PPM),
                new Vector2(xEdge/CooperativeGame.PPM, 170/CooperativeGame.PPM));
        fdefRight.shape = boyRight;
        /*a sensor does not collide with anything, it just let us the ability
        to ask for a query and get data*/
        fdefRight.isSensor = true;
        b2body_boy.createFixture(fdefRight).setUserData("VIRTUAL_BOY_RIGHT");


        /*LEFT*/
        FixtureDef fdefLeft = new FixtureDef();
        CircleShape shapeLeft = new CircleShape();
        /* It is the distance between the player and the ground. */
        // Category means - what is this fixture?.
        fdefLeft.filter.categoryBits = Constants.boyBit;
        // What the player can collide with.
        declareCollisions(fdefLeft);
        // a line between two different points.
        EdgeShape boyLeft = new EdgeShape();
        /* Define this line. */
        boyLeft.set(new Vector2(-xEdge/CooperativeGame.PPM, -75/CooperativeGame.PPM),
                new Vector2(-xEdge/CooperativeGame.PPM, 170/CooperativeGame.PPM));
        fdefLeft.shape = boyLeft;
        // a sensor does not collide with anything, it just let us the ability
        // to ask for a query and get data.
        fdefLeft.isSensor = true;
        b2body_boy.createFixture(fdefLeft).setUserData("VIRTUAL_BOY_LEFT");
    }

    public void declareCollisions(FixtureDef fixtureDef){
        // Pay mode. Virtual Player ignore ice obstacles.
        if(CooperativeGame.gameData.getGame_params().getGame_help_logic() == Enums.HelpLogic.Pay) {
            fixtureDef.filter.maskBits = Constants.defaultBit | Constants.vCoinBit |
                    Constants.brickBit | Constants.natureObsBit;
        }
        // Walk mode.
        else if(CooperativeGame.gameData.getGame_params().getGame_help_logic() == Enums.HelpLogic.Walk){
            fixtureDef.filter.maskBits = Constants.defaultBit | Constants.vCoinBit |
                    Constants.brickBit | Constants.natureObsBit | Constants.iceObsBit;
        }
    }

    // before virtual player stands next to a blocked ice cube - check if her face is in the
    // right direction. if not- flip.
    // In MatrixCells - getX returns col number.
    public void flipVirtualPlayerIfNeeded(){
        MatrixCell blockedVirtualCoin = virtualPlayerRequestUtils.getBlockedElementMatrixCell();
        MatrixCell virtualPlayerRightLeg = new MatrixCell(
                Math.round(GameScreen.mapHeight - (this.b2body_boy.getPosition().y
                        * CooperativeGame.PPM) / GameScreen.mapCellWidth), Math.round
                ((this.b2body_boy.getPosition().x
                        * CooperativeGame.PPM) / GameScreen.mapCellWidth));

        // true when the player is standing to the right of the virtual coin and also looks right.
        boolean flipFromRightToLeft = (virtualPlayerRightLeg.getX() > blockedVirtualCoin.getX())
                && (this.isRight);

        // true when the player is standing to the left of the virtual coin and also looks left.
        boolean flipFromLeftToRight = (virtualPlayerRightLeg.getX() < blockedVirtualCoin.getX())
                && (!(this.isRight));

        // change flip field if needed.
        if(flipFromRightToLeft || flipFromLeftToRight){
            this.isRight = !this.isRight;
        }

        /*
        if(flipFromLeftToRight){
            isRight = true;
        }
        if(flipFromRightToLeft){
            isRight = false;
        }
        */
    }

    public VirtualPlayerRequestUtils getVirtualPlayerRequestUtils() {
        return virtualPlayerRequestUtils;
    }

    public VirtualPlayerConfig getConfig() {
        return config;
    }

    public void setConfig(VirtualPlayerConfig config) {
        this.config = config;
    }

    public int getVirtPlayerCol(int mapCellWidth) {
        // compute players positions
        return Math.round((b2body_boy.getPosition().x
                * CooperativeGame.PPM) / mapCellWidth);
    }

    public int getVirtPlayerRow(int mapCellWidth, int mapHeight) {
        return Math.round(mapHeight - (b2body_boy.getPosition().y
                * CooperativeGame.PPM) / mapCellWidth);
    }
}
