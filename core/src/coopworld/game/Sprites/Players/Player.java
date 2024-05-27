package coopworld.game.Sprites.Players;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import coopworld.game.CooperativeGame;
import coopworld.game.CrossPlatform.GeneralUtils;
import coopworld.game.Logs.Enums;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.RequestUtils.HumanPlayerRequestUtils;

/*
 * Class Name: Player
 * Description: super class of all types of players (ice/regular boy/girl)
 * represents the player
 */
public abstract class Player {
    protected Animation<TextureRegion> boyRun, boyJump, boyStand;
    /* Measure time of the animation. */
    protected float timer;
    public World world_boy;
    float buff = 0;
    public Body b2body_boy;
    //public Animation boyStand;
    protected TextureAtlas atlas;
    public enum State {FALLING, JUMPING, STANDING, RUNNING};
    public State currState, prevState;
    public Sprite playerSprite = new Sprite();
    long elapsedTime = 0;
    long currentTime = 0;
    long lastJumpTime = 0;
    long JUMP_DELAY = 1000000000L;
    GeneralUtils utils;
    boolean isRight = true;

    /**
     * Abstract methods - each derived player will have its own.
     */
    protected abstract String getTextureRegionJump();
    protected abstract String getTextureRegionWalk();
    protected abstract String getTextureRegionIdle();

    protected HumanPlayerRequestUtils humanPlayerRequestUtils;

    protected String textureAtlasString;

    public Player(GeneralUtils generalUtils) {
        this.utils = generalUtils;
        this.humanPlayerRequestUtils = new HumanPlayerRequestUtils();
    }

    /**
     * initialize player parameters
     *
     * @param w - world of game.
     * @param screen - the game screen.
     * */
    public abstract void Init(World w, Screen screen);

    /**
     *  Attach the boy pic to the circle that represent the player.
     * @param dt : time
     */
    public void update(float dt){
        playerSprite.setPosition(b2body_boy.getPosition().x - playerSprite.getWidth() / 2, (b2body_boy.getPosition().y -
                playerSprite.getHeight() / 2) + (float)0.5);
        playerSprite.setRegion(getFrame(dt));
    }

    public Sprite getPlayerSprite() {
        return playerSprite;
    }

    /**
     * Returns the wanted frame to show.
     * @param dt : time
     * @return : frame
     */
    public TextureRegion getFrame(float dt){
        currState = getState();
        TextureRegion region;
        /* Return frame according to current state. */
        switch(currState){
            case JUMPING:
                region = boyJump.getKeyFrame(timer);
                break;
            case RUNNING:
                region = boyRun.getKeyFrame(timer, true);
                break;
            /* If the player fall or stand its the same frame. */
            case FALLING:
            case STANDING:
            default:
                region = boyStand.getKeyFrame(timer, true);
                break;
        }

        /* stateTimer - check if currState equal prevState. 0 - reset the timer*/
        if (currState == prevState){
            buff = timer + dt;
        }
        else{
            buff = 0;
        }
        timer = buff;
        prevState = currState;
        return region;
    }
    /**
     * get state of player according to its x & y velocities
     * @return : state
     */
    public State getState(){
        /*
        * if player has y positive velocity(jumping)-1st condition,or if he has
        * y negative velocity (falling) but the previous state was jumping
        * (2nd condition) - return jump state.
        */
        if(b2body_boy.getLinearVelocity().y > 0 || (b2body_boy.getLinearVelocity().y < 0 &&
                this.prevState == State.JUMPING)){
            return State.JUMPING;
        }
        /*
        * if player has y negative velocity(falling) - return Falling state.
        */
        else if(b2body_boy.getLinearVelocity().y < 0){
            return State.FALLING;
        }
        /*
        * if player has x velocity(running) - return running state.
        */
        else if(b2body_boy.getLinearVelocity().x != 0) {
            return State.RUNNING;
        }
        /*else return standing state*/
        else
            return State.STANDING;
    }

    /**
     * Default first location in 1,3 - derived class might override this to change
     * their first location if needed
     * @param bdef body def to set position
     */
    public void SetFirstLocation(BodyDef bdef)
    {
        bdef.position.set(3, 1);
    }



