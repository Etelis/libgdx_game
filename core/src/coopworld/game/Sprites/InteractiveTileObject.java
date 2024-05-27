package coopworld.game.Sprites;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import coopworld.game.CooperativeGame;
import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Tools.Constants;

/*
 * Class Name: InteractiveTileObject
 * Description: super class of collision objects.
 */
public abstract class InteractiveTileObject {
    //protected World world;
    protected TiledMap map;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;
    protected TiledMapTileLayer layer;
    protected int row; // row #0 is the lowest row on Tiled application
    protected int col; // col #0 is the leftest col on Tiled application
    protected GameScreen gameScreen;

    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds, int size, GameScreen gameScreen) {
     //   this.world = world;
        this.map = map;
        this.bounds = bounds;
        // Get graphics layer.
        this.layer = (TiledMapTileLayer) map.getLayers().get(Constants.COLLECTABLE_LAYER);
        this.gameScreen = gameScreen;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth()/2)/ CooperativeGame.PPM,
                (bounds.getY() + bounds.getHeight()/2)/ CooperativeGame.PPM);

        body = world.createBody(bdef);
        shape.setAsBox((bounds.getWidth()/2)/ CooperativeGame.PPM,
                (bounds.getHeight()/2)/ CooperativeGame.PPM);

        fdef.shape = shape;
        fixture = body.createFixture(fdef);

        // row indexing is different in Tiled application and here
        // (int)(body.getPosition().y * CooperativeGame.PPM / 64) we will mark this expression with ***
        // This expression (***) will return
        // the body's position such that index  is the bottom row in Tiled map.
        // What we want here is to have the same indexing as in the Tiled map application
        // (where row 0 is the top row).
        // To compute the row that we'd like to work with: the map's height - *** - 1 (for indexing)

        // 21, 11 -> need to be 20, 10
        float y = bounds.y;
        float x = bounds.x;

        //col:18, row:7 should be col:17, row:6
        this.row = GameScreen.mapHeight - Math.round((y) / 64) - size;
        this.col = Math.round(x / 64);

        /*
        // 21, 11 -> need to be 20, 10
        float y = body.getPosition().y;
        float x = body.getPosition().x;

        //col:18, row:7 should be col:17, row:6
        this.row = GameScreen.mapHeight - (int)(body.getPosition().y * CooperativeGame.PPM / 64) - 1;
        this.col = (int)(body.getPosition().x * CooperativeGame.PPM / 64);
         */

        /*
        // todo - change it!!
        if(isObs){
            this.row = this.row - 1;
            this.col = this.col - 1;
        }
        */
    }

    /***
     * This constructor will be called only in GeneratedTileObject class
     * It is used to create an interactive tile object without a rectangle shape from the tmx file.
     * @param world world
     * @param map tiledmap
     * @param col wanted col
     * @param row wanted row
     */
    protected InteractiveTileObject(World world,TiledMap map,int col ,int row, int size, GameScreen gameScreen) {
        this.map = map;
        this.layer = (TiledMapTileLayer) map.getLayers().get(Constants.COLLECTABLE_LAYER);
        this.gameScreen = gameScreen;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;

        int boundsX= col * GameScreen.mapCellHeight;
        // todo - height?
        //int boundsY = (GameScreen.mapHeight - 1) * GameScreen.mapCellHeight - (row*GameScreen.mapCellHeight); //mapCellHeight = 64
        int boundsY = (GameScreen.mapHeight - size) * GameScreen.mapCellHeight - (row*GameScreen.mapCellHeight); //mapCellHeight = 64

        int boundsWidth= GameScreen.mapCellHeight * size;
        int boundsHeight = GameScreen.mapCellHeight * size;

        this.bounds = new Rectangle(boundsX,boundsY,boundsWidth,boundsHeight);
        bdef.position.set((boundsX + boundsWidth/2)/ CooperativeGame.PPM,
                (boundsY + boundsHeight/2)/ CooperativeGame.PPM);

        body = world.createBody(bdef);
        shape.setAsBox((boundsWidth/2)/ CooperativeGame.PPM, (boundsHeight/2)/ CooperativeGame.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);

        this.row = row;
        this.col = col;
    }

    /* Hit functions - implemented in inherited classes. */
    public abstract void HeadHit(boolean virtualHit,Contact contact);
    public abstract void LegsHit(boolean virtualHit,Contact contact);
    public abstract void RightHit(boolean virtualHit,Contact contact);
    public abstract void LeftHit(boolean virtualHit,Contact contact);
    /* Set category bit. */
    public void setCategoriyFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
    /* Return the indexes of collision cell. */
    public TiledMapTileLayer.Cell getCell(){
        /* Scale up in PPM. */
        /*
        System.out.println("GET CELL X:" + (int)(body.getPosition().x * CooperativeGame.PPM / 64) + " Y:"
                + (int)(body.getPosition().y * CooperativeGame.PPM / 64) );
                */
        return layer.getCell((int)(body.getPosition().x * CooperativeGame.PPM / 64),
                (int)(body.getPosition().y * CooperativeGame.PPM / 64));
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void print()
    {
        System.out.print("Col:" + this.col + " Row:" + this.row);
    }

    /**
     * Equals function is according to interactive tile object position (row and col)
     * @param obj compared obj
     * @return true,false
     */
    @Override
    public boolean equals(Object obj) {
        InteractiveTileObject i = (InteractiveTileObject)obj;
        if ((i.getCol() == this.col) && (i.getRow() == this.row))
            return true;
        return false;
    }
}
