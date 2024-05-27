package coopworld.game.Tools;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import coopworld.game.CooperativeGame;
import coopworld.game.Scenes.Hud;
import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Sprites.Coin;
import coopworld.game.Sprites.IceObstacle;
import coopworld.game.Sprites.InteractiveTileObject;
import coopworld.game.Sprites.LockedCoin;
import coopworld.game.Sprites.LockedVirtualCoin;
import coopworld.game.Sprites.NatureObstacle;
import coopworld.game.Sprites.VirtualPlayerCoin;

import static coopworld.game.Tools.Constants.COINS_LAYER;
import static coopworld.game.Tools.Constants.GROUND_LAYER;
import static coopworld.game.Tools.Constants.HUMAN_PLAYER_POSITION;
import static coopworld.game.Tools.Constants.ICE_OBSTACLES_LAYER;
import static coopworld.game.Tools.Constants.LOCKED_COINS_LAYER;
import static coopworld.game.Tools.Constants.NATURE_OBSTACLES_LAYER;
import static coopworld.game.Tools.Constants.VIRTUAL_COINS_LAYER;
import static coopworld.game.Tools.Constants.VIRTUAL_LOCKED_COINS_LAYER;
import static coopworld.game.Tools.Constants.VIRTUAL_PLAYER_POSITION;

/*
 * Class Name: WorldBuilder
 * create ground, water, ladder, door and coin fixtures.
 */
public class WorldBuilder {
    TiledMap tiledMap;
    World world;
    Hud hud;

    // obstacles arrays.
    private static ArrayList<NatureObstacle> natureObstacles;
    private static ArrayList<IceObstacle> iceObstacles;

    // obstacles arrays.
    private static ArrayList<LockedCoin> lockedCoins;
    private static ArrayList<LockedVirtualCoin> lockedVirtualCoins;

    private GameScreen gameScreen;

    private Vector2 humanPlayerPosition, virtualPlayerPosition;

    public WorldBuilder(World world, TiledMap tiledMap, GameScreen gameScreen){
        this.tiledMap = tiledMap;
        this.world = world;
        this.gameScreen =  gameScreen;
        // default initial positions.
        this.humanPlayerPosition = new Vector2(6, 1);
        this.virtualPlayerPosition = new Vector2(10, 1);

        natureObstacles = new ArrayList<NatureObstacle>();
        iceObstacles = new ArrayList<IceObstacle>();

        lockedCoins = new ArrayList<LockedCoin>();
        lockedVirtualCoins = new ArrayList<LockedVirtualCoin>();

        // invisible layers.
        // note - I have noticed that making a layer invisible does not make sense
        // in the code, and isVisible() returns true. Therefore, I decided
        // to set it manually. The problem is only in object layers.
        // In tile layers - it works fine.


        // create all objects (only for visible layers).
        for(int i = 0; i < tiledMap.getLayers().getCount(); i++){
            build((short)i);
        }

        // fill gamescreen obstacles arrays.
        // match and store - ice obstacles and locked coins.
        for (IceObstacle iceObstacle: iceObstacles) {
            for(LockedCoin lockedCoin: lockedCoins) {
                if (areLinked(iceObstacle, lockedCoin)) {
                    gameScreen.getIceObstacles().put(iceObstacle, lockedCoin);
                    //iceObstacles.remove(iceObstacle);
                    //lockedCoins.remove(lockedCoin);
                    break;
                }
            }
        }

        // match and store - nature obstacles and locked virtual coins.
        for (NatureObstacle natureObstacle: natureObstacles) {
            for(LockedVirtualCoin lockedVirtualCoin: lockedVirtualCoins) {
                if (areLinked(natureObstacle, lockedVirtualCoin)) {
                    gameScreen.getNatureObstacles().put(natureObstacle, lockedVirtualCoin);
                    //natureObstacles.remove(natureObstacle);
                    //lockedVirtualCoins.remove(lockedVirtualCoin);
                    break;
                }
            }
        }
    }

    public boolean areLinked(InteractiveTileObject obstacle, InteractiveTileObject lockedCoin){
        if(obstacle.getCol() == lockedCoin.getCol() - 1){
            if(obstacle.getRow() == lockedCoin.getRow() - 1){
                return true;
            }
        }
        return false;
    }

    public Vector2 getHumanPlayerPosition() {
        return humanPlayerPosition;
    }

    public Vector2 getVirtualPlayerPosition() {
        return virtualPlayerPosition;
    }

