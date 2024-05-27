package coopworld.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import coopworld.game.Sprites.InteractiveTileObject;

/*
 * Class Name: WorldContactListener
 * implement ContactListener,when two objects of box2d collide each other
 * identify what kind of collision occurred,and point to the right function
 */
public class WorldContactListener implements ContactListener {
    private boolean virtualCollision;

    /* When two objects begin to make a connection. */
    public void beginContact(Contact contact) {
        /* Write collision to log. */
        /* Get 2 fixture of collision. */
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        /* If both fixtures are not null. */
        if(fixA.getUserData() != null && fixB.getUserData() != null){
            if (fixA.getUserData().toString().contains("VIRTUAL") || fixB.getUserData().toString().contains("VIRTUAL")) {
                virtualCollision = true;
            }
            /* 2 fixtures - a head and another object. */
            if (fixA.getUserData().toString().contains("BOY_HEAD") || fixB.getUserData().toString().contains("BOY_HEAD")) {
                Fixture head = fixA.getUserData().toString().contains("BOY_HEAD") ? fixA : fixB;
                Fixture object = head == fixA ? fixB : fixA;
                /* If player's head collide with interactive tile object - go to
                 onHeadHit function. */
                if (object.getUserData() instanceof InteractiveTileObject) {
                    ((InteractiveTileObject) object.getUserData()).HeadHit(virtualCollision, contact);
                }
            }
            /* 2 fixtures - legs and another object. */
            if (fixA.getUserData().toString().contains("BOY_LEGS") || fixB.getUserData().toString().contains("BOY_LEGS")) {
                Fixture head = fixA.getUserData().toString().contains("BOY_LEGS") ? fixA : fixB;
                Fixture object = head == fixA ? fixB : fixA;
                /* If player's legs collide with interactive tile object - go to
                 onLegsHit function. */
                if (object.getUserData() instanceof InteractiveTileObject) {
                    ((InteractiveTileObject) object.getUserData()).LegsHit(virtualCollision, contact);
                }
            }
            /* 2 fixtures - player's right side and another object. */
            if (fixA.getUserData().toString().contains("BOY_RIGHT") || fixB.getUserData().toString().contains("BOY_RIGHT")) {
                Fixture head = fixA.getUserData().toString().contains("BOY_RIGHT") ? fixA : fixB;
                Fixture object = head == fixA ? fixB : fixA;
                /* If player's right side collide with interactive tile object -
                 go to onRightHit function. */
                if (object.getUserData() instanceof InteractiveTileObject) {
                    ((InteractiveTileObject) object.getUserData()).RightHit(virtualCollision, contact);
                }
            }
            /* 2 fixtures - player's left side and another object. */
            if (fixA.getUserData().toString().contains("BOY_LEFT") || fixB.getUserData().toString().contains("BOY_LEFT")) {
                //System.out.println("HITTTTT LEFT!!!");
                Fixture head = fixA.getUserData().toString().contains("BOY_LEFT") ? fixA : fixB;
                Fixture object = head == fixA ? fixB : fixA;
                /* if player's left side collide with interactive tile object -
                 go to onLeftHit function. */
                if (object.getUserData() instanceof InteractiveTileObject) {
                    ((InteractiveTileObject) object.getUserData()).LeftHit(virtualCollision, contact);
                }
            }
            virtualCollision = false; // back to default
        }
    }
    /* When two objects stop to make a connection, write end of collision to log. */
    public void endContact(Contact contact) {
        Gdx.app.log("INTO endContact", "");
    }
    /* If we want to change the characteristics of this collision. */
    public void preSolve(Contact contact, Manifold oldManifold) {
    }
    /* What happened after the collision. angles etc... */
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}