    /**
     * defines player location
     */
    protected void InitPlayer() {
        //todo - the first if statement avoids double creation of bodies (fixtures).
        // Remember to change the inheritance so that it will not be necessary.
        if(this.getClass().getSimpleName().equals("IceGirlPlayer") ||
                this.getClass().getSimpleName().equals("Ice_Boy_Player")){
            return;
        }
        //System.out.println("(Player) InitPlayer() from " + this.getClass().getSimpleName());

        BodyDef bdef = new BodyDef();
        /* The first place of the player. */
        // SetFirstLocation(bdef);
        /*set position of player, dynamic position*/
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body_boy = world_boy.createBody(bdef);

        int radius = 64;
        int xEdge = radius + 1;

        // Body
        FixtureDef fixtureDef = new FixtureDef();

        //fall - rotate
        //fixtureDef.density = 1;
        //Friction represents how easy or difficult it is for two solid objects to slide past
        //one another. Two pieces of waxed paper, for instance, would have very little friction
        //against one another, compared with two pieces of sandpaper.

        // when 0- good sliding next to ground or brick, but no "walking try" next to ice obstacle.
        // therefore it is 0.05, visually looks OK.
        fixtureDef.friction = 0.05f;
        b2body_boy.resetMassData();
        //Restitution is simply a measure of how bouncy an object is.
        //fixtureDef.restitution = 0.3f;

        CircleShape circleShape = new CircleShape();
        /* It is the distance between the player and the ground. */
        circleShape.setRadius(radius / CooperativeGame.PPM);
        /* Category means - what is this fixture?. */
        fixtureDef.filter.categoryBits = Constants.boyBit;
        /* What the player can collide with. */
        declareCollisions(fixtureDef);

        fixtureDef.shape = circleShape;
        b2body_boy.createFixture(fixtureDef);


        // HEAD
        /* a line between two different points. */
        EdgeShape boyHead = new EdgeShape();
        /* Define this line. */
        boyHead.set(new Vector2(-xEdge/CooperativeGame.PPM, 170/CooperativeGame.PPM),
                new Vector2(xEdge/CooperativeGame.PPM, 170/CooperativeGame.PPM));
        fixtureDef.shape = boyHead;
        /* a sensor does not collide with anything, it just let us the ability
        to ask for a query and get data. */
        fixtureDef.isSensor = true;
        b2body_boy.createFixture(fixtureDef).setUserData("BOY_HEAD");


        // LEGS
        FixtureDef fdefLegs = new FixtureDef();
        //CircleShape shapeLegs = new CircleShape();
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
        b2body_boy.createFixture(fdefLegs).setUserData("BOY_LEGS");


        // RIGHT
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
        b2body_boy.createFixture(fdefRight).setUserData("BOY_RIGHT");


        // LEFT
        FixtureDef fdefLeft = new FixtureDef();
        CircleShape shapeLeft = new CircleShape();
        /* Category means - what is this fixture?. */
        fdefLeft.filter.categoryBits = Constants.boyBit;
        /* What the player can collide with. */
        declareCollisions(fdefLeft);
        /* a line between two different points. */
        EdgeShape boyLeft = new EdgeShape();
        /* Define this line. */
        boyLeft.set(new Vector2(-xEdge/CooperativeGame.PPM, -75/CooperativeGame.PPM),
                new Vector2(-xEdge/CooperativeGame.PPM, 170/CooperativeGame.PPM));
        fdefLeft.shape = boyLeft;
        /* a sensor does not collide with anything, it just let us the ability
        to ask for a query and get data. */
        fdefLeft.isSensor = true;
        b2body_boy.createFixture(fdefLeft).setUserData("BOY_LEFT");
    }

    public void declareCollisions(FixtureDef fixtureDef){
        // Pay mode. Human Player ignore nature obstacles.
        if(CooperativeGame.gameData.getGame_params().getGame_help_logic() == Enums.HelpLogic.Pay) {
            fixtureDef.filter.maskBits = Constants.defaultBit | Constants.coinBit |
            Constants.brickBit | Constants.iceObsBit;
        }
        // Walk mode.
        else if(CooperativeGame.gameData.getGame_params().getGame_help_logic() == Enums.HelpLogic.Walk){
            fixtureDef.filter.maskBits = Constants.defaultBit | Constants.coinBit |
            Constants.brickBit | Constants.natureObsBit | Constants.iceObsBit;
        }
    }

    public void up(){
        currentTime = utils.getNanoTime();
        elapsedTime = currentTime - lastJumpTime;
        if (elapsedTime >= JUMP_DELAY) {
                /* If the player is standing in a place with high y value. */
            if (b2body_boy.getLinearVelocity().y == 0.1f) {
                b2body_boy.applyLinearImpulse(new Vector2(0, 1f),
                        b2body_boy.getWorldCenter(), true);
                lastJumpTime = utils.getNanoTime();

            } else if (b2body_boy.getLinearVelocity().y < 2f) {
                b2body_boy.applyLinearImpulse(new Vector2(0, 8.5f),
                        b2body_boy.getWorldCenter(), true);
                lastJumpTime = utils.getNanoTime();
            }
        }
    }

    public void right(){
        if (b2body_boy.getLinearVelocity().x <= 7){
            b2body_boy.applyLinearImpulse(new Vector2(1f, 0),
                    b2body_boy.getWorldCenter(), true);
        }
        isRight = true;
    }

    public void left(){
        if (b2body_boy.getLinearVelocity().x >= -7){
            b2body_boy.applyLinearImpulse(new Vector2(-1f, 0),
                    b2body_boy.getWorldCenter(), true);
        }
        isRight = false;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }

    public HumanPlayerRequestUtils getHumanPlayerRequestUtils() {
        return humanPlayerRequestUtils;
    }

    public void setHumanPlayerRequestUtils(HumanPlayerRequestUtils humanPlayerRequestUtils) {
        this.humanPlayerRequestUtils = humanPlayerRequestUtils;
    }

    protected String getTextureAtlasString()
    {
        return this.textureAtlasString;
    }

    public int getRealPlayerCol(int mapCellWidth) {
        return Math.round((b2body_boy.getPosition().x
                * CooperativeGame.PPM) / mapCellWidth);
    }

    public int getRealPlayerRow(int mapCellWidth, int mapHeight) {
        return Math.round(mapHeight - (b2body_boy.getPosition().y
                * CooperativeGame.PPM) / mapCellWidth);
    }
}