    public void build(short name){
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;
        BodyDef bodyDef = new BodyDef();

       /* Create coin - layer 11 represents the virtual locked coins in map. */
        MapLayer mapLayer = tiledMap.getLayers().get(name);
            if(mapLayer.isVisible()){
            switch (name){
                case GROUND_LAYER:
                    for(MapObject object : mapLayer.getObjects()
                            .getByType(RectangleMapObject.class)){
                        Rectangle rect = ((RectangleMapObject) object).getRectangle();
                        RectangleMapObject r = (RectangleMapObject) object;

                        /* Ground is static. */
                        rect.setX((Math.round(rect.getX())));
                        rect.setY((Math.round(rect.getY())));
                        bodyDef.type = BodyDef.BodyType.StaticBody;
                        bodyDef.position.set((rect.getX() + rect.getWidth()/2)/ CooperativeGame.PPM,
                                (rect.getY() + rect.getHeight()/2)/CooperativeGame.PPM);
                        body = world.createBody(bodyDef);
                        shape.setAsBox((rect.getWidth() / 2) / CooperativeGame.PPM, (rect.getHeight() / 2) /
                                CooperativeGame.PPM);
                        fixtureDef.shape = shape;
                        body.createFixture(fixtureDef);
                    }
                    break;

                case ICE_OBSTACLES_LAYER:
                    for(MapObject object : mapLayer.getObjects().
                            getByType(RectangleMapObject.class)) {
                        // bottom left in libGdx indices.
                        Rectangle rect = ((RectangleMapObject) object).getRectangle();
                        rect.setX((Math.round(rect.getX())));
                        rect.setY((Math.round(rect.getY())));
                        // Create ice obstacle object and add it to ice obstacles array.
                        iceObstacles.add(new IceObstacle(world, tiledMap, rect, 3, gameScreen));
                    }
                    break;

                case NATURE_OBSTACLES_LAYER:
                    for(MapObject object : mapLayer.getObjects().
                            getByType(RectangleMapObject.class)) {
                        Rectangle rect = ((RectangleMapObject) object).getRectangle();
                        rect.setX((Math.round(rect.getX())));
                        rect.setY((Math.round(rect.getY())));
                        // Create nature obstacle object and add it to nature obstacles array.
                        natureObstacles.add(new NatureObstacle(world, tiledMap, rect, 3, gameScreen));
                    }
                    break;

                case COINS_LAYER:
                    for(MapObject object : mapLayer.getObjects().
                            getByType(RectangleMapObject.class)) {
                        Rectangle rect = ((RectangleMapObject) object).getRectangle();
                        rect.setX((Math.round(rect.getX())));
                        rect.setY((Math.round(rect.getY())));
                        /* Create coin object.*/
                        gameScreen.getCoins().add(new Coin(world, tiledMap, rect, 1, gameScreen));
                    }
                    break;

                case VIRTUAL_COINS_LAYER:
                    for(MapObject object : mapLayer.getObjects().
                            getByType(RectangleMapObject.class)) {
                        Rectangle rect = ((RectangleMapObject) object).getRectangle();
                        rect.setX((Math.round(rect.getX())));
                        rect.setY((Math.round(rect.getY())));
                        // Create virtual coin object.
                        gameScreen.getVirtualCoins().add(new VirtualPlayerCoin(world, tiledMap, rect, 1, gameScreen));
                    }
                    break;

                case LOCKED_COINS_LAYER:
                    for(MapObject object : mapLayer.getObjects().
                            getByType(RectangleMapObject.class)){
                        Rectangle rect = ((RectangleMapObject) object).getRectangle();
                        rect.setX((Math.round(rect.getX())));
                        rect.setY((Math.round(rect.getY())));
                        // Create locked coin object and add it to locked coins array.
                        lockedCoins.add(new LockedCoin(world, tiledMap, rect, 1, gameScreen));
                    }
                    break;

                case VIRTUAL_LOCKED_COINS_LAYER:
                    for(MapObject object : mapLayer.getObjects().getByType(RectangleMapObject.class)){
                        Rectangle rect = ((RectangleMapObject) object).getRectangle();
                        rect.setX((Math.round(rect.getX())));
                        rect.setY((Math.round(rect.getY())));
                        // Create virtual locked coins object and add it to locked coins array.
                        lockedVirtualCoins.add(new LockedVirtualCoin(world, tiledMap, rect, 1, gameScreen));
                    }
                    break;

                case HUMAN_PLAYER_POSITION:
                    for(MapObject object : mapLayer.getObjects().getByType(RectangleMapObject.class)){
                        Rectangle rect = ((RectangleMapObject) object).getRectangle();
                        humanPlayerPosition.x = Helping.getTiledCol(Math.round(rect.getX()));
                        humanPlayerPosition.y = Helping.getTiledRow(Math.round(rect.getY()));
                    }
                    break;

                case VIRTUAL_PLAYER_POSITION:
                    for(MapObject object : mapLayer.getObjects().getByType(RectangleMapObject.class)){
                        Rectangle rect = ((RectangleMapObject) object).getRectangle();
                        virtualPlayerPosition.x = Helping.getTiledCol(Math.round(rect.getX()));
                        virtualPlayerPosition.y = Helping.getTiledRow(Math.round(rect.getY()));
                    }
                    break;
            }
        }
    }
}